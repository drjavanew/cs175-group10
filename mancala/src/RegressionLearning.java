/*
 * author: Andrew Furusawa
 * 
 * class name: RegressionLearning
 * description: 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;


public class RegressionLearning {

	/* CONSTANTS */
	private static final int TOTAL_FEATURES = 7+1; //# of features + 1 constant.
	
	int player;
	int reward;
	
	int preGame;
	int nowGame;
	
	// these are used to break out of looping looses
	boolean signal = false; 
	boolean firstSol = false;
	boolean secondSol = false;
	
	int cutoffDepth; //search depth
	/*
	 * Game History contains each state of a turn within a single game.
	 */
	ArrayList<RegressionState> gameHistory = new ArrayList<RegressionState>();
	
	// these are to verify the cost function and adjust learning rate 
	ArrayList<RegressionState> sampleHistory = new ArrayList<RegressionState>();
	int sampleReward;
	
	// might need these for checking difference type of games later
	ArrayList<RegressionState> firstDraw = new ArrayList<RegressionState>();
	ArrayList<RegressionState> firstLoose = new ArrayList<RegressionState>();
	ArrayList<RegressionState> firstWin = new ArrayList<RegressionState>();
	int rWin;
	int rLoose;
	int rDraw;
	
	/* other */
	public double[] weight = new double[TOTAL_FEATURES]; //a.k.a. list of theta value.
	double[] copy = new double[TOTAL_FEATURES]; // value of weight before learning new weight
	
	
	double[] recentWin = new double[TOTAL_FEATURES]; // the most recent value of weight that leads to a win
	
	MancalaGameState board;
	int gamesPlayed = 0;
	int instance_gamesPlayed = 0;
	
	
	
	/* Constructor 
	 * 
	 * This constructor takes in a filename and reads in the weight values of a
	 * previous record.
	 */
	
	
	public RegressionLearning(int playerNum, String filename) {
		this.player = playerNum;
		cutoffDepth = 9;
			//read and set weights of each feature from a file.
			try {
				String sCurrentLine;
			 
				BufferedReader br = new BufferedReader(new FileReader(filename));
				System.out.println("Reading theta from " + filename );
				int i=0;
				String s = br.readLine();
				gamesPlayed = Integer.parseInt(s);
				System.out.println(gamesPlayed);
				while ((sCurrentLine = br.readLine()) != null) {
					weight[i] =	Double.parseDouble(sCurrentLine);
					i++;
				}			
				br.close();	    
			}   
			
			//if file not found, create new theta values at 0.	 
			catch (IOException x) {
				System.out.println("New thetas");
				for(int i = 0; i < weight.length; i++) {
					weight[i] = 0;
				}
			}
		}
	
	/* Sets the reward value to win or loss. */
	public void setReward(int value) {
		reward = value;
		nowGame = reward;
	}
	
	/* Increment the number of games played by one. */
	public void incGamesPlayed() {
		gamesPlayed ++;
		instance_gamesPlayed ++;
	}

	/* Get number total of games played. */
	public int getGamesPlayed () {
		return gamesPlayed;
	}
	
	/* Reset the current game. */
	public void reset() {
		preGame = reward;
		gameHistory.clear();
	}
	
	/*
	 * LINEAR REGRESSION METHODS.
	 */
	
	/* Returns the sum of each weight * value of each feature. */
	public double predictedValue(RegressionState state, double[] weight) {
		double sum = weight[0] * 1;
		for(int i = 1; i < TOTAL_FEATURES; i++) {
			sum += weight[i] * state.getFeature(i);
		}
		
		return sum;
	}
	
	/* Save a copy of the current game history. */
	@SuppressWarnings("unchecked")
	public void saveSample() {
		if (reward >0) {
			if (firstWin.isEmpty()) {
				firstWin = (ArrayList<RegressionState>) gameHistory.clone();
				rWin = reward; 
			}
		}
		else if (reward < 0) {
			if (firstLoose.isEmpty()) {
				firstLoose = (ArrayList<RegressionState>) gameHistory.clone();
				rLoose = reward;
			}
		}
		else {
			if (firstDraw.isEmpty()) {
				firstDraw = (ArrayList<RegressionState>) gameHistory.clone();
				rDraw = reward;
			}
		}
			
	}
	
	public boolean hasNoSample() {
		return firstWin.isEmpty() || firstLoose.isEmpty() || firstDraw.isEmpty();
	}
	
	
	public double costFunction(ArrayList<RegressionState> sample, double[] weightValue, double reward) {
		double sumAll =0;
		double total  = 0;
		Iterator<RegressionState> it = sample.iterator();	
		while (it.hasNext()) {
			RegressionState aState = it.next();
			sumAll = predictedValue (aState, weightValue);
			sumAll -= reward;
			total += sumAll*sumAll;
		} //end while
		return total;
	}
	
	
	
      /* Prints out the value of the cost function. */
	public void checkEvalFunction(){
		
		double preCostValue;
		double nowCostValue;
		double currentPreCostValue = costFunction(gameHistory,copy,reward);
		double currentNowCostValue = costFunction(gameHistory,weight,reward);
		if (reward >0) {
		nowCostValue = costFunction(firstWin,weight,rWin);
		preCostValue = costFunction(firstWin,copy,rWin);
		}
		else if (reward <0) {
			nowCostValue = costFunction(firstLoose,weight,rLoose);
			preCostValue = costFunction(firstLoose,copy,rLoose);
		}
		else {
			nowCostValue = costFunction(firstDraw,weight,rDraw);
			preCostValue = costFunction(firstDraw,copy,rDraw);
		}
//		System.out.println();
//		System.out.println("Current game value with copy    = " + costFunction(gameHistory,copy,reward));
//		System.out.println("Current game value with learnt  = " + costFunction(gameHistory,weight,reward));
//		System.out.println("Pre Value= " + preCostValue);
//		System.out.println("Now Value= " + nowCostValue);
//		System.out.println("Pre Game= " + preGame);
//		System.out.println("Now Gamee= " + nowGame);
		
		if ((preGame == 0 ) || (nowGame ==0)) {
			return;
		}
		
		if (preGame < 0) {
			if (nowGame > 0) {
				copyArr(copy,recentWin);
			}
			else {
				if (preGame == nowGame) { // loose after loose by same margin
					if (signal == false) {
						firstLoose.clear();
						saveSample(); // save current game as sample of first Loose
						signal = true;
						return;
					}
					else {
						if ((preCostValue == currentPreCostValue) && 
								(nowCostValue == currentNowCostValue)) { // same game detected 
							if (firstSol == false) {
								gamesPlayed = 1;
								firstSol = true;
								return;
							}
							else {
								copyArr(recentWin,weight);
								gamesPlayed *= 2;
								if (secondSol== false) {
									secondSol = true;
									return;}
								
								else {
							 //reset everything - re-learn from beginning
								gamesPlayed = 0;
								weight = new double[TOTAL_FEATURES];
								signal = false;
								firstSol = false;
								secondSol = false;
							}
						}
					}
					
				}
			}
		}
		
	}
		}
	
	public void printThetas(double[] myValues) {
		for (int i = 0; i < myValues.length; i++) {
			System.out.println (myValues[i]); //theta values.
			
		}
		System.out.printf ("\n");
	}
	
	/* Write the theta values to the data file. */
	public void saveThetas(String filename) {
		try {
			
			String s;
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			bw.write(String.valueOf(gamesPlayed)+ "\n");
			for (int j=0; j < weight.length; j++) {
					s = String.valueOf(weight[j])+ "\n";
					bw.write(s);
			}
			bw.close();
		}   
			 
		catch (IOException e) {
			    System.err.println("RegressionLearning - saveThetas");
		}
	}
	
	public void copyArr (double[] originalArr, double[] copyArr) {
		for (int i = 0; i < originalArr.length; i++) {
			copyArr[i] = originalArr[i]; 
		}
	}
	
	/* calculate the gradient descent. */
	public void gradientDescent(double stepsize) {
		
		copyArr(weight,copy);
		
		int i = 0;
		while (i<TOTAL_FEATURES) {
			
			double sumAll = 0;
			double sumTotal = 0;
			
			Iterator<RegressionState> it = gameHistory.iterator();	
			while (it.hasNext()) {
				RegressionState aState = it.next();
				sumAll = predictedValue (aState, copy);
				sumAll -= reward;

				sumTotal += sumAll * aState.getFeature(i);
			} //end inner while
			
			weight[i] = copy[i] - stepsize*((double) 1/gameHistory.size())* sumTotal;
			i++;
		}// end while
		
	}
	

	public void updateHistory(RegressionState state) {
		gameHistory.add(state);
	}
	
	/* Gets the list of weights */
	public double[] getWeight() {
		return weight;
	}
	
	
	public int findBestMove(Node currentNode, int best) { 
        int turn = currentNode.getBoard().CurrentPlayer();
	
	// set the current value of the current node to compare with children node
        if (turn != player)
                currentNode.setValue(Float.POSITIVE_INFINITY);
        else
                currentNode.setValue(Float.NEGATIVE_INFINITY);

	//Check each pit on your side to find the best move. */
        for (int i = 0; i < 6 ; i++)
                if (currentNode.getBoard().validMove(i)) {
                        try {
                                MancalaGameState newBoard = currentNode.getBoard().copy() ;
                                try {
									newBoard.play(i);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

                                Node newNode = new Node(newBoard,
                                                currentNode.getDepth() + 1);

                                newNode.setParent(currentNode);
                                currentNode.setChild(newNode, i);

                                // CUT OFF
                                if (newNode.getBoard().checkEndGame()
                                                || (newNode.getDepth() >= cutoffDepth)) {

                                		RegressionState aState = new RegressionState(newNode.getBoard(),player);
                                        newNode.setValue(predictedValue(aState,weight));

                                } else
                                        findBestMove(newNode,best);

                                // alpha-beta pruning
                                // AI = MAX
                                // pick the child with larger value
                                if (currentNode.getBoard().CurrentPlayer() == player) {
                                        if (currentNode.getChild(i) != null) {
                                                if (currentNode.getChild(i).getValue() > currentNode.getValue()) {
                                                        currentNode.setValue(currentNode.getChild(i).getValue());
                                                        best = i;
                                                }
                                        }
                                        currentNode.deleteChild(i);

                                        // alpha cut off if our value is greater than ANY
                                        // player/MIN parent value

                                        Node nodePtr = currentNode;
                                        while (nodePtr.getParent() != null) {
                                                nodePtr = nodePtr.getParent();
                                                if ((nodePtr.getBoard().CurrentPlayer != player)
                                                                && (currentNode.getValue() > nodePtr.getValue())) {
                                                        nodePtr = null;
                                                        return best;
                                                }
                                        }

                                        nodePtr = null;
                                }

                                // Player = MIN
                                // pick the child with smaller value
                                if (currentNode.getBoard().CurrentPlayer() != player) {
                                        if (currentNode.getChild(i) != null) {
                                                if (currentNode.getChild(i).getValue() < currentNode.getValue()) {
                                                        currentNode.setValue(currentNode.getChild(i).getValue());
                                                        best = i;
                                                }
                                        }
                                        currentNode.deleteChild(i);

                                        // beta cut off if our value is less than ANY
                                        // computer/MAX parent value
                                        Node nodePtr = currentNode;
                                        while (nodePtr.getParent() != null) {
                                                nodePtr = nodePtr.getParent();
                                                if ((nodePtr.getBoard().CurrentPlayer() == player)
                                                                && (currentNode.getValue() < nodePtr.getValue())) {
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
        return best; //return the best move
}

	
}

//predicted value for a given turn.
//pval()
//========
//sum = 0
//for i=0 to <total features>
//-> sum += weight[i]
//return sum
//
//
//
//error in winning/losing - condition obtained at the end of the game.
//error(win | lose)
//========
//eval(lose) = -1
//eval(win) = 1
//
//sum = 0
//for i=0 to <your total turns>
//sum += (pval() - eval(win | lose))^2
//return sum
//
//
//gradient descent
//========
//n = game count
//for int i=0 to <your total turns>
//weight[i] = weight[i] x (1/n) x (2)(pval() - eval(win | lose)) x eval(win | lose)
//
//
//*2 = derivative from (...)^2