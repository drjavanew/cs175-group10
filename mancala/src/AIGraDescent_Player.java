import java.util.Random;

/*
 * 
 * 
 * class name: AI Version 3 using gradient descent with matrix manipulation  
 * description: 
 */

public class AIGraDescent_Player implements MancalaPlayer {
	
	private int player;
	private int opponent;
	private double learning_rate;
	boolean firstMoveofFirstPlayer =false;
	
	private GradientDescent ai;

	public AIGraDescent_Player (int playerNum) {
	  player = playerNum;
	  opponent = 1 - player;
	  learning_rate = 0.003;
	  
	  // initialize ai and load theta values.
	  ai = new GradientDescent(playerNum,"GDdata.txt");
	 
	  
	}
	/* This function's logic is based on the Mancala game analysis located here:
     * http://fritzdooley.com/mancala/6_mancala_best_opening_move.html
     * 
     * But desired functionality may be changed to personal tastes
     */
    boolean[] DesiredFirstMove = { false /* 0 */, false /* 1 */, true /* 2 */,
                                                             false /* 3 */, false /* 4 */, true /* 5 */ };
    boolean[] DesiredBonusMove = { false /* 0 */, false /* 1 */, false /* WILL NEVER BE CALLED */,
                     true /* 3 */, true /* 4 */, true /* 5 */ };
    
    public boolean usePieMove(MancalaGameState gs)
    {
            boolean takePieMove = false;
            if(gs.validMove(KalahPieGameState.PIE_MOVE))
            {
                    //Determine our test set. If they got a bonus move, use the bonus set, otherwise use first move set.
                    boolean[] TestSet = ((DesiredFirstMove[2] && gs.stonesAt(opponent, 2) == 1) ? DesiredBonusMove : DesiredFirstMove);
                    
                    for(int i = 0; i < 6; i++)
                    {
                            if(TestSet[i] && gs.stonesAt(opponent, i) == 0)
                            {
                                    takePieMove = true;
                                    break;
                            }
                    }
                    
            }
            return takePieMove;
    }

	
		
		
		
		
		
		
	
	public int getMove(MancalaGameState gs) throws Exception {


//
//		ai.updateHistory(gs.copy());
//		if((player == 1) && (usePieMove(gs))) {
//            return -1;
//		}
//		
//		if ((player== 0) && (ai.isNew())) {
//			
//			if (firstMoveofFirstPlayer == false) {
//			    firstMoveofFirstPlayer = true;
//			    return 2;
//			}
//			else {
//				firstMoveofFirstPlayer =false;
//				Random rand = new Random();
//				int val = rand.nextInt(10);
//				if (val >=5) return 1;
//				else return 3;
//			}
//			 
//			
//		}
		
		int bestMove = -1;
		Node evalNode = new Node(gs.copy(), 0);
		bestMove = ai.findBestMove(evalNode, bestMove);
		ai.updateHistory(gs.copy().play(bestMove));
			 
		return bestMove;
	
	}
	
	
	/* This method will be used to learn from the game we just played. */
	public Object postGameActions(MancalaGameState gs) {
//		ai.printThetas(ai.getWeight());
	    if (!gs.checkEndGame()) return null;
	    
//	    Make a copy to compute the final score
	    MancalaGameState gsCopy = gs.copy();
	    gsCopy.computeFinalScore();
	    
		ai.updateHistory(gsCopy);
		ai.incGamesPlayed();
		
		int finalscore = gsCopy.getScore(player)-gsCopy.getScore(opponent);
		ai.setReward(finalscore);
		
	    if (finalscore > 0) {
	        System.out.println("AIV3 Win");
	    }
	    else if (finalscore < 0) {
	        System.out.println("AIV3 Loose");
	    }
	    else  {
	        System.out.println("Draw");
	    }
	    
	    
	    ai.learnWeights((double)learning_rate/ai.getGamesPlayed());
//	    ai.printThetas(ai.getWeight());
	    ai.reset();
	    firstMoveofFirstPlayer =false;
	    return null;
	}

	@Override
	public Object actionsBeforeDeletion() {
		ai.cleanup();
		return null;
	}









	@Override
	public void reset() {
		ai.reset();
		firstMoveofFirstPlayer =false;
		
	}

}
