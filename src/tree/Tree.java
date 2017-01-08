package tree;

public class Tree {
	Node root;
	
	
	public Tree(Node _root) {
		root = _root;
	}
	public Tree() {
		root = new Node();
	}
	
	public Node getRoot() {
		return root;
	}
}
