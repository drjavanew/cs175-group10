import java.util.Random;

// This AI is not working well because of the AB


public class Group10_Player implements MancalaPlayer {

	private int player;
	private int opponent;
    private Group10 ai;
    private int cutoff_depth;
    private boolean firstMoveofFirstPlayer =false; 
   
	public Group10_Player (int playerNum) {
	  player = playerNum;
	  opponent = 1 - player;
	  cutoff_depth = 10;
	  ai = new Group10(playerNum,"Kdata.txt");
	  
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

	@Override
	public int getMove(MancalaGameState gs) throws Exception {
		
		
		ai.updateHistory(gs.copy());
		
		
		if((player == 1) && (usePieMove(gs))) {
            return -1;
		}
		
		if ((player== 0) && (ai.isNew())) {
			if (firstMoveofFirstPlayer == false) {
			    firstMoveofFirstPlayer = true;
			    return 2;
			}
			else {
				firstMoveofFirstPlayer =false;
				Random rand = new Random();
				int val = rand.nextInt(10);
				if (val >=5) return 1;
				else return 3;
			}
			 
			
		}
		
		int currentMove = -1;
		double currentValue = Float.NEGATIVE_INFINITY;
		
		for(int i = 0; i < 6; i++)
		{
			if(gs.validMove(i))
			{
				MancalaGameState gs_copy = gs.copy().play(i);
				double tempValue = getMiniMaxABValue(gs_copy, cutoff_depth, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, player);
				if( tempValue >= currentValue)
				{
					currentValue = tempValue;
					currentMove = i;
				}
			}
		}
		
		
		return currentMove;
	}
	
	
	
	private double getMiniMaxABValue(MancalaGameState gs, int depth, double alpha, double beta, int abPlayer) throws Exception
	{
		
		if(depth == 0)
		{
			
			return ai.getStateValue(gs, abPlayer);
		}
		
		if(abPlayer == player)
		{
			for(int i = 0; i < 6; i++)
			{
				if(gs.validMove(i))
				{
					MancalaGameState gs_copy = gs.copy().play(i);
					alpha = Math.max(alpha, getMiniMaxABValue(gs_copy, depth - 1, alpha, beta, gs_copy.CurrentPlayer));
					
					if(beta <= alpha)
						break;
				}
			}
			
			return alpha;
		} else {
			for(int i = 0; i < 6; i++)
			{
				if(gs.validMove(i))
				{
					MancalaGameState gs_copy = gs.copy().play(i);
					beta = Math.min(beta, getMiniMaxABValue(gs_copy, depth - 1, alpha, beta, gs_copy.CurrentPlayer));
					
					if(beta <= alpha)
						break;
				}
			}
			
			return beta;
		}
	}
	
	

	/* This method will be used to learn from the game we just played. */
	public Object postGameActions(MancalaGameState gs) {
//		ai.printThetas(ai.getWeight());
	    if (!gs.checkEndGame()) return null;
	    
//	    Make a copy to compute the final score
	    MancalaGameState gsCopy = gs.copy();
	    gsCopy.computeFinalScore();
	    
		ai.updateHistory(gsCopy);
		
		int finalscore = gsCopy.getScore(player)-gsCopy.getScore(opponent);
		ai.setReward(finalscore);
		
	    if (finalscore > 0) {
	        System.out.println("Khanh Win");
	    }
	    else if (finalscore < 0) {
	        System.out.println("Khanh Loose");
	    }
	    else  {
	        System.out.println("Draw");
	    }
	    
	    
	    ai.learnWeights();
//	    ai.printThetas(ai.getWeight());
	    ai.reset();
	    
	    return null;
	}

	@Override
	public Object actionsBeforeDeletion() {
		ai.cleanup();
		return null;
	}


}
