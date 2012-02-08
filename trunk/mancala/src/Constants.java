public class Constants {
	
	static final int MAX_STONES = 4;
	static final int MAX_SLOTS = 6;
	static final int PLAYER1 = MAX_SLOTS; //6
	static final int PLAYER2 = MAX_SLOTS * 2 + 1; //13

	public static int inverse(int player) {
		if (player == PLAYER1)
			return PLAYER2;
		else
			return PLAYER1;
	}
}
