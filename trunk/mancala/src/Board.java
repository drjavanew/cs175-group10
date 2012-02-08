import java.io.*;

public class Board {

	/* Board */
	private static final int MAX_STONES = Constants.MAX_STONES;
	private static final int MAX_SLOTS = Constants.MAX_SLOTS;
	private static final int PLAYER1 = Constants.PLAYER1;
	private static final int PLAYER2 = Constants.PLAYER2;
	private int[] slots = new int[MAX_SLOTS * 2 + 2];

	/* Game States */
	static final int WIN_P1 = 1; // Player 1 wins.
	static final int WIN_P2 = 2; // Player 2 wins.
	static final int DRAW = 0; // Game is a draw.
	static final int PLAYING = -1; // Game is still going.

	int turn; // Options: PLAYER1, PLAYER2
	int gameState; // Options: WIN_P1, WIN_P2, DRAW, PLAYING
	static int PLAYER1_CutOffDepth;
	static int PLAYER2_CutOffDepth;

	/**
	 * @param args
	 */

	/* Constructor 1 */
	public Board() {

		/* Initial values. */
		gameState = PLAYING;

		/* Set all slots to contain 4 stones. */
		for (int i = 0; i < slots.length; i++) {
			slots[i] = MAX_STONES;
		}

		slots[PLAYER1] = 0; // This is player 1's mancala store.
		slots[PLAYER2] = 0; // This is player 2's mancala store.
	}

	/* Constructor 2 - Copy constructor */
	public Board(Board aBoard) {

		gameState = aBoard.getGameState();

		for (int i = 0; i < slots.length; i++)
			slots[i] = aBoard.getSlotValue(i);

		turn = aBoard.getTurn();
	}

	/* Accessor Methods */
	public int getSlotValue(int i) {
		return slots[i];
	}

	public int getTurn() {
		return turn;
	}

	public Board get() {
		return this;
	}

	public int getGameState() {
		return gameState;
	}

	private int checkEmptySide(int player) {
		int start = player - PLAYER1;
		int end = player;

		for (int i = start; i < end; i++)
			if (slots[i] > 0)
				return 0;
		return 1;
	}

	public boolean isGameEnd() {
		int i = checkEmptySide(turn);
		if (i == 0) {
			int j = checkEmptySide(inverse(turn));

			if (j == 1) { // game end - empty on the other side
//				collectStones(turn);
				return true;
			}

			else {
				return false;
			}
		}

		else { // game end - empty on our side
//			collectStones(turn);
//			collectStones(inverse(turn));
			return true;
		}
	}
/*	public boolean isGameEnd() {
		int i = checkEmptySide(turn);
		if (i == 0) {
			int j = checkEmptySide(inverse(turn));

			if (j == 1) { // game end - empty on the other side
				collectStones(turn);
				return true;
			}

			else {
				return false;
			}
		}

		else { // game end - empty on our side
			collectStones(turn);
			collectStones(inverse(turn));
			return true;
		}
	}*/

	public boolean isLegalMove(int id) {

		if ((id == PLAYER1) || (id >= PLAYER2) || (slots[id] == 0))
			return false;

		if (turn == PLAYER1)
			if (id >= turn)
				return false;
			else
				return true;

		else if (id > turn / 2)
			return true;
		else
			return false;

	}

	private int inverse(int player) {
		if (player == PLAYER1)
			return PLAYER2;
		else
			return PLAYER1;
	}

	private void collectStones(int player) {
		for (int i = 1; i <= MAX_SLOTS; i++) {
			slots[player] += slots[player - i];
			slots[player - i] = 0;
		}
	}

	public void setGameState() {
		if (isGameEnd()) {
			collectStones(turn);
			collectStones(inverse(turn));
			if (slots[PLAYER1] > slots[PLAYER2]) {
				gameState = WIN_P1;
			} else {
				if (slots[PLAYER1] < slots[PLAYER2]) {
					gameState = WIN_P2;
				} else {
					gameState = DRAW;
				}

			}
		} else {
			gameState = PLAYING;
		}
	}

	public void move(int id) {
		// id MUST BE legal move already

		int i = id;
		while (slots[id] > 0) {
			i++;
			i = i % (MAX_SLOTS * 2 + 2);
			if (i != inverse(turn)) {
				slots[i]++;
				slots[id]--;
			}
		}

		if ((slots[i] == 1) && (isLegalMove(i))) {
			// landed at empty slot on own side
			// System.out.println("CAPTURE");
			int opposite = MAX_SLOTS * 2 - i;
			if (slots[opposite] > 0) {
				slots[turn] += slots[i];
				slots[turn] += slots[opposite];
				slots[i] = 0;
				slots[opposite] = 0;
			}
		}

		// toggle turn if not double turn
		if (!(i == turn))
			turn = inverse(turn);
		// else System.out.println("DOUBLE TURN");
	}

