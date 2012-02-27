/*
 * author: Khanh Nguyen
 * 
 * class name: LeastSquareState
 * description: 
 */

public class LeastSquareState {

	private MancalaGameState board;
	private int player;
	private int opponent;
	
	public LeastSquareState(MancalaGameState board, int player) {
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
		/* Feature 1: My score */
		case 1:
			return board.getScore(player);
					
		/* Feature 2: Opponent score */
		case 2:
			return board.getScore(opponent);
					
		// Feature 3: # stones on my side
		case 3:
			int sum=0;
			for (int i = 0; i< 6; i++)
				sum += board.stonesAt(player, i);
//			sum -= StonesCouldBeCaptured();
			return sum;
		
		// Feature 4: # stones on opponent's side
		case 4:
			sum=0;
			for (int i = 0; i< 6; i++)
				sum += board.stonesAt(opponent, i);
//			sum -= StonesCouldCapture();
			return sum;
			
		// Feature 5: # empty slots on my side 
		case 5: 
			sum = 0;
			for (int i = 0; i< 6; i++)
				if (board.stonesAt(player, i) == 0)
					sum++;
			return sum;
					
		// Feature 6: # empty slots on opponent's side
		case 6:
			sum = 0;
			for (int i = 0; i< 6; i++)
				if (board.stonesAt(opponent, i) == 0)
					sum++;
			return sum;
			
		case 7:
			int max =0;
			for (int i =0; i< 6; i++)
				if ((board.stonesAt(opponent, i) != 0) && (board.stonesAt(player,i) ==0)){
					
					if (opponent == 1){ 
						for (int j =0; j < i;j++)
							if ((board.stonesAt(player, j) != 0) &&
							       (board.stonesAt(player, j)+j == i)) 
								if (max < board.stonesAt(opponent,i)) max = board.stonesAt(opponent,i);
						for (int j = i; j <6; j++)
							if (board.stonesAt(player, j) == 13-j+i)
								if (max < board.stonesAt(opponent,i)) max = board.stonesAt(opponent,i);
					}
					else {
						for (int j = i+1; j <6; j++)
							if ((board.stonesAt(player, j) != 0) &&
							       (j- board.stonesAt(player, j) == i)) 
								if (max < board.stonesAt(opponent,i)) max = board.stonesAt(opponent,i);
						for (int j =0; j <= i;j++)
							if (board.stonesAt(player, j) == 13-i+j)
								if (max < board.stonesAt(opponent,i)) max = board.stonesAt(opponent,i);
					}
				}
			return max;
				
		case 8:
				max =0;
				for (int i =0; i< 6; i++)
					if ((board.stonesAt(opponent, i) == 0) && (board.stonesAt(player,i) !=0)){
						
						if (player == 1){ 
							for (int j =0; j < i;j++)
								if ((board.stonesAt(opponent, j) != 0) &&
								       (board.stonesAt(opponent, j)+j == i)) 
									if (max < board.stonesAt(player,i)) max = board.stonesAt(player,i);
							for (int j = i; j <6; j++)
								if (board.stonesAt(opponent, j) == 13-j+i)
									if (max < board.stonesAt(player,i)) max = board.stonesAt(player,i);
						}
						else {
							for (int j = i+1; j <6; j++)
								if ((board.stonesAt(opponent, j) != 0) &&
								       (j- board.stonesAt(opponent, j) == i)) 
									if (max < board.stonesAt(player,i)) max = board.stonesAt(player,i);
							for (int j =0; j <= i;j++)
								if (board.stonesAt(opponent, j) == 13-i+j)
									if (max < board.stonesAt(player,i)) max = board.stonesAt(player,i);
						}
					}
				return max;
				
		default:
			System.out.println("ERROR: LeastSquareState.java = invalid feature number!");
			return -1;
		}
		
	}
	
}
