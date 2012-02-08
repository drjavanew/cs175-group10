import java.util.*;
import java.io.*;

public class LearningAI {

	private static Board MancalaBoard;
	private double[][] weight = { {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}}; //why so big?
	private int[] rewards = new int[2];
	
	private int winCountP1=0;
	private int winCountP2=0;
	
	private Vector[] GameHistory = new Vector[2]; 
	private int[] players = { Constants.PLAYER1, Constants.PLAYER2 };
	
	/* Constructor 1
	 * 
	 */
	public LearningAI() {
		MancalaBoard = new Board();
		GameHistory[0] = new Vector<State>(); //list of states.
		GameHistory[1] = new Vector<State>();
		runTest();

	}

	/* Constructor 2
	 * 
	 * This constructor takes in a filename and reads in the weight values of a
	 * previous record.
	 */
	public LearningAI(String filename) {
		MancalaBoard = new Board();
		GameHistory[0] = new Vector<State>();
		GameHistory[1] = new Vector<State>();
		
		
		//read and set weights of each feature from a file.
		try {
			String sCurrentLine;
		 
			BufferedReader br = new BufferedReader(new FileReader(filename));
			int i=0;
			int j=0;
			while ((sCurrentLine = br.readLine()) != null) {
				weight[i][j] =	Double.parseDouble(sCurrentLine);
				j++;
				if (j == weight[i].length) {
					i ++;
					j = 0;
				}				
			}			
			br.close();	    
		}   
			 
		catch (IOException x) {
				runTest();
		}
	}
	
	
	/* Reset the game */
	public void reset () {
		MancalaBoard = new Board();
		GameHistory[0] = new Vector<State>();
		GameHistory[1] = new Vector<State>();
	}
	
	
	/*
	 *  Randomly selects a slot to move.
	 *  currentPlayer: a number 0 or 7 depending on who's turn it is. 
	 */
	public int requestMoveRandom() {
		boolean inputOK = false;
		int i = 0;
		int currentPlayer = MancalaBoard.getTurn() - Constants.PLAYER1;
		do {
			Random r = new Random();
			int randomInt = r.nextInt(6);

			i = randomInt + currentPlayer;
			inputOK = MancalaBoard.isLegalMove(i);

		} while (!inputOK);

		return i;
	}

	public void learn() {

		int moveid = 0;
		
		
		while (!(MancalaBoard.isGameEnd())) {
//			MancalaBoard.draw();
			int player = MancalaBoard.getTurn(); 
			Node evalNode = new Node(MancalaBoard.get(), 0);
			if (MancalaBoard.getTurn() == Constants.PLAYER2)
				 moveid = requestMoveRandom();
//				moveid = MancalaBoard.findBestMove(evalNode);
			else
				moveid = requestMoveRandom();
//				moveid = MancalaBoard.BestMove(evalNode);

			if (moveid != -1) {
//				System.out.println("PLAYER " + ((player % Constants.MAX_SLOTS)+1) + " MOVING FROM " + moveid);
				MancalaBoard.move(moveid);
				State  aState = new State(MancalaBoard);
				addGameHistory(player, aState);
			}

			else
				break;
		}
		int me = MancalaBoard.getTurn();
		State  aState = new State(MancalaBoard);
		addGameHistory(me, aState);
		addGameHistory(Constants.inverse(me),aState);
		MancalaBoard.setGameState();
		switch (MancalaBoard.getGameState()) {
		case Board.WIN_P1:
//			System.out.println("PLAYER 1 WON");
			rewards[0] = 1;
			rewards[1] = -1;
			winCountP1 ++;
			break;
		case Board.WIN_P2:
//			System.out.println("PLAYER 2 WON");
			rewards[0] = -1;
			rewards[1] = 1;
			winCountP2 ++;
			break;
		case Board.DRAW:
//			System.out.println("DRAW");
			rewards[0] = 0;
			rewards[1] = 0;
			break;
		default:
			System.out.println("Something is wrong!");
			break;
		}
		
	}

	public void copyArr (double[] original, double[] copy) {
		for (int i = 0; i<original.length; i++) {
			copy[i] = original[i]; 
		}
	}
	
	
	public double calStateValue (double[] thetas, State aState, int player) {
		double sum = 0;
		sum = thetas[0]*1;
		sum += thetas[1]* aState.getFeature1(player);
		sum += thetas[2]* aState.getFeature2(player);
		sum += thetas[3]* aState.getFeature3(player);
		sum += thetas[4]* aState.getFeature4(player);
		sum += thetas[5]* aState.getFeature5(player);
		sum += thetas[6]* aState.getFeature6(player);
		
		return sum;
	}
	
	
	public void learnThetas (double learning_rate, double[] thetasValue, Vector<State> history, int player, int reward) {

		double[] copy = new double[thetasValue.length];
		copyArr(thetasValue,copy);
		
		int i = 0;
		while (i<7) {
			
			double sumAll = 0;
			double sumTotal = 0;
			
			Iterator<State> it = history.iterator();	
			while (it.hasNext()) {
				State aState = it.next();
				sumAll = calStateValue (copy, aState, player);
				sumAll -= reward;

				switch (i) {
				case 0:
					sumTotal = sumAll * 1;
					break;
				case 1:
					sumTotal = sumAll * aState.getFeature1(player);
					break;
				case 2:
					sumTotal = sumAll * aState.getFeature2(player);
					break;
				case 3:
					sumTotal = sumAll * aState.getFeature3(player);
					break;
				case 4:
					sumTotal = sumAll * aState.getFeature4(player);
					break;
				case 5:
					sumTotal = sumAll * aState.getFeature5(player);
					break;
				case 6:
					sumTotal = sumAll * aState.getFeature6(player);
					break;
				} //end switch
			} //end inner while
		
			thetasValue[i] = copy[i] - learning_rate*((double) 1/history.size())* sumTotal;
			i++;
		}
		
	}
	
	
	
	
	
