/*
 * author: William Lam
 * author: Andrew Furusawa
 * 
 * class name: AILearning_Player
 * description: 
 */

public class AILearning_Player implements MancalaPlayer {
	
	private int player;
	private int opponent;
	private double learning_rate;
	
	RegressionLearning ai;

	public AILearning_Player (int playerNum) {
	  player = playerNum;
	  opponent = 1 - player;
	  learning_rate = 0.003;
	 
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
//		ai.printThetas(ai.getWeight());
	    if (!gs.checkEndGame()) return null;
	    
//	    Make a copy to compute the final score
	    MancalaGameState gsCopy = gs.copy();
	    gsCopy.computeFinalScore();
	    RegressionState aState = new RegressionState(gsCopy,player);
		ai.updateHistory(aState);
		ai.incGamesPlayed();
		
	    if (gsCopy.getScore(player) > gsCopy.getScore(1-player)) {
	        ai.setReward(gsCopy.getScore(player)-gsCopy.getScore(1-player));
	        System.out.printf("AI Win \t\n ");
	    }
	    else if (gsCopy.getScore(player) < gsCopy.getScore(1-player)) {
	        ai.setReward(gsCopy.getScore(player)-gsCopy.getScore(1-player));
	        System.out.printf("AI Loose \t\n ");
	    }
	    else  {
	        ai.setReward(0);
	        System.out.printf("Draw \t\n ");
	    }
	    if  (ai.hasNoSample()) {
	    	ai.saveSample();
	    }
	    
	    ai.gradientDescent((double)learning_rate/ai.getGamesPlayed());
//	    ai.printThetas(ai.getWeight());
	    ai.checkEvalFunction();
	    ai.reset();
	    ai.saveThetas("data.txt");
	    return null;
	}

}
