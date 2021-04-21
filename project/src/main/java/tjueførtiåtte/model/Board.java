package tjueførtiåtte.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Board {
	private Tile[][] tiles;
	private int height;
	private int width;
	
	public Board(int height, int width) {
		tiles = new Tile[height][width];
		this.height = height;
		this.width = width;
	}
	
	public Board(String serialized) {
		String[] array = serialized.split(",");
		width = Integer.valueOf(array[0]);
		height = Integer.valueOf(array[1]);
		
		tiles = new Tile[height][width];
		
		for (int i = 2; i < array.length; i++) {
			int x = (i-2) % width;
			int y = (i-2) / width;
			
			if (!array[i].equals("-")) {
				tiles[y][x] = new Tile(array[i]);
				tiles[y][x].setPosition(new Position(this, x, y));
			}
		}
	}

	/*
	 * Gets the Tile at the specified coordinates
	 * MAY BE NULL
	 */
	public Tile getTile(int x, int y) {
		return getTile(createPosition(x, y));
	}
	
	private Position createPosition(int x, int y) {
		try {
			return new Position(this, x, y);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("The coordinates do not correspond to a valid board position.");
		}
	}
	
	/*
	 * Gets the tile in the specified location
	 * MAY BE NULL
	 */
	public Tile getTile(Position position) {
		return tiles[position.getY()][position.getX()];
	}
	
	public Iterator<Tile> getIterator() {
		return new BoardIterator(this);
	}
	
	public Collection<Tile> getTiles() {
		Collection<Tile> tiles = new ArrayList<Tile>();

		getIterator().forEachRemaining(tiles::add);
		
		tiles.removeIf(Objects::isNull);
		
		return tiles;
	}
	
	public Tile getBestTile() {
		Iterator<Tile> iterator = getIterator(); 
		
		Tile best = null;
		
		while (iterator.hasNext()) {
			Tile tile = iterator.next();
			if (tile != null && (best == null || tile.getTier() > best.getTier())) {
				best = tile;
			}
		}
		
		return best;
	}
	
	public Tile[] getRow(int y) {
		validateRowIndex(y);
		
		return tiles[y];
	}
	
	public void setRow(int y, Tile[] row) {
		validateRowIndex(y);
		
		for (int i = 0; i < row.length; i++) {
			if (row[i] != null) {
				setTile(new Position(this, i, y), row[i]);
			} else {
				tiles[y][i] = null;
			}
		}
	}
	
	public Tile[] getCol(int x) {
		validateColumnIndex(x);
		
		Tile col[] = new Tile[getHeight()];
		for (int i = 0; i < tiles.length; i++) {
			col[i] = tiles[i][x];
		}
		return col;
	}
	
	public void setCol(int x, Tile[] col) {
		validateColumnIndex(x);
		
		for (int i = 0; i < col.length; i++) {
			if (col[i] != null) {
				setTile(new Position(this, x, i), col[i]);
			} else {
				tiles[i][x] = null;
			}
		}
	}
	
	private void validateRowIndex(int index) {
		if (index < 0 || index >= getHeight()) {
			throw new IllegalArgumentException("Row "+index+" doesn't exist on the board");
		}
	}
	
	private void validateColumnIndex(int index) {
		if (index < 0 || index >= getWidth()) {
			throw new IllegalArgumentException("Column "+index+" doesn't exist on the board");
		}
	}
	
	private void setTile(Position position, Tile tile) {
		tiles[position.getY()][position.getX()] = tile;
		tile.setPosition(position);
	}
	
	public boolean isEmptyTile(Position position) {
		return tiles[position.getY()][position.getX()] == null;
	}
	
	public boolean containsCoords(int x, int y) {
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void addTile(Position position, Tile tile) {
		if (!isEmptyTile(position)) {
			throw new IllegalArgumentException("Cannot add tile in occupied location");
		}
		setTile(position, tile);
	}
	
	public void removeTile(Position position) {
		if (isEmptyTile(position)) {
			throw new IllegalStateException("Cannot remove tile that doesn't exist");
		}
		tiles[position.getY()][position.getX()].setPosition(null);
		tiles[position.getY()][position.getX()] = null;
	}
	
	public void removeTile(Tile tile) {
		removeTile(tile.getPosition());
	}
	
	public int getNumberOfTiles() {
		Iterator<Tile> iterator = getIterator(); 
		
		int number = 0;
		
		while (iterator.hasNext()) {
			Tile tile = iterator.next();
			if (tile != null)
				number++;
		}
		
		return number;
	}
	
	public int getNumberOfEmptyPositions() {
		return getHeight()*getWidth() - getNumberOfTiles();
	}
	
	public List<Position> getEmptyPositions() {
		List<Position> emptyPositions = new ArrayList<Position>();
		
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				Position position = new Position(this, x,y);
				if (isEmptyTile(position)) {
					emptyPositions.add(position);
				}
			}
		}
		
		return emptyPositions;
	}
	
	/*
	 * Serializes the board in the following format:
	 * {board width},{board height},{tile in row 0 col 0},{tile in row 0 col 1}, ... ,{tile in row 1 col 0}, ...
	 */
	public String serialize() {
		String serialized = String.format("%d,%d", width, height);
		
		Iterator<Tile> iterator = getIterator();
		while (iterator.hasNext()) {
			Tile tile = iterator.next();
			if (tile != null) {
				serialized = serialized.concat(String.format(",%s", tile.serialize()));
			} else {
				serialized = serialized.concat(",-");
			}
		}
		
		return serialized;
	}
	
	public String toString() {
		String result = "---------------------\n";
		for (Tile[] row : tiles) {
			for (Tile tile : row) {
				result += String.format("|%1$4s", tile != null ? tile.getDisplayText() : "");
			}
			result += "|\n---------------------\n";
		}
		return result;
	}
}
