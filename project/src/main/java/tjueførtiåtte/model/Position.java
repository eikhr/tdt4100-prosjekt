package tjueførtiåtte.model;

public class Position {
	private final int x;
	private final int y;
	private final Board board;
	
	public Position(Board board, int x, int y) throws IllegalArgumentException {
		validateBoard(board);
		validatePosition(board, x, y);
		this.board = board;
		this.x = x;
		this.y = y;
	}
	
	public void validateBoard(Board board) throws IllegalArgumentException {
		if (board == null) {
			throw new IllegalArgumentException("A position must be on a board");
		}
	}
	
	public void validatePosition(Board board, int x, int y) throws IllegalArgumentException {
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
