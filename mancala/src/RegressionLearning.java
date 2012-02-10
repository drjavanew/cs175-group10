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




//TODO: MERGE WITH PLAYER CLASS.




public class RegressionLearning {

	/* CONSTANTS */
	private static final int TOTAL_FEATURES = 6+1;
	
	int player;
	static int reward;
	static int cutoffDepth;
	/*
	 * Game History contains each state of a turn within a single game.
	 */
	ArrayList<RegressionState> gameHistory = new ArrayList<RegressionState>();
	
	
	
	/* other */
	public double[] weight = new double[TOTAL_FEATURES]; //a.k.a. theta value.

	MancalaGameState board;
	int gamesPlayed = 0;
	
	
	
	/* Constructor 
	 * 
	 * This constructor takes in a filename and reads in the weight values of a
	 * previous record.
	 */
	
	
	public RegressionLearning(int playerNum, String filename) {
		this.player = playerNum;
		cutoffDepth = 10;
			//read and set weights of each feature from a file.
			try {
				String sCurrentLine;
			 
				BufferedReader br = new BufferedReader(new FileReader(filename));
				int i=0;
				
				while ((sCurrentLine = br.readLine()) != null) {
					weight[i] =	Double.parseDouble(sCurrentLine);
					i++;
				}			
				br.close();	    
			}   
				 
			catch (IOException x) {
				for(int i = 0; i < weight.length; i++) {
					weight[i] = 0;
				}
//					runTest();
			}
		}
	
	
	public void setReward(int value) {
		reward = value;
	}
	
	public void incGamesPlayed() {
		gamesPlayed ++;
	}
	public int getGamesPlayed () {
		return gamesPlayed;
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
	
	
	/* Returns the square of error value. */
	public int errorValue(int result) {
		return 1;
	}
	
	public void printThetas(double[] myValues) {
		for (int i = 0; i < myValues.length; i++) {
			System.out.println (myValues[i]);
			
		}
		System.out.printf ("\n");
	}
	
	public void saveThetas(String filename) {
		try {
			
			String s;
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
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
	
	public void copyArr (double[] original, double[] copy) {
		for (int i = 0; i < original.length; i++) {
			copy[i] = original[i]; 
		}
	}
	
	
	
	/* calculate the gradient descent. */
	public void gradientDescent(double stepsize) {
		
		double[] copy = new double[TOTAL_FEATURES];
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
		}
		gameHistory.clear();
	}
	

	public double evalFunction (RegressionState state) {
		double value = weight[0];
		for (int i = 1; i< TOTAL_FEATURES; i++) {
			value += weight[i] * state.getFeature(i);
		}
		return value;
			
	}
	public void updateHistory(RegressionState state) {
		gameHistory.add(state);
	}
	
	
	public double[] getWeight() {
		return weight;
	}
	
	
	public int findBestMove(Node currentNode, int best) { 
        int turn = currentNode.getBoard().CurrentPlayer();

        if (turn != player)
                currentNode.setValue(Float.POSITIVE_INFINITY);
        else
                currentNode.setValue(Float.NEGATIVE_INFINITY);

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

                                		RegressionState aState = new RegressionState(newNode.getBoard(),newNode.getBoard().CurrentPlayer());
                                        newNode.setValue(evalFunction(aState));

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
        return best;
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