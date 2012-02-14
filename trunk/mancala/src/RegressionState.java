/*
 * author: Andrew Furusawa
 * 
 * class name: RegressionState
 * description: 
 */

public class RegressionState {

	private MancalaGameState board;
	private int player;
	private int opponent;
	
	public RegressionState(MancalaGameState board, int player) {
		this.board = board;
		this.player = player;
		opponent = 1 - player;
	}
	
	
	/*
	 * FEATURE METHOD
	 */

	public int getFeature(int feature) {

		
		switch (feature) {
		
		/* Feature 0: just for constant */
		case 0:
			return 1;
		/* Feature 1: Difference between my score and opponent's score. */
		case 1:
			return board.getScore(player) - board.getScore(opponent);
					
		/* Feature 2: How close I am to 24 stones (win condition). */
		case 2:
			return board.getScore(player) - 24;
					
		/* Feature 3: How close my opponent is to 24 stones (lose condition). */
		case 3:
			return board.getScore(opponent) - 24;
		
		// Feature 4: # stones on my side
		case 4:
			int sum=0;
			for (int i = 0; i< 6; i++)
				sum += board.stonesAt(player, i);
			return sum;
		
		// Feature 5: # stones on opponent's side
		case 5:
			sum=0;
			for (int i = 0; i< 6; i++)
				sum += board.stonesAt(opponent, i);
			return sum;
			
		// Feature 6: # stones on my side that could be captured
		
		// Feature 7: # stones on opponent's side what could be captured
			
			
		/* Feature 4: # of stones in 2 closet slots. 
		case 4:
			return board.stonesAt(player, 4) + board.stonesAt(player, 5);
		
		 Feature 5: # of stones in 2 middle slots. 
		case 5:
			return board.stonesAt(player, 2) + board.stonesAt(player, 3);
		
		 Feature 6: # of stones in 2 farthest slots. 
		case 6:
			return board.stonesAt(player, 0) + board.stonesAt(player, 1);*/
		
		
		default:
			System.out.println("ERROR: RegressionState.java = invalid feature number!");
			return -1;
		}
		
	}
	
	
}
