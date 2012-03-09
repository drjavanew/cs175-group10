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
			
		// Feature 6: # stones on my side what could be captured
		case 6: 
			sum =0;
			for (int i =0; i< 6; i++)
				if ((board.stonesAt(opponent, i) == 0) && (board.stonesAt(player,i) !=0)){
					
					if (player == 1){ 
						for (int j =0; j < i;j++)
							if ((board.stonesAt(opponent, j) != 0) &&
							       (board.stonesAt(opponent, j)+j == i)) 
								sum += board.stonesAt(player,i);
						for (int j = i; j <6; j++)
							if (board.stonesAt(opponent, j) == 13-j+i)
									sum += board.stonesAt(player,i)+1;
					}
					else {
						for (int j = i+1; j <6; j++)
							if ((board.stonesAt(opponent, j) != 0) &&
							       (j- board.stonesAt(opponent, j) == i)) 
								sum += board.stonesAt(player,i);
						for (int j =0; j <= i;j++)
							if (board.stonesAt(opponent, j) == 13-i+j)
									sum += board.stonesAt(player,i)+1;
					}
				}
			return sum;
					
		// Feature 7: # stones on opponent's side what could be captured
		case 7:
			sum =0;
			for (int i =0; i< 6; i++)
				if ((board.stonesAt(opponent, i) != 0) && (board.stonesAt(player,i) ==0)){
					
					if (opponent == 1){ 
						for (int j =0; j < i;j++)
							if ((board.stonesAt(player, j) != 0) &&
							       (board.stonesAt(player, j)+j == i)) 
								sum += board.stonesAt(opponent,i);
						for (int j = i; j <6; j++)
							if (board.stonesAt(player, j) == 13-j+i)
									sum += board.stonesAt(opponent,i)+1;
					}
					else {
						for (int j = i+1; j <6; j++)
							if ((board.stonesAt(player, j) != 0) &&
							       (j- board.stonesAt(player, j) == i)) 
								sum += board.stonesAt(opponent,i);
						for (int j =0; j <= i;j++)
							if (board.stonesAt(player, j) == 13-i+j)
									sum += board.stonesAt(opponent,i)+1;
					}
				}
			return sum;
			
		
		
		default:
			System.out.println("ERROR: RegressionState.java = invalid feature number!");
			return -1;
		}
		
	}
	
	
}