	public void addGameHistory (int player, State aState) {
		
	/*	if (player == Constants.PLAYER1) {
			GameHistory1.add(aState);
		}
		else {
			GameHistory2.add(aState);
		}*/
		GameHistory[player % Constants.MAX_SLOTS].add(aState);
	}
	
	private int newPositive (int limit) {
		int created;
		Random randomGenerator = new Random();
		do {
			created = randomGenerator.nextInt(limit);
		} while (created >0);
		
		return created;
	}
	
	public void runTest () {
		
		for (int i = 1; i <= 1000; i++) {
//			MancalaBoard.PLAYER1_CutOffDepth = newPositive(10);
//			MancalaBoard.PLAYER2_CutOffDepth = newPositive(10);
			MancalaBoard.setTurn(1); // Player 1 first.
			learn();
			for (int j=0; j<=1; j++) {
			learnThetas((double) 1/(50*i),weight[j],GameHistory[j],players[j],rewards[j]);
			}
			reset();
		}
		
		for (int k = 1; k <= 1000; k++) {
//			MancalaBoard.PLAYER1_CutOffDepth = newPositive(10);
//			MancalaBoard.PLAYER2_CutOffDepth = newPositive(10);
			MancalaBoard.setTurn(2); // Player 2 first.
			learn();
			for (int j=0; j<=1; j++) {
				learnThetas((double) 1/(50*k),weight[j],GameHistory[j],players[j],rewards[j]);
				}
			reset();
		}
		
		System.out.println("THETA1");
		printThetas(weight[0]);
		
		System.out.println();
		System.out.println("THETA2");
		printThetas(weight[1]);
	}
	
	
	
	public void printThetas(double[] myValues) {
		for (int i = 0; i < myValues.length; i++) {
			System.out.println (myValues[i]);
			
		}
		System.out.printf ("\n");
		System.out.println("W1 = " + winCountP1);
		System.out.println("W2 = " + winCountP2);
	}
	
	public int requestMove_AI(int player) {
		int offset = player - Constants.PLAYER1;
		double max = Float.NEGATIVE_INFINITY;
		int best = -1;
		
		for (int i = (0 + offset); i < (Constants.MAX_SLOTS + offset); i++)
			if (MancalaBoard.isLegalMove(i)) {
				Board newBoard = new Board(MancalaBoard);
				newBoard.move(i);
				State newState = new State(newBoard);
				double stateValue = calStateValue(weight[player%Constants.PLAYER1],newState,player);
				if (stateValue > max) {
					max = stateValue;
					best = i;
				}
			}
		 
		return best;
		}
	
	public void AIplay () {
		int moveid = 0;
		MancalaBoard.setTurn(1);
		while (!(MancalaBoard.isGameEnd())) {
			MancalaBoard.draw();
			int player = MancalaBoard.getTurn(); 
			Node evalNode = new Node(MancalaBoard.get(), 0);
			if (player == Constants.PLAYER2)
				moveid = requestMove_AI(player);
//				moveid = MancalaBoard.findBestMove(evalNode);
			else
				moveid = requestMoveRandom();
//				moveid = MancalaBoard.BestMove(evalNode);
//				moveid = MancalaBoard.requestMove();

			//add each move into the history.
			if (moveid != -1) {
				System.out.println("PLAYER " + ((player % Constants.MAX_SLOTS)+1) + " MOVING FROM " + moveid);
				MancalaBoard.move(moveid);
				State  aState = new State(MancalaBoard);
				addGameHistory(player, aState);
			}

			else
				break;
		}
		
		
		int me = MancalaBoard.getTurn();
		State  aState = new State(MancalaBoard);
		addGameHistory(me, aState);
		addGameHistory(Constants.inverse(me),aState);
		MancalaBoard.setGameState();
		switch (MancalaBoard.getGameState()) {
		case Board.WIN_P1:
			System.out.println("PLAYER 1 WON");
			rewards[0] = 1;
			rewards[1] = -1;
			winCountP1 ++;
			break;
		case Board.WIN_P2:
			System.out.println("PLAYER 2 WON");
			rewards[0] = -1;
			rewards[1] = 1;
			winCountP2 ++;
			break;
		case Board.DRAW:
			System.out.println("DRAW");
			rewards[0] = 0;
			rewards[1] = 0;
			break;
		default:
			System.out.println("Something is wrong!");
			break;
		}

	}
	
	
	public void saveThetas(String filename) {
		try {
			
			String s;
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			for (int  i=0; i <=1; i++ )
				for (int j=0; j < weight[i].length; j++) {
					s = String.valueOf(weight[i][j]);
					s = s + "\n";
					bw.write(s);
			}
			
			bw.close();
			    
		}   
			 
		catch (IOException e) {
			    System.err.println(e);
		}
	}
	public static void main(String[] args) {
		LearningAI myAI = new LearningAI("data.txt");
//		myAI.saveThetas("data.txt");
		myAI.AIplay();
	}

}
