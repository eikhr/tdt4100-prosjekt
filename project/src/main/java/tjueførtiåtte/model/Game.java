package tjueførtiåtte.model;

import java.util.List;
import java.util.Random;

public class Game {
	private Board board;
	private int score = 0;
	
	public Game() {
		board = new Board();
		
		addRandomTile();
		addRandomTile();
	}
	
	public int getScore() {
		return score;
	}
	
	public void move(Direction direction) {
		

		boolean reverse = false;
		boolean vertical = false;
		
		switch (direction) {
		case UP:
			reverse = false;
			vertical = true;
			break;
		case DOWN:
			reverse = true;
			vertical = true;
			break;
		case LEFT:
			reverse = false;
			vertical = false;
			break;
		case RIGHT:
			reverse = true;
			vertical = false;
			break;
		}
		
		boolean hasMoved = false;
		
		if (vertical) {
			for (int i = 0; i < board.getWidth(); i++) {
				Tile col[] = board.getCol(i);
				col = moveLine(col, reverse);
				
				hasMoved = hasMoved || !equals(col, board.getCol(i));
				
				board.setCol(i, col);
			}
		} else {
			for (int i = 0; i < board.getHeight(); i++) {
				Tile row[] = board.getRow(i);
				row = moveLine(row, reverse);

				hasMoved = hasMoved || !equals(row, board.getRow(i));
				
				board.setRow(i, row);
			}
		}
		
		if (hasMoved) {
			// after moving we add a new tile
			addRandomTile();
		} else {
			System.out.println("didn't move");
		}
		
		System.out.println(this.toString());
	}
	
	private boolean equals(Tile line1[], Tile line2[]) {
		for (int i = 0; i < line1.length; i++) {
			if (line1[i] == null) {
				if (line2[i] != null)
					return false;
			} else if (line2[i] == null) {
				if (line1[i] != null)
					return false;
			} else {
				if (line1[i].getTier() != line2[i].getTier())
					return false;
			}
		}
		return true;
	}
	
	private Tile[] reverse(Tile line[]) {
		Tile newLine[] = new Tile[line.length];
		
		for (int i = 0; i < line.length; i++) {
			newLine[i] = line[line.length - 1 - i];
		}
		
		return newLine;
	}
	
	// moves line to beginning of list, and merges any mergeable tiles 
	private Tile[] moveLine(Tile[] line, boolean reverse) {
		if (reverse)
			line = reverse(line);
		
		// move all tiles to beginning of list
		Tile[] shifted = new Tile[line.length];
		int pos = 0;
		for (Tile tile : line) {
			if (tile != null) {
				shifted[pos] = tile;
				pos++;
			}
		}
		
		// merge tiles
		Tile previous = null;
		Tile[] newLine = new Tile[line.length];
		pos = 0;
		for (Tile tile : shifted) {
			if (tile == null)
				break;
			if (previous != null && tile.canMergeWith(previous)) {
				// "merge" with the previous tile by increasing its value, and not adding this one
				previous.increaseValue();
				score += previous.getValue();
				previous = null;
			} else {
				previous = tile;
				newLine[pos] = tile;
				pos++;
			}
		}
		

		if (reverse)
			newLine = reverse(newLine);
		
		return newLine;
	}

	// adds a random tile in a random empty spot
	private void addRandomTile() {
		List<int[]> emptyPositions = board.getEmptyPositions();
		
		if (emptyPositions.isEmpty()) {
			throw new IllegalStateException("No empty positions to place new tile in!");
		}
		
		// choose a random position
		Random random = new Random();
		int[] position = emptyPositions.get(random.nextInt(emptyPositions.size()));
		
		// add the tile
		addRandomTile(position[0], position[1]);
	}
	
	// adds a random tile in the specified location (either a 2 or a 4)
	private void addRandomTile(int x, int y) {
		Random random = new Random();
		if (random.nextInt(5) == 0) {
			board.addTile(x, y, new Tile(2));
		} else {
			board.addTile(x, y, new Tile(1));
		}
	}
	
	public String toString() {
		return board.toString();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Tile tile = new Tile(1);

		System.out.println(tile.getDisplayText());
		
		tile.increaseValue();

		System.out.println(tile.getDisplayText());
		
		Tile tile2 = new Tile(1);
		
		System.out.println(tile.canMergeWith(tile2));
		
		tile2.increaseValue();

		System.out.println(tile.canMergeWith(tile2));
		
		Game game = new Game();
		
		System.out.println(game.toString());
		
		game.move(Direction.UP);
		
		System.out.println(game.toString());

		game.move(Direction.DOWN);
		
		System.out.println(game.toString());
		
		for (int i = 0; i < 5; i++) {
			game.move(Direction.DOWN);
			
			System.out.println(game.toString());
	
			game.move(Direction.RIGHT);
			
			System.out.println(game.toString());
		}
	}
}