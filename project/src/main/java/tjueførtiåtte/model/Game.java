package tjueførtiåtte.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Game {
	private GameState state = GameState.ONGOING;
	private Board board;
	private int score = 0;
	private List<GhostTile> ghostTiles = new ArrayList<GhostTile>();
	private List<GameScoreListener> scoreListeners = new ArrayList<GameScoreListener>();
	
	
	public Game() {		
		board = new Board(4,4);
		
		addRandomTile();
		addRandomTile();
	}
	
	public GameState getState() {
		return state;
	}
	
	public List<GhostTile> getGhostTiles() {
		return ghostTiles;
	}
	
	public int getScore() {
		return score;
	}
	
	public Tile getTile(int x, int y) {
		return board.getTile(x, y);
	}
	
	public int getNumberOfTiles() {
		return board.getNumberOfTiles();
	}
	
	public boolean boardPositionHasTile(int x, int y) {
		return !board.isEmptyTile(x, y);
	}
	
	public int getBoardWidth() {
		return board.getWidth();
	}
	
	public int getBoardHeight() {
		return board.getHeight();
	}
	
	public Collection<Tile> getTiles() {
		return board.getTiles();
	}
	
	// Executes a move in the given direction
	public void move(Direction direction) {
		ghostTiles.clear();
		if (state != GameState.ONGOING && state != GameState.CONTINUED) {
			throw new IllegalStateException("Cannot move while game state is not ongoing or continued");
		}
		
		for (int x = 0; x < board.getWidth(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				if (!board.isEmptyTile(x, y)) {
					Tile tile = board.getTile(x, y);
					Coordinates coords = new Coordinates(x, y);
					tile.setPrevious(coords);
				}
			}
		}

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
			// after moving we add a new tile and notify manager of potential score update
			addRandomTile();
			fireScoreUpdated();
			updateGameState();
		} else {
			// Do literally nothing
			// System.out.println("didn't move");
		}
	}
	
	private void updateGameState() {
		// if we are playing normally and have won :D
		if (state == GameState.ONGOING && isWon()) {
			state = GameState.WON;
			return;
		} 
		// if we have lost :(
		if (state != GameState.LOST && isLost()) {
			state = GameState.LOST;
		}
	}
	
	// continue the game after having won
	public void continueGame() {
		if (state != GameState.WON) {
			throw new IllegalStateException("Cannot continue a game that is not in 'WON' state");
		}
		
		state = GameState.CONTINUED;
	}
	
	private boolean isWon() {
		return board.getBestTile().getTier() >= 11;
	}
	
	private boolean isLost() {
		if (board.getNumberOfEmptyPositions() != 0) 
			return false;
		if (neighborsCanMerge())
			return false;
		
		return true;
	}
	
	private boolean neighborsCanMerge() {
		// horizontally
		for (int x = 0; x < board.getWidth()-1; x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				if (board.getTile(x, y).canMergeWith(board.getTile(x+1, y)))
					return true;
			}
		}
		
		// vertically
		for (int x = 0; x < board.getWidth(); x++) {
			for (int y = 0; y < board.getHeight()-1; y++) {
				if (board.getTile(x, y).canMergeWith(board.getTile(x, y+1)))
					return true;
			}
		}
		
		return false;
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
	
	// moves line to beginning of list, and merges any mergeable tiles, adds removed tiles to gostTiles
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
				score += previous.getScoreValue();
				ghostTiles.add(new GhostTile(tile, previous));
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
			board.addTile(x, y, new Tile(board, 2));
		} else {
			board.addTile(x, y, new Tile(board, 1));
		}
	}
	
	public String toString() {
		return board.toString();
	}
	
	public void fireScoreUpdated() {
		for (GameScoreListener listener : scoreListeners) {
			listener.gameScoreUpdated(getScore());
		}
	}
	
	public void addScoreListener(GameScoreListener listener) {
		scoreListeners.add(listener);
	}
	
	public void removeScoreListener(GameScoreListener listener) {
		scoreListeners.remove(listener);
	}
}