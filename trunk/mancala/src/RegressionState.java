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
		board = this.board;
		player = this.player;
		opponent = 1 - player;
	}
	
	
	/*
	 * FEATURE METHOD
	 */

	public int getFeature(int feature) {

		int value = 0;
		/* Feature 1: Difference between my score and opponent's score. */
		if(feature == 1) {
			value = board.getScore(player) - board.getScore(opponent);
		}
		
		/* Feature 2: How close I am to 24 stones (win condition). */
		else if(feature == 2) {
			value = board.getScore(player) - 24;
		}
		
		/* Feature 3: How close my opponent is to 24 stones (lose condition). */
		else if(feature == 3) {
			value = board.getScore(opponent) - 24;		
		}
		
		/* Feature 4: # of stones in 2 closet slots. */
		else if(feature == 4) {
			value = board.stonesAt(player, 4) + board.stonesAt(player, 5);
		}
		
		/* Feature 5: # of stones in 2 middle slots. */
		else if(feature == 5) {
			value = board.stonesAt(player, 2) + board.stonesAt(player, 3);
			
		}
		
		/* Feature 6: # of stones in 2 farthest slots. */
		else if(feature == 6) {
			value = board.stonesAt(player, 0) + board.stonesAt(player, 1);
			
		}
		
		else {
			System.out.println("ERROR: RegressionState.java = invalid feature number!");
		}
		
		return value;
	}
	
	
}
