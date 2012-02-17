public class Herlihy_Player implements MancalaPlayer {

	private int player;
	private int opponent;

	public Herlihy_Player (int playerNum) {
	  player = playerNum;
	  opponent = 1 - player;
	 
	}
	
	@Override
	public int getMove(MancalaGameState gs) throws Exception {
		int currentMove = -1;
		int currentValue = Integer.MIN_VALUE;
		
		for(int i = 0; i < 6; i++)
		{
			if(gs.validMove(i))
			{
				MancalaGameState gs_copy = gs.copy().play(i);
				int tempValue = getMiniMaxABValue(gs_copy, 14, Integer.MIN_VALUE, Integer.MAX_VALUE, player);
				if( tempValue >= currentValue)
				{
					currentValue = tempValue;
					currentMove = i;
				}
			}
		}		
		
		return currentMove;
	}
	
	private static int callCount = 0, returnCount = 0;
	
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
		System.out.println("Final Count: " + callCount);
		System.out.println("Return Count: " + returnCount);
		return null;
	}

}
