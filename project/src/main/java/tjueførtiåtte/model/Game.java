package tjueførtiåtte.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Game {
	private GameState state = GameState.ONGOING;
	private final Board board;
	private int score = 0;
	private List<GhostTile> ghostTiles = new ArrayList<GhostTile>();
	private List<GameScoreListener> scoreListeners = new ArrayList<GameScoreListener>();
	private List<GameStateListener> gameStateListeners = new ArrayList<GameStateListener>();
	
	public Game() {		
		board = new Board(4,4);
		
		addRandomTile();
		addRandomTile();
		
		fireTilesMoved();
	}
	
	public Game(String serialized) throws IllegalArgumentException {
		String[] parts = serialized.split(";");
		
		try {
			score = Integer.valueOf(parts[1]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid score format in serialized string", e);
		}
		
 		board = new Board(parts[2]);
 		
 		try {
 			state = GameState.valueOf(parts[0]);
 		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid GameState format in serialized string", e);
 		}
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
	
	public boolean boardPositionHasTile(Position position) {
		return !board.isEmptyTile(position);
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
	
	public Collection<RenderableTile> getRenderableTiles() {
		Collection<RenderableTile> tiles = new ArrayList<RenderableTile>();
		tiles.addAll(board.getTiles());
		tiles.addAll(getGhostTiles());
		return tiles;
	}
	
	// Executes a move in the given direction
	public void move(Direction direction) throws IllegalStateException {
		if (state != GameState.ONGOING && state != GameState.CONTINUED) {
			throw new IllegalStateException("Cannot move while game state is not ongoing or continued");
		}
		
		ghostTiles.clear();
		
		for (Tile tile : board.getTiles()) {
			tile.startNewTurn();
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
				
				hasMoved = hasMoved || !Tile.areEqualRows(col, board.getCol(i));
				
				board.setCol(i, col);
			}
		} else {
			for (int i = 0; i < board.getHeight(); i++) {
				Tile row[] = board.getRow(i);
				row = moveLine(row, reverse);

				hasMoved = hasMoved || !Tile.areEqualRows(row, board.getRow(i));
				
				board.setRow(i, row);
			}
		}
		

		for (Tile tile : board.getTiles()) {
			tile.endTurn();
		}
		
		if (hasMoved) {
			// after moving we add a new tile and fire change events
			addRandomTile();
			
			fireScoreUpdated();
			updateGameState();
			fireTilesMoved();
		} else {
			// Do literally nothing
			// System.out.println("didn't move");
		}
	}
	
	private void updateGameState() {
		// if we are playing normally and have won :D
		if (state == GameState.ONGOING && isWon()) {
			state = GameState.WON;
			fireGameStateUpdated();
			return;
		} 
		// if we have lost :(
		if (state != GameState.LOST && isLost()) {
			state = GameState.LOST;
			fireGameStateUpdated();
			return;
		}
	}
	
	// continue the game after having won
	public void continueGame() throws IllegalStateException {
		if (state != GameState.WON) {
			throw new IllegalStateException("Cannot continue a game that is not in 'WON' state");
		}
		
		state = GameState.CONTINUED;
		fireGameStateUpdated();
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
	
	/*
	 * Checks all pairs of neighbouring tiles and return whether or not any of them can merge together
	 */
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
	
	// moves line to beginning of list, and merges any mergeable tiles, adds removed tiles to gostTiles
	private Tile[] moveLine(Tile[] line, boolean reverse) throws IllegalArgumentException {
		if (reverse)
			line = Tile.reverseRow(line);
		
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
				previous.increaseTier();
				ghostTiles.add(new GhostTile(tile, previous));
				score += previous.getScoreValue();
				previous = null;
			} else {
				previous = tile;
				newLine[pos] = tile;
				pos++;
			}
		}
		

		if (reverse)
			newLine = Tile.reverseRow(newLine);
		
		return newLine;
	}

	// adds a random tile in a random empty spot
	private void addRandomTile() throws IllegalStateException {
		List<Position> emptyPositions = board.getEmptyPositions();
		
		if (emptyPositions.isEmpty()) {
			throw new IllegalStateException("No empty positions to place new tile in!");
		}
		
		// choose a random position
		Random random = new Random();
		Position position = emptyPositions.get(random.nextInt(emptyPositions.size()));
		
		// add the tile
		addRandomTile(position);
	}
	
	// adds a random tile in the specified location (either a 2 or a 4)
	private void addRandomTile(Position position) throws IllegalArgumentException {
		Random random = new Random();
		if (random.nextInt(5) == 0) {
			board.addTile(position, new Tile( 2));
		} else {
			board.addTile(position, new Tile( 1));
		}
	}
	
	@Override
	public String toString() {
		return String.format("Game in state %s, with score %d\n", getState(), getScore())+board.toString();
	}
	
	private void fireScoreUpdated() {
		for (GameScoreListener listener : scoreListeners) {
			listener.scoreUpdated(getScore());
		}
		for (GameStateListener listener : gameStateListeners) {
			listener.scoreUpdated(getScore());
		}
	}
	
	public void addScoreListener(GameScoreListener listener) {
		scoreListeners.add(listener);
	}
	
	public void removeScoreListener(GameScoreListener listener) {
		scoreListeners.remove(listener);
	}
	
	private void fireGameStateUpdated() {
		for (GameStateListener listener : gameStateListeners) {
			listener.stateUpdated(getState());
		}
	}

	private void fireTilesMoved() {
		for (GameStateListener listener : gameStateListeners) {
			listener.tilesMoved(getRenderableTiles());
		}
	}
	
	public void addGameStateListener(GameStateListener listener) {
		gameStateListeners.add(listener);
	}

	public void removeGameStateListener(GameStateListener listener) {
		gameStateListeners.remove(listener);
	}
	
	public String serialize() {
		return String.format("%s;%d;%s", state.toString(), score, board.serialize());
	}
}