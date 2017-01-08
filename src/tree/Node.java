package tree;

public class Node {
	SimpleBoard board;
	int action;
	Node[] ancestors;
	
	public Node() {
		action = -1;
		ancestors = new Node[7];
		for( int i = 0 ; i < 7 ; i++ )
			ancestors[i] = null;
	}
	
	public Node( SimpleBoard b ) {
		this();
		board = b;
	}
	
	public SimpleBoard getBoard() {
		return board;
	}
	
	public void setAction( int a ) {
		action = a;
	}
	
	public int getAction(){
		return action;
	}
	
	public void setAncestor( Node n , int i ) {
		ancestors[i] = n;
	}
	
	public Node getAncestor( int i ) {
		return ancestors[i];
	}
	
}