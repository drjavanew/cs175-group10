public class MinMaxAB_Player implements MancalaPlayer
//I plan on implementing the negascout we heard about today or whatever on top of this 

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
                return findBestMove(currentNode);
        }

        @Override
        public Object postGameActions(MancalaGameState gs) {
                // TODO Auto-generated method stub
                return null;
        }
    
        public static int findBestMove(Node currentNode)
        {
                int best =-1;
                int turn = currentNode.currentState.CurrentPlayer();

                if (turn == 0)
                        currentNode.setValue(Integer.MAX_VALUE);
                else
                        currentNode.setValue(Integer.MIN_VALUE);
                for(int i = 0; i < 6; i++)
                {
                        try {
                        if (currentNode.getState().validMove(i))
                        {
                                MancalaGameState newState = currentNode.getState().copy();

                                        newState.play(i);


                                Node newNode = new Node(newState,currentNode.getDepth()+1);
                                newNode.setParent(currentNode);
                                currentNode.setChild(newNode, i);
                                if (newNode.getState().checkEndGame()|| newNode.getDepth() > 8) {
                                        newNode.setValue(newNode.getState().getScore(1) - newNode.getState().getScore(0));
                                } else
                                        findBestMove(newNode);

                                if (currentNode.getState().CurrentPlayer() == 1) {
                                        if (currentNode.getChild(i) != null) {
                                                if (currentNode.getChild(i).getValue() > currentNode.getValue()) {
                                                        currentNode.setValue(currentNode.getChild(i).getValue());
                                                        best = i;
                                                }
                                        }
                                        currentNode.setChild(null,i);

                                        Node nodePtr = currentNode;
                                        while (nodePtr.getParent()!=null) {
                                                nodePtr = nodePtr.getParent();
                                                if ((nodePtr.getState().CurrentPlayer() == 0) && (currentNode.getValue() > nodePtr.getValue())) {
                                                        nodePtr = null;
                                                        return best;
                                                }
                                        }

                                        nodePtr = null;
                                }

                                // PLAYER 1 is Player = MIN
                                if (currentNode.getState().CurrentPlayer() == 0)  {
                                        if (currentNode.getChild(i) != null) {
                                                if (currentNode.getChild(i).getValue() < currentNode.getValue()) {
                                                        currentNode.setValue(currentNode.getChild(i).getValue());
                                                        best = i;
                                                }
                                        }
                                        currentNode.setChild(null,i);

                                        Node nodePtr = currentNode;
                                        while (nodePtr.getParent()!=null) {
                                                nodePtr = nodePtr.getParent();
                                                if ((nodePtr.getState().CurrentPlayer() == 1) && (currentNode.getValue() < nodePtr.getValue())) {
                                                        nodePtr = null;
                                                        return best;
                                                }
                                        }

                                        nodePtr = null;
                                }


                        }
                        }
                        catch (java.lang.OutOfMemoryError | Exception e) {
                                System.out.println("OUT OF MEM");
                                return -1;
                        }



        }
        return best;
}


}
