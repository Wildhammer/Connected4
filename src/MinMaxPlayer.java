import tree.Node;
import tree.SimpleBoard;



public class MinMaxPlayer implements Player {
	DepthLimitedMinMaxPlayer dlmmp;
	
    @Override
    public int getType() {
        return 4;
    }
    
    public MinMaxPlayer() {
    	dlmmp = new DepthLimitedMinMaxPlayer();
    }
    
    public int minimax(Node n, boolean maximizingPlayer) {
    	//this is impractical but it's been implemented anyway!
    	//since the maximum depth for game is 42 depth parameter has been set 42
		return dlmmp.minimax(n, 42, maximizingPlayer);
	}

    @Override
    public void go(SimpleBoard b) {
        int m = 0;
    	System.gc();
    	SimpleBoard currentGame = new SimpleBoard(b);				
    	
		Node newState = new Node(currentGame);
		if (b.next() == 1)
			minimax(newState, true);
		else
			minimax(newState, false);

		m = newState.getAction();	
        b.Move(m);
    }

	@Override
	public void setMove(int col) {
		
	}
}
