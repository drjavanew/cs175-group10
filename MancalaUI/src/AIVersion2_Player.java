/*
 * 
 * 
 * class name: AI Version 2 using least square 
 * description: No longer used.
 */

public class AIVersion2_Player implements MancalaPlayer {
	
	private int player;
	private int opponent;
	private double learning_rate;
	
	LeastSquare ai;

	public AIVersion2_Player (int playerNum) {
	  player = playerNum;
	  opponent = 1 - player;
	  learning_rate = 0.003;
	 
	  // initialize ai and load theta values.
	  ai = new LeastSquare(playerNum,"LQdata.txt");
	 
	  
	}
	
	public int getMove(MancalaGameState gs) throws Exception {

		ai.board = gs.copy();
		int bestMove = -1;
		Node evalNode = new Node(gs.copy(), 0);
		bestMove = ai.findBestMove(evalNode, bestMove);
		
		if (bestMove != -1) {
			ai.updateHistory(gs.copy());
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
	    
		ai.updateHistory(gsCopy);
		
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
	    
//	    ai.printThetas(ai.getWeight());
	    ai.learnWeights();
	    ai.reset();
	    ai.saveThetas("LQdata.txt");
	    return null;
	}

	@Override
	public Object actionsBeforeDeletion() {
		// TODO Auto-generated method stub
		return null;
	}

}
