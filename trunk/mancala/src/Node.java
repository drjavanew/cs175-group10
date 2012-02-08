import java.util.Vector;

public class Node {
	private int id;
	public Board currentBoard;
	private int depth;
	private int value;
	private Node parent;
	private Vector<Node> children;

	public Node(Board aBoard, int depthVal) {
		id = -1;
		currentBoard = aBoard;
		depth = depthVal;
		parent = null;
		children = new Vector<Node>();

	}

	public int getId() {
		return id;
	}

	public void setId(int anID) {
		id = anID;
	}

	public Board getBoard() {
		return currentBoard;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depthVal) {
		depth = depthVal;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int newVal) {
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
