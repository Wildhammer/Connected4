import java.util.Vector;
import java.util.Vector;

import tree.Node;
import tree.SimpleBoard;

public class AlphaBetaPruningPlayer implements Player {
	boolean first;
	long mean;
	int stepCount;
	
	public AlphaBetaPruningPlayer() {
		super();
		first = true;
		mean = 0;
		stepCount = 0;
	}
	
	@Override
	public void setMove(int col) {
	}

	@Override
	public int getType() {
		return 3;
	}

	@Override
	public void go(SimpleBoard b) {
		System.gc();
	
		long duration = System.nanoTime();
		
		SimpleBoard currentGame = new SimpleBoard(b);

		if( first && b.next() == 1 )//this is for Victor Allis thesis
		{
			b.Move(3);
			first = false;
			return;
		}
			
		int m = 0;

		Node newState = new Node(currentGame);
		if (b.next() == 1)
			alphabeta(newState, 8, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
		else
			alphabeta(newState, 8, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

		m = newState.getAction();
		
	
		System.out.println((System.nanoTime()-duration)/1000000);
		b.Move(m);
	}

	public int alphabeta(Node n, int depth, int alpha, int beta,
			boolean maximizingPlayer) {
		if (depth == 0 || n.getBoard().over())
			return heuristic(n);
		if (maximizingPlayer) {
			int bestValue = alpha;
			int action = -1;
			for (int i = 0; i < 7; i++) {
				if (n.getAncestor(i) == null) {
					if (n.getBoard().ret_col()[i] == 6)
						continue;
					SimpleBoard board = new SimpleBoard(n.getBoard());
					board.Move(i);
					Node child = new Node(board);
					n.setAncestor(child, i);
					int newAlpha = alphabeta(child, depth - 1, bestValue, beta,
							false);
					if (bestValue != Math.max(bestValue, newAlpha)) {
						bestValue = Math.max(bestValue, newAlpha);
						action = i;
					}
					if (beta <= bestValue)
						break;
				}
			}
			n.setAction(action);
			return bestValue;
		} else {
			int bestValue = beta;
			int action = -1;
			for (int i = 0; i < 7; i++) {
				if (n.getAncestor(i) == null) {
					if (n.getBoard().ret_col()[i] == 6)
						continue;
					SimpleBoard board = new SimpleBoard(n.getBoard());
					board.Move(i);
					Node child = new Node(board);
					n.setAncestor(child, i);
					int newBeta = alphabeta(child, depth - 1, alpha, bestValue,
							true);
					if (bestValue != Math.min(bestValue, newBeta)) {
						bestValue = Math.min(bestValue, newBeta);
						action = i;
					}
					if (bestValue <= alpha)
						break;
				}
			}
			n.setAction(action);
			return bestValue;
		}
	}

	public int evaluation(Node n) {
		return rowHeuristics(n) + columnHeuristics(n) + diagonalHeuristics(n);
	}

	public int columnHeuristics(Node n) {
		int returnValue = 0, checkersNum = 0;
		int currentMoveRow = n.getBoard().m_x, currentMoveColumn = n.getBoard().m_y;
		int[][] board = n.getBoard().view();
		if (board[3][currentMoveColumn] == n.getBoard().next())
			return 0;
		for (int i = currentMoveRow; i >= 0; i--) {
			if (board[i][currentMoveColumn] == 3 - n.getBoard().next())
				checkersNum++;
			else
				break;
		}
		if (checkersNum == 1)
			returnValue = 1;
		else if (checkersNum == 2)
			returnValue = 4;
		else if (checkersNum == 3)
			returnValue = 32;

		return returnValue;
	}

	public int calcular(Vector<Integer> playedRow, SimpleBoard n) {
		int returnValue = 0;
		Vector<Integer> tempPlayedRow = new Vector<Integer>();
		for( Integer i : playedRow )
		{
			if( i == 3-n.next() )
				tempPlayedRow.add(i);
			else
				tempPlayedRow.add(0);
		}

		for (int i = 0; i < playedRow.size()-3; i++) {
			int temp = 0;
			int checkersNum = tempPlayedRow.get(i) + tempPlayedRow.get(i + 1)
					+ tempPlayedRow.get(i+2) + tempPlayedRow.get(i+3);
			checkersNum /= 3 - n.next();
			if (checkersNum == 1)
				temp = 1;
			else if (checkersNum == 2)
				temp = 4;
			else if (checkersNum == 3)
				temp = 32;
			for (int j = i; j < i + 4; j++) {
				if (playedRow.get(j) == n.next()) {
					temp = 0;
					break;
				}
			}
			returnValue += temp;
		}
		return returnValue;
	}
	
	public int rowHeuristics(Node n) {
		int currentMoveRow = n.getBoard().m_x, currentMoveColumn = n.getBoard().m_y;
		Vector<Integer> playedRow = new Vector<Integer>();
		for (int i = 0; i < 7; i++) {
			playedRow.add(n.getBoard().view()[currentMoveRow][i]);
			if (i != currentMoveColumn - 1 && i != currentMoveColumn + 1
					&& i != currentMoveColumn
					&& playedRow.get(i) == 3 - n.getBoard().next())
				playedRow.set(i, 0);
		}
		return calcular(playedRow, n.getBoard());
	}

	public int diagonalHeuristics(Node n) {
		int returnValue = 0;
		int currentMoveRow = n.getBoard().m_x, currentMoveColumn = n.getBoard().m_y;
		int[][] board = n.getBoard().view();
		int temp = Math.min(currentMoveRow, currentMoveColumn);
		int[] lu = new int[2];
		lu[0] = currentMoveRow-temp;
		lu[1] = currentMoveColumn-temp;
		temp = Math.min(5-currentMoveRow, 6-currentMoveColumn);
		int[] rd = new int[2];
		rd[0] = currentMoveRow+temp;
		rd[1] = currentMoveColumn+temp;
		temp = Math.min(currentMoveRow,6-currentMoveColumn);
		int[] ru = new int[2];
		ru[0] = currentMoveRow-temp;
		ru[1] = currentMoveColumn+temp;
		temp = Math.min(5-currentMoveRow, currentMoveColumn);
		int[] ld = new int[2];
		ld[0] = currentMoveRow+temp;
		ld[1] = currentMoveColumn-temp;
		
		Vector<Integer> playedRow = new Vector<Integer>();
		for ( int i = 0 ; i <= lu[1]-rd[1] ; i++ )
		{
			playedRow.add(board[lu[0]+i][lu[1]+i]);
			if( i != currentMoveColumn-1 && i != currentMoveColumn+1 && i != currentMoveColumn && playedRow.get(i) == 3-n.getBoard().next() )
				playedRow.set(i, 0);
		}
		returnValue += calcular(playedRow, n.getBoard());
		playedRow.clear();
		for( int i = 0 ; i <= ru[1]-ld[1] ; i++ )
		{
			playedRow.add(board[ru[0]+i][ru[1]-i]);
			if( i != currentMoveColumn-1 && i != currentMoveColumn+1 && i != currentMoveColumn && playedRow.get(i) == 3-n.getBoard().next() )
				playedRow.set(i, 0);
		}
		returnValue += calcular(playedRow, n.getBoard());
		
		return returnValue;
	}

	public int heuristic(Node n) {
		if (n.getBoard().winner != 0)
			switch (n.getBoard().winner) {
			case 1:
				return 256;
			case 2:
				return -256;
			}
		int returnValue = 0;
		if ( n.getBoard().next() == 2 )
			returnValue = evaluation(n);
		else if ( n.getBoard().next() == 1 )
			returnValue = -1*evaluation(n);
		return returnValue;
	}

}
