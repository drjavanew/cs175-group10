/*
 * 
 * 
 * class name: AI Version 2 using normal equation 
 * description: 
 */

public class AINormalEq_Player implements MancalaPlayer {
	
	private int player;
	private int opponent;
	
	private NormalEquation ai;

	public AINormalEq_Player (int playerNum) {
	  player = playerNum;
	  opponent = 1 - player;
	 
	  // initialize ai and load theta values.
	  ai = new NormalEquation(playerNum,"NEdata.txt");
	 
	  
	}
	
	public int getMove(MancalaGameState gs) throws Exception {

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
		
		int finalscore = gsCopy.getScore(player)-gsCopy.getScore(opponent);
		ai.setReward(finalscore);
		
	    if (finalscore > 0) {
	        System.out.println("AIV2 Win");
	    }
	    else if (finalscore < 0) {
	        System.out.println("AIV2 Loose");
	    }
	    else  {
	        System.out.println("Draw");
	    }
	    
	    
	    ai.learnWeights();
//	    ai.printThetas(ai.getWeight());
	    ai.reset();
	    return null;
	}

	@Override
	public Object actionsBeforeDeletion() {
		ai.cleanup();
		return null;
	}

}
