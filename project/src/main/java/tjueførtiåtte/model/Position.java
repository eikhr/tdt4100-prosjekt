package tjueførtiåtte.model;

public class Position {
	private int x;
	private int y;
	private Board board;
	
	public Position(Board board, int x, int y) {
		validateBoard(board);
		validatePosition(board, x, y);
		this.board = board;
		this.x = x;
		this.y = y;
	}
	
	public void validateBoard(Board board) {
		if (board == null) {
			throw new IllegalArgumentException("A position must be on a board");
		}
	}
	
	public void validatePosition(Board board, int x, int y) {
		if (!board.containsCoords(x, y)) {
			throw new IllegalArgumentException("The position "+x+", "+y+" does not exist on the board");
		}
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Board getBoard() {
		return board;
	}
}
