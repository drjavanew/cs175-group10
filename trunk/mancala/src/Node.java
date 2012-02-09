import java.util.Vector;

public class Node {
	private int id;
	public MancalaGameState currentBoard;
	private int depth;
	private double value;
	private Node parent;
	private Vector<Node> children;

	public Node(MancalaGameState aBoard, int depthVal) {
		this.id = -1;
		this.currentBoard = aBoard;
		this.depth = depthVal;
		this.parent = null;
		this.children = new Vector<Node>();

	}

	public int getId() {
		return id;
	}

	public void setId(int anID) {
		id = anID;
	}

	public MancalaGameState getBoard() {
		return currentBoard;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depthVal) {
		depth = depthVal;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double newVal) {
		value = newVal;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node aNode) {
		parent = aNode;
	}

	public Node getChild(int childId) {

		for (Node a : children) {
			if (a.getId() == childId)
				return a;
		}
		return null;
	}

	public void deleteChild(int childId) {
		if (!children.isEmpty())
			for (Node a : children)
				if (a.getId() == childId) {
					children.remove(a);
					return;
				}

	}

	public void setChild(Node aNode, int childId) {
		aNode.setId(childId);
		children.add(aNode);
	}

}
