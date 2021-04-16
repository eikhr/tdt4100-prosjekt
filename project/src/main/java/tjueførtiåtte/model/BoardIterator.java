package tjueførtiåtte.model;

import java.util.Iterator;

public class BoardIterator implements Iterator<Tile> {
	private Board board;
	private int index = 0;
	
	public BoardIterator(Board board) {
		this.board = board;
	}

	@Override
	public boolean hasNext() {
		return index < board.getHeight()*board.getWidth();
	}

	@Override
	public Tile next() {
		int xPos = index % board.getWidth();
		int yPos = index / board.getWidth();
		
		index++;
		
		return board.getTile(xPos, yPos);
	}

}
