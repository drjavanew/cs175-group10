/*
 * author: Andrew Furusawa
 * 
 * class name: RegressionLearning
 * description: 
 */

import java.util.ArrayList;
import java.util.Vector;




//TODO: MERGE WITH PLAYER CLASS.




public class RegressionLearning {

	/* CONSTANTS */
	private static final int TOTAL_FEATURES = 6+1;
	
	int player;
	
	/*
	 * Game History contains each state of a turn within a single game.
	 */
	ArrayList<RegressionState> gameHistory = new ArrayList<RegressionState>();
	
	RegressionState state;
	
	/* other */
	public double[] weight = new double[TOTAL_FEATURES]; //a.k.a. theta value.

	MancalaGameState board;
	int gamesPlayed = 0;
	
	
	public RegressionLearning() {
		
	}
	
	/* Constructor */
	public RegressionLearning(MancalaGameState board, int player) {
		board = this.board;
		player = this.player;
	
		//initialize with 0 weight value.
		for(int i = 0; i < weight.length; i++) {
			weight[i] = 0;
		}
		
		state = new RegressionState(board, player);
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
	
	/* calculate the gradient descent. */
	public int gradientDescent(double stepsize, double[] weight, Vector<State> history, int player, int resultValue) {
		return 1;
	}
	

	public void updateHistory(RegressionState state) {
		gameHistory.add(state);
	}
	
	
	//Accessors
	public double[] getWeight() {
		return weight;
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