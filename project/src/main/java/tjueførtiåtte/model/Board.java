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
		return tiles[y];
	}
	
	public void setRow(int y, Tile[] row) {
		for (int i = 0; i < row.length; i++) {
			if (row[i] != null) {
				setTile(new Position(this, i, y), row[i]);
			} else {
				tiles[y][i] = null;
			}
		}
	}
	
	public Tile[] getCol(int x) {
		Tile col[] = new Tile[getHeight()];
		for (int i = 0; i < tiles.length; i++) {
			col[i] = tiles[i][x];
		}
		return col;
	}
	
	public void setCol(int x, Tile[] col) {
		for (int i = 0; i < col.length; i++) {
			if (col[i] != null) {
				setTile(new Position(this, x, i), col[i]);
			} else {
				tiles[i][x] = null;
			}
		}
	}
	
	private void setTile(Position position, Tile tile) {
		tiles[position.getY()][position.getX()] = tile;
		tile.setPosition(position);
	}
	
	public boolean isEmptyTile(Position position) {
		return tiles[position.getY()][position.getX()] == null;
	}
	
	public boolean containsPosition(int x, int y) {
		return x >= 0 && x < 4 && y >= 0 && y < 4;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void addTile(Position position, Tile tile) {
		if (!isEmptyTile(position)) {
			throw new IllegalStateException("Cannot add tile in occupied location");
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
		Position position = getPositionOfTile(tile);
		removeTile(position);
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
	
	public Position getPositionOfTile(Tile tile) {		
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				Position position = new Position(this, x, y);
				if (getTile(position) == tile) {
					return position;
				}
			}
		}
		
		throw new IllegalArgumentException("This tile is not on the board");
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
