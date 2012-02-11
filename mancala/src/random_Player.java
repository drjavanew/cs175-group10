import java.util.Random;

/*
 * author: William Lam
 * author: Andrew Furusawa
 * 
 * class name: random_Player
 * description: this player will just play random move, 
 * there is no intelligence whatsoever
 */

public class random_Player implements MancalaPlayer {
	
	private int player;
	private int opponent;
	
	

	public random_Player (int playerNum) {
	  player = playerNum;
	  opponent = 1 - player;
	 
	}
	
	public int getMove(MancalaGameState gs) throws Exception {

		boolean inputOK = false;
		int randomInt;
		do {
			Random r = new Random();
			randomInt = r.nextInt(6);

			inputOK = gs.validMove(randomInt);

		} while (!inputOK);

		return randomInt;
		
	
	}
	
	
	/* This method will be used to learn from the game we just played. */
	public Object postGameActions(MancalaGameState gs) {
		
	    if (!gs.checkEndGame()) return null;
	    
	    return null;
	}

}
