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
	  
	  ai = new RegressionLearning();
	  //load theta values.
	  
	}
	
	public int getMove(MancalaGameState gs) throws Exception {

		
		//int offset = player - Constants.PLAYER1;
		double max = Float.NEGATIVE_INFINITY;
		int bestMove = -1;
		
		for (int i = 0; i < 6; i++) {
			
			if (gs.validMove(i)) {
				
				//copy board and values given you execute the move.
				MancalaGameState newBoard = gs.copy();
				newBoard.play(i);
				
				
				RegressionState newState = new RegressionState(newBoard, player);
				
				//calculate the state value for each slot and take the best one.
				double stateValue = calStateValue(weight[player%Constants.PLAYER1], newState);
				if (stateValue > max) {
					max = stateValue;
					bestMove = i;
				}
			}
		}
		
		
		if (bestMove != -1) {
			MancalaGameState newBoard = gs.copy();
			newBoard.play(bestMove);
			updateHistory(state);
		}
		
		
		 
		return bestMove;
	
	}
	
	
	/* This method will be used to learn from the game we just played. */
	public Object postGameActions(MancalaGameState gs) {
	    if (!gs.checkEndGame()) return null;
	
	    // Make a copy to compute the final score
	    MancalaGameState gsCopy = gs.copy();
	    gsCopy.computeFinalScore();
	    
		
		
		ai = new RegressionLearning(gs, player);
		
		
		
	
	    if (gsCopy.getScore(player) > gsCopy.getScore(opponent)) {
	        System.out.println("I win!");    	
	    }
	
	    else if (gsCopy.getScore(player) < gsCopy.getScore(opponent)) {
	        System.out.println("I lost...");
	    }
	    
	    else {
	        System.out.println("I tied.");
	    }
	
	    return null;
	}

}
