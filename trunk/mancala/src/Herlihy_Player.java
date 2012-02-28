
public class Herlihy_Player implements MancalaPlayer {

	private int player;
	private int opponent;

	public Herlihy_Player (int playerNum) {
	  player = playerNum;
	  opponent = 1 - player;
	 
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
		
		if(usePieMove(gs))
            return -1;

		int currentMove = -1;
		int currentValue = Integer.MIN_VALUE;
		
		for(int i = 0; i < 6; i++)
		{
			if(gs.validMove(i))
			{
				MancalaGameState gs_copy = gs.copy().play(i);
				int tempValue = getMiniMaxABValue(gs_copy, 10, Integer.MIN_VALUE, Integer.MAX_VALUE, player);
				if( tempValue >= currentValue)
				{
					currentValue = tempValue;
					currentMove = i;
				}
			}
		}		
		
		return currentMove;
	}
	
	private static long callCount = 0, returnCount = 0;
	
	private int getMiniMaxABValue(MancalaGameState gs, int depth, int alpha, int beta, int abPlayer) throws Exception
	{
		callCount++;
		if(depth == 0)
		{
			returnCount++;
			return getStateValue(gs, abPlayer);
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
	
	private int getStateValue(MancalaGameState gs, int abPlayer)
	{
		int score = gs.getScore(abPlayer) - gs.getScore(1 - abPlayer);
		for(int i = 0; i < 6; i++)
		{
			score += gs.stonesAt(abPlayer, i);
			score -= gs.stonesAt(1 - abPlayer, i);
			
			switch(abPlayer)
			{
				case 0:
					if(gs.stonesAt(0, i) == 6 - i)
						score *= 3;				
					break;
				case 1:
					if(gs.stonesAt(1, i) == 1 + i)
						score *= 3;
					break;
			}
			
		}
		return score;
	}

	@Override
	public Object postGameActions(MancalaGameState gs) {
		gs.checkEndGame();
//		System.out.println("Final Count: " + callCount);
//		System.out.println("Return Count: " + returnCount);
		return null;
	}

	@Override
	public Object actionsBeforeDeletion() {
		
		return null;
	}

}
