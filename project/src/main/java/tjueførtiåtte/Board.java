package tjueførtiåtte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Board {
	private Tile[][] tiles;
	
	public Board() {
		tiles = new Tile[4][4];
	}
	
	public Tile getTile(int x, int y) {
		return tiles[y][x];
	}
	
	public boolean isEmptyTile(int x, int y) {
		return tiles[y][x] == null;
	}
	
	public boolean containsPosition(int x, int y) {
		return x >= 0 && x < 4 && y >= 0 && y < 4;
	}
	
	public int getWidth() {
		return 4;
	}
	
	public int getHeight() {
		return 4;
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
	
	public List<int[]> getEmptyPositions() {
		List<int[]> emptyPositions = new ArrayList<int[]>();
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (tiles[x][y] == null) {
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
				result += String.format("|%1$4s", tile != null ? tile.getDisplayValue() : "");
			}
			result += "|\n---------------------\n";
		}
		return result;
	}
}
