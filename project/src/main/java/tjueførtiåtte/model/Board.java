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
	
	public Tile getTile(int x, int y) {
		return tiles[y][x];
	}
	
	public Iterator<Tile> getIterator() {
		return new BoardIterator(this);
	}
	
	public Collection<Tile> getTiles() {
		Collection<Tile> tiles = new ArrayList<Tile>();

		Iterator<Tile> iterator = getIterator();
		iterator.forEachRemaining(tiles::add);
		
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
		tiles[y] = row;
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
			tiles[i][x] = col[i];
		}
	}
	
	public boolean isEmptyTile(int x, int y) {
		return tiles[y][x] == null;
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
	
	public void addTile(int x, int y, Tile tile) {
		if (!isEmptyTile(x, y)) {
			throw new IllegalStateException("Cannot add tile in occupied location");
		}
		tiles[y][x] = tile;
	}
	
	public void removeTile(int x, int y) {
		if (isEmptyTile(x, y)) {
			throw new IllegalStateException("Cannot remove tile that doesn't exist");
		}
		tiles[y][x] = null;
	}
	
	public void removeTile(Tile tile) {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (tiles[y][x] == tile) {
					tiles[y][x] = null;
				}
			}
		}
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
	
	public Coordinates getPositionOfTile(Tile tile) {		
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (getTile(x, y) == tile) {
					return new Coordinates(x, y);
				}
			}
		}
		
		throw new IllegalArgumentException("This tile is not on the board");
	}
	
	public List<int[]> getEmptyPositions() {
		List<int[]> emptyPositions = new ArrayList<int[]>();
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (tiles[y][x] == null) {
					int pos[] = {x,y};
					emptyPositions.add(pos);
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
