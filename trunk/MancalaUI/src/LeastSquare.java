/*
 * author: 
 * 
 * class name: Linear Regression using Least Square Equation
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


public class LeastSquare {

	/* CONSTANTS */
	private static final int TOTAL_FEATURES = 7+1; //# of features + 1 constant.
	
	int player;
	int reward;
	
	int cutoffDepth; //search depth
	/*
	 * Game History contains each state of a turn within a single game.
	 */
	ArrayList<RegressionState> gameHistory = new ArrayList<RegressionState>();
	
	/* other */
	public double[] weight = new double[TOTAL_FEATURES]; //a.k.a. list of theta value.
	
	MancalaGameState board;

	
	/* Constructor 
	 * 
	 * This constructor takes in a filename and reads in the weight values of a
	 * previous record.
	 */
	
	
	public LeastSquare(int playerNum, String filename) {
		this.player = playerNum;
		cutoffDepth = 9;
			//read and set weights of each feature from a file.
			try {
				String sCurrentLine;
			 
				BufferedReader br = new BufferedReader(new FileReader(filename));
				System.out.println("Reading theta from " + filename );
				int i=0;
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
	}
	
	/* Reset the current game. */
	public void reset() {
		gameHistory.clear();
	}
	
	public void updateHistory(MancalaGameState gs) {
		RegressionState aState = new RegressionState(gs.copy(),player);
		gameHistory.add(aState);
	}
	
	/*
	 * LINEAR REGRESSION METHODS USING MATRIX AND LEAST SQUARE.
	 */
	private double[][] createReward (int length){
		double[][] result = new double[length][1];
		
		for (int i = 0; i <length; i++) {
			result[i][0] = reward;
		}
		return result;
	}
	
	public void learnWeights() {
		int historyLength = gameHistory.size();
		double[][] phi = new double[TOTAL_FEATURES][historyLength];
		
		int count = 0;
		Iterator<RegressionState> it = gameHistory.iterator();	
		while (it.hasNext()) {
			RegressionState aState = it.next();
			for(int i =0; i <TOTAL_FEATURES; i++) {
				double value = aState.getFeature(i);
				phi[i][count] = value;
			}
			count++;
		} //end inner while
		
		
		Matrix util = new Matrix();
		
		double[][] phiTrans = util.transpose(phi);
		
		double[][] mul = util.multiply(phi, phiTrans);
		
		double[][] lhs = util.invert(mul);
		
		double[][] rewards = createReward(historyLength);
		
		double[][] rhs = util.multiply(phi, rewards);
		
		double[][] newWeight = util.multiply(lhs, rhs);
		
		
		for (int i = 0; i <TOTAL_FEATURES; i++) {
			weight[i] = newWeight[i][0];
		}
		         
	}
	
	/* Returns the sum of each weight * value of each feature. */
	public double predictedValue(RegressionState state, double[] weight) {
		double sum = weight[0] * 1;
		for(int i = 1; i < TOTAL_FEATURES; i++) {
			sum += weight[i] * state.getFeature(i);
		}
		
		return sum;
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
			for (int j=0; j < weight.length; j++) {
					s = String.valueOf(weight[j])+ "\n";
					bw.write(s);
			}
			bw.close();
		}   
			 
		catch (IOException e) {
			    System.err.println("Least Square - saveThetas");
		}
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

