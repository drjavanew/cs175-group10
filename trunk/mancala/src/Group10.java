/*
 * author: Khanh Nguyen
 * 
 * class name: Linear Regression using Normal Equation
 * description: Normal Equation is:
 * Weight = inverse(Phi*PhiTranspose) * Phi * Rewards
 * All are matrices
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;


public class Group10 {

	/* CONSTANTS */
	private static final int TOTAL_FEATURES = 8+1; //# of features + 1 constant.
	
	private int player;
	private int reward;
	private boolean inputFail;

	
	
	/*
	 * Game History contains each state of a turn within a single game.
	 */
	private ArrayList<LeastSquareState> gameHistory = new ArrayList<LeastSquareState>();
	
	/* other */
	public double[] weight = new double[TOTAL_FEATURES]; //a.k.a. list of theta value.
	
	
	/* Matrix phi represents the total history of all trained data:
	 * Each row represents one feature
	 * Each column represents one data point - one single state
	 * */
	private int[][] phi; 
	
	/* Rewards stores the final score of the game, since we associate the final score
	 * to each state after the game is done, there will be duplicated a lot
	 * Rewards essentially is a Mx1 matrix where M is total states of all games trained*/
	private int[][] rewards;
	
	private Matrix util = new Matrix();

	
	/* Constructor 
	 * 
	 * This constructor takes in a filename and reads in the weight values of a
	 * previous record.
	 */
	
	
	public Group10(int playerNum, String filename) {
		this.player = playerNum;
		
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
		
			inputFail = false;
			// load the database of all trained data
			phi = loadData("DatabaseK.txt");
			if (inputFail == false) {
				phi = util.transpose(phi);
			}
			
			inputFail = false;
			// load the rewards of all trained data
			int[][] table = loadData("RewardsK.txt");
			if (inputFail == false) {
				rewards = table;
			}
			else {
				rewards = new int[0][0];
			}

		}
	
	
	public boolean isNew() {
		if ((gameHistory.size() == 1) || (gameHistory.size() == 2))  {
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public void cleanup() {
		
			saveData(util.transpose(phi),"DatabaseK.txt");
			saveData(rewards,"RewardsK.txt");
			saveThetas("Kdata.txt");
		
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
		LeastSquareState aState = new LeastSquareState(gs.copy(),player);
		gameHistory.add(aState);
	}
	
	/*
	 * LINEAR REGRESSION METHODS USING MATRIX AND LEAST SQUARE.
	 */
	private void createReward (int length){
	// startPos is old length	
		int startPos = rewards.length;
		rewards = util.resize(rewards,startPos+length,1);
		for (int i = startPos; i <rewards.length; i++) {
			rewards[i][0] = reward;
		}
		return ;
		
	}
	
	public void learnWeights() {
		int historyLength = gameHistory.size();
		int row = phi.length;
		
		int count = phi[0].length;
		
		
		phi = util.resize(phi, row, count+historyLength);
		
		int value = 0;
		Iterator<LeastSquareState> it = gameHistory.iterator();	
		while (it.hasNext()) {
			LeastSquareState aState = it.next();
			for(int i =0; i <TOTAL_FEATURES; i++) {
				value = aState.getFeature(i);
				phi[i][count] = value;
			}
			count++;
		} //end inner while
		
		
		
		
		int[][] phiTrans = util.transpose(phi);
		
		int[][] mul = util.multiply(phi, phiTrans);
		
		// ridge regression: to stablize the matrix to be inverted. i.e:
		// get smaller condition number on the matrix
		// Instead of inv(phi*phiTranspose), do
		// inv(phi*phiTranspose - lambda*I), where I is the identity matrix,
		// (size TOTAL_FEATURESxTOTALFEATURES) in our case 
		// and lambda is a parameter -- try 1 for now.
		
		int[][] identity = util.createIdentityInt(TOTAL_FEATURES);
		
		int lamda = 1;
		
		identity = util.mulConst(identity, lamda);
		
		double[][] insideMatrix = util.subtract(mul, identity);
		
		double[][] lhs = util.invert(insideMatrix);
		
		createReward(historyLength);
		
		int[][] rhs = util.multiply(phi, rewards);
		
		double[][] newWeight = util.multiply(lhs, rhs);
		

		for (int i = 0; i <TOTAL_FEATURES; i++) {
			weight[i] = newWeight[i][0];
		}
		         
	}
	
	public double[] getWeight() {
		return weight;
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
			    System.err.println("Normal Equation - saveThetas");
		}
	}
	
	public void saveData (int[][] data, String filename) {
		   try {

			 String s;
			 BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

			 int row = data.length;
			 int col = data[0].length;
			 
			 bw.write(String.valueOf(row) + "\t" + String.valueOf(col)+ "\n" );

			 for (int i = 0; i < row; i++ ) {

			 for (int j = 0; j < col; j++) {
				   s = String.valueOf(data[i][j])+ "\t";
				   bw.write(s);
				 }
		         bw.write("\n");
			 } 
				 bw.close();
		   } // end of try

			       catch (IOException e) {
				 System.err.println("Gradient Descent - saveData");
			       }


		}

	public int[][] loadData (String filename) {

		   
		try {
		  String sCurrentLine;

		  BufferedReader br = new BufferedReader(new FileReader(filename));
		  sCurrentLine = br.readLine();
		  String[] row;
		  row = sCurrentLine.trim().split("\\t");
		  int[][] states = new int[Integer.parseInt(row[0])][Integer.parseInt(row[1])];
		  
		  System.out.println("Reading all data trained from " + filename );
		  int i=0;
		  while ((sCurrentLine = br.readLine()) != null) {
		    // split the line based on "\t"
		    row = sCurrentLine.trim().split("\\t");
		    for (int j = 0; j < row.length; j++)  
		        states[i][j] =  Integer.parseInt(row[j]);
		    i++;
		  }
		  br.close();
		  return states;
		}

		// if file not found, init the table
		catch (IOException x) {
			inputFail = true;
			int[][] table = new int[TOTAL_FEATURES][0];
			return table;
		}
	}


	public double getStateValue (MancalaGameState gameState, int player) {
		LeastSquareState aState = new LeastSquareState(gameState,player);
		double sum = weight[0] * 1;
		for(int i = 1; i < TOTAL_FEATURES; i++) {
			sum += weight[i] * aState.getFeature(i);
		}
		return sum;
		
	}


	
}

