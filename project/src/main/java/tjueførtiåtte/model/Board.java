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
	
	public Tile getTile(Position position) {
		return tiles[position.getY()][position.getX()];
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
		for (int i = 0; i < row.length; i++) {
			setTile(new Position(i, y), row[i]);
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
			setTile(new Position(x, i), col[i]);
		}
	}
	
	private void setTile(Position position, Tile tile) {
		tiles[position.getY()][position.getX()] = tile;
		//tile.setPostion(position);
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
		//tiles[position.getY()][position.getX()].setPosition(null);
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
				Position position = new Position(x, y);
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
				Position position = new Position(x,y);
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
