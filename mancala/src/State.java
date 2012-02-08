public class State {

	private int[] holes = new int[Constants.MAX_SLOTS * 2 + 2]; //12+2

	public State(Board aBoard) {
		for (int i = 0; i < holes.length; i++) {
			holes[i] = aBoard.getSlotValue(i);
		}
	}
	

	
	
	
	

	/*
	 * H1: (My Mancala – Opponent’s Mancala) 8 
	 * H2: How close I am to winning 6
	 * H3: How close my opponent is to winning 6 focus on go again 
	 * H4: # of stones close to my home (1/3) 5 
	 * H5: # of stones away from my home (1/3) 4  
	 * 		having your stones farthest from home would result in the greatest
	 * 		possibility for a capture or a go-again. 
	 * H6: # of stone in the middle (1/3) 5
	 */

	public int getFeature1(int me) {
		return (holes[me] - holes[Constants.inverse(me)]);
	}

	public int getFeature2(int me) {
		return holes[me] - Constants.MAX_SLOTS * Constants.MAX_STONES;
	}

	public int getFeature3(int me) {
		return holes[Constants.inverse(me)] - Constants.MAX_SLOTS
				* Constants.MAX_STONES;
	}

	public int getFeature4(int me) {
		if (me == Constants.PLAYER1) {
			return holes[0] + holes[1];
		}
		else {
			return holes[7] + holes [8];
		}

	}

	public int getFeature5(int me) {
		if (me == Constants.PLAYER1) {
			return holes[2] + holes[3];
		}
		else {
			return holes[9] + holes [10];
		}
	}

	public int getFeature6(int me) {
		if (me == Constants.PLAYER1) {
			return holes[4] + holes[5];
		}
		else {
			return holes[11] + holes [12];
		}
	}
}