	public void draw() {
		int i;

		for (i = PLAYER2 - 1; i > PLAYER1; i--)
			System.out.printf("[ %2d ]", i);
		System.out.println();
		for (i = 0; i < PLAYER1; i++)
			System.out.printf("[ %2d ]", i);
		System.out.println();
		for (i = PLAYER2 - 1; i > PLAYER1; i--)
			System.out.printf("  %2d  ", slots[i]);
		System.out.println();
		System.out.println(slots[PLAYER2]);

		for (i = 0; i < PLAYER1; i++)
			System.out.printf("[ %2d ]", i);
		System.out.println();
		for (i = 0; i < PLAYER1; i++)
			System.out.printf("  %2d  ", slots[i]);
		System.out.println();
		System.out.println(slots[PLAYER1]);

	}

	public void setTurn(int i) {
		if (i == 1)
			turn = PLAYER1;
		else
			turn = PLAYER2;

	}

	public int requestMove() {
		boolean inputOK = false;
		int i = 0;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		do {
			System.out.printf("PLAYER %2d - Please pick slot",
					(turn % MAX_SLOTS) + 1);
			try {
				i = Integer.parseInt(in.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (i == -1)
				return -1;
			inputOK = isLegalMove(i);
			if (!inputOK)
				System.out.println("PICK ANOTHER NUMBER!!!!");
		} while (!inputOK);

		return i;
	}

	
//	public void playGame(){
//		int i=0; 
//		int moveid=0; 
//		BufferedReader in=new  BufferedReader(new InputStreamReader(System.in));
//	  
//	  do { 
//		  System.out.println("ENTER -1 to quit - PICK WHO GO FIRST 1 or 2");
//	  try { 
//		  i = Integer.parseInt(in.readLine()); 
//		  } 
//	  catch (IOException e) { 
//		  //  TODO Auto-generated catch block 
//		  e.printStackTrace(); 
//		  } 
//	  if (!((i ==1) || (i==2))) 
//		  System.out.println("PICK ONLY 1 or 2!!!"); 
//	  } while (!((i ==1) ||	  (i==2)));
//	  
//	  setTurn(1); //Player 1 goes first.
//	  
//	  while (!(isGameEnd())) { 
//		  draw(); 
//		  if (turn == PLAYER1) { 
//			  moveid =	  requestMove(); 
//			  } 
//		  else { 
//			  Node evalNode = new Node(this, 0); 
//			  moveid =  findBestMove(evalNode); 
//			  } 
//		  if (moveid != -1) {
//			  System.out.println("MOVING FROM " + moveid); 
//			  move(moveid); 
//			  }
//	  
//	  else break; 
//		  }
//	  
//	  setGameState(); 
//	  switch (getGameState()){ 
//	  case WIN_P1:
//		  System.out.println("PLAYER 1 WON"); 
//		  break; 
//	  case WIN_P2:
//		  System.out.println("PLAYER 2 WON"); 
//		  break; 
//	  case DRAW:
//		  System.out.println("DRAW"); 
//		  break; 
//	  default:
//		  System.out.println("Something is wrong!"); 
//		  break; } 
//	  }
	 

	public static int findBestMove(Node currentNode) {
		int best = -1;
		int turn = currentNode.getBoard().getTurn();
		int offset = turn - PLAYER1;

		if (turn == PLAYER1)
			currentNode.setValue(Integer.MAX_VALUE);
		else
			currentNode.setValue(Integer.MIN_VALUE);

		for (int i = (0 + offset); i < (MAX_SLOTS + offset); i++)
			if (currentNode.getBoard().isLegalMove(i)) {
				try {
					Board newBoard = new Board(currentNode.getBoard());
					newBoard.move(i);

					Node newNode = new Node(newBoard,
							currentNode.getDepth() + 1);

					newNode.setParent(currentNode);
					currentNode.setChild(newNode, i);

					// CUT OFF
					if (newNode.getBoard().isGameEnd()
							|| (newNode.getDepth() >= PLAYER2_CutOffDepth)) {

						newNode.setValue(newNode.getBoard().getSlotValue(
								PLAYER2)
								- newNode.getBoard().getSlotValue(PLAYER1));

					} else
						findBestMove(newNode);

					// alpha-beta pruning
					// PLAYER 2 is AI = MAX
					// pick the child with larger value
					if (currentNode.getBoard().getTurn() == PLAYER2) {
						if (currentNode.getChild(i) != null) {
							if (currentNode.getChild(i).getValue() > currentNode
									.getValue()) {
								currentNode.setValue(currentNode.getChild(i)
										.getValue());
								best = i;
							}
						}
						currentNode.deleteChild(i);

						// alpha cut off if our value is greater than ANY
						// player/MIN parent value

						Node nodePtr = currentNode;
						while (nodePtr.getParent() != null) {
							nodePtr = nodePtr.getParent();
							if ((nodePtr.getBoard().getTurn() == PLAYER1)
									&& (currentNode.getValue() > nodePtr
											.getValue())) {
								nodePtr = null;
								return best;
							}
						}

						nodePtr = null;
					}

					// PLAYER 1 is Player = MIN
					// pick the child with smaller value
					if (currentNode.getBoard().getTurn() == PLAYER1) {
						if (currentNode.getChild(i) != null) {
							if (currentNode.getChild(i).getValue() < currentNode
									.getValue()) {
								currentNode.setValue(currentNode.getChild(i)
										.getValue());
								best = i;
							}
						}
						currentNode.deleteChild(i);

						// beta cut off if our value is less than ANY
						// computer/MAX parent value
						Node nodePtr = currentNode;
						while (nodePtr.getParent() != null) {
							nodePtr = nodePtr.getParent();
							if ((nodePtr.getBoard().getTurn() == PLAYER2)
									&& (currentNode.getValue() < nodePtr
											.getValue())) {
								nodePtr = null;
								return best;
							}
						}
						nodePtr = null;
					}
				} catch (java.lang.OutOfMemoryError e) {
					System.out.println("OUT OF MEM");
					return -1;
				}
			}
		return best;
	}

	public static int BestMove(Node currentNode) {
		int best = -1;
		int turn = currentNode.getBoard().getTurn();
		int offset = turn - PLAYER1;

		if (turn == PLAYER1)
			currentNode.setValue(Integer.MIN_VALUE);
		else
			currentNode.setValue(Integer.MAX_VALUE);

		for (int i = (0 + offset); i < (MAX_SLOTS + offset); i++)
			if (currentNode.getBoard().isLegalMove(i)) {
				try {
					Board newBoard = new Board(currentNode.getBoard());
					newBoard.move(i);

					Node newNode = new Node(newBoard,
							currentNode.getDepth() + 1);

					newNode.setParent(currentNode);
					currentNode.setChild(newNode, i);

					// CUT OFF
					if (newNode.getBoard().isGameEnd()
							|| (newNode.getDepth() >= PLAYER1_CutOffDepth)) {
						newNode.setValue(newNode.getBoard().getSlotValue(
								PLAYER1)
								- newNode.getBoard().getSlotValue(PLAYER2));
					} else
						BestMove(newNode);

					// alpha-beta pruning
					// PLAYER 2 is Player = MIN
					// pick the child with smaller value
					if (currentNode.getBoard().getTurn() == PLAYER2) {
						if (currentNode.getChild(i) != null) {
							if (currentNode.getChild(i).getValue() < currentNode
									.getValue()) {
								currentNode.setValue(currentNode.getChild(i)
										.getValue());
								best = i;
							}
						}
						currentNode.deleteChild(i);

						// beta cut off if our value is less than ANY Player/MIN
						// parent value

						Node nodePtr = currentNode;
						while (nodePtr.getParent() != null) {
							nodePtr = nodePtr.getParent();
							if ((nodePtr.getBoard().getTurn() == PLAYER2)
									&& (currentNode.getValue() < nodePtr
											.getValue())) {
								nodePtr = null;
								return best;
							}
						}

						nodePtr = null;
					}

					// PLAYER 1 is AI = MAX
					// pick the child with larger value
					if (currentNode.getBoard().getTurn() == PLAYER1) {
						if (currentNode.getChild(i) != null) {
							if (currentNode.getChild(i).getValue() > currentNode
									.getValue()) {
								currentNode.setValue(currentNode.getChild(i)
										.getValue());
								best = i;
							}
						}
						currentNode.deleteChild(i);

						// alpha cut off if our value is greater than ANY
						// Player/MIN parent value
						Node nodePtr = currentNode;
						while (nodePtr.getParent() != null) {
							nodePtr = nodePtr.getParent();
							if ((nodePtr.getBoard().getTurn() == PLAYER1)
									&& (currentNode.getValue() > nodePtr
											.getValue())) {
								nodePtr = null;
								return best;
							}
						}
						nodePtr = null;
					}
				} catch (java.lang.OutOfMemoryError e) {
					System.out.println("OUT OF MEM");
					return -1;
				}
			}
		return best;
	}

}
