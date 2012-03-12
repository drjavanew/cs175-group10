public class MinMaxAB_Player implements MancalaPlayer


{
        private int player;
        public MinMaxAB_Player(int playerNum)
        {
                player = playerNum;
        }

        @Override
        public int getMove(MancalaGameState gs) throws Exception {
                MancalaGameState gs_copy=gs.copy();
                player = gs_copy.CurrentPlayer();
                Node currentNode = new Node(gs_copy, 0);
                return bestMove(currentNode);
        }

        @Override
        public Object postGameActions(MancalaGameState gs) {
                // TODO Auto-generated method stub
                return null;
        }
    
        public int negaScout(Node node, int depth, int alpha, int beta)
        {
        	int a,b;
        	if(node.getBoard().checkEndGame() || depth == 0)
        		return evalFunction(node.getBoard());
        	a = alpha;
        	b = beta;
        	 for( int i = 0; i < 6; i++)
        	 {
        		 if (node.getBoard().validMove(i)) {
        			 
        		 MancalaGameState copy = node.getBoard().copy();
        		 try {
					copy.play(i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		 Node child = new Node(copy, 0);
        		  int t = - negaScout(child,depth-1,-b,-a);
                  if ( (t > a) && (t < beta) && (i > 0) && (depth < 12))
                          a = -negaScout(child,depth-1,-beta,-t);
                  a = Math.max(a,t);
                  if (a >= beta)
                          return a;
                  b = a + 1;
              }
              
          }
        	 return a;
        	
        }
     
        public int bestMove(Node node)
        {
        	int move = -1;
        	int best = Integer.MIN_VALUE;
        	for (int i = 0; i < 6; i++)
        	{
        		
        		 if (node.getBoard().validMove(i)) {
        			 
            		 MancalaGameState copy = node.getBoard().copy();
            		 try {
    					copy.play(i);
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
            		Node child = new Node(copy, 0);
            		if(move == -1)
            			 move = i;
            		int score = -negaScout(child, 12, Integer.MIN_VALUE, Integer.MAX_VALUE);
            		if(score > best){
            			move = i;
            			best = score;
        		 }
        		 }
        	}
        	return move;
        }

		private int evalFunction(MancalaGameState board) {
			if(player == 1)
				return board.getScore(0) - board.getScore(1);
			else
				return board.getScore(1) - board.getScore(0);
		}

		@Override
		public Object actionsBeforeDeletion() {
			
			return null;
		}

		@Override
		public void reset() {
			
			
		}

		

}
