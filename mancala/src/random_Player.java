/*
 * author: William Lam
 * author: Andrew Furusawa
 * 
 * class name: random_Player
 * description: 
 */

public class random_Player implements MancalaPlayer {
	
	private int player;
	private int opponent;
	
	RegressionLearning ai;

	public random_Player (int playerNum) {
	  player = playerNum;
	  opponent = 1 - player;
	 
	  // initialize ai and load theta values.
	  ai = new RegressionLearning(playerNum,"data.txt");
	 
	  
	}
	
	public int getMove(MancalaGameState gs) throws Exception {

		ai.board = gs.copy();
		int bestMove = -1;
		Node evalNode = new Node(gs.copy(), 0);
		bestMove = ai.findBestMove(evalNode, bestMove);
		
		if (bestMove != -1) {
			RegressionState aState = new RegressionState(gs.copy(),player);
			ai.updateHistory(aState);
		}
		 
		return bestMove;
	
	}
	
	
	/* This method will be used to learn from the game we just played. */
	public Object postGameActions(MancalaGameState gs) {
		ai.printThetas(ai.getWeight());
	    if (!gs.checkEndGame()) return null;
	    
//	    Make a copy to compute the final score
	    MancalaGameState gsCopy = gs.copy();
	    gsCopy.computeFinalScore();
	    RegressionState aState = new RegressionState(gsCopy,player);
		ai.updateHistory(aState);
		ai.incGamesPlayed();
		System.out.println(ai.getGamesPlayed());
		
	    if (gsCopy.getScore(player) > gsCopy.getScore(1-player))
	        ai.setReward(1);
	    else if (gsCopy.getScore(player) < gsCopy.getScore(1-player)) 
	        ai.setReward(-1);
	    else 
	        ai.setReward(0);
	    
	    ai.gradientDescent((double)0.01);
	    ai.printThetas(ai.getWeight());
	    ai.saveThetas("data.txt");
	    return null;
	}

}
