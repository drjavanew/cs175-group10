
public class Node {
        public final int MIN = -9999;
        public final int MAX = 9999;
        public MancalaGameState currentState;
        private int depth;
        private int value;
        private boolean visited;
        private Node parent;
        private Node children[] = new Node[20];

        public Node(MancalaGameState aState, int depthVal) {
                currentState = aState;
                depth = depthVal;
                visited = false;
                parent = null;
                for (int i = 0; i < children.length; i++) {
                        children[i] = null;
                }
                value = MIN;
        }

        public MancalaGameState getState() {
                return currentState;
        }

        public int getDepth () {
                return depth;
        }

        public void setDepth (int depthVal) {
                depth = depthVal;
        }

        public int getValue() {
                return value;
        }

        public void setValue (int newVal) {
                value = newVal;
        }

        public boolean isVisited() {
                return visited;
        }

        public void setVisited(boolean boolVal) {
                visited = boolVal;
        }

        public Node getParent() {
                return parent;
        }

        public void setParent(Node aNode) {
                parent = aNode;
        }

        public Node getChild(int childId) {
                return children[childId];
        }

        public void setChild(Node aNode, int childId) {
                children[childId] = aNode;
        }

}
