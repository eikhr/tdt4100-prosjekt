package tjueførtiåtte.model;

import java.util.Collection;
import java.util.HashSet;

public class Tile {
	private int tier;
	private Board board;
	
	private Coords previousPosition;
	
	public Tile(Board board, int tier) {
		this.board = board;
		this.validateTier(tier);
		this.tier = tier;
	}
	
	public void setPreviousPosition(Coords coords) {
		previousPosition = coords;
	}
	
	public Coords getPreviousPosition() {
		return previousPosition;
	}

	private void validateTier(int tier) {
		if (tier < 1) {
			throw new IllegalArgumentException("Tile tier must be at least 1 (displays as 2)");
		}
		if (tier > 11) {
			throw new IllegalArgumentException("Tile tier cannot be larger than 11 (displays as 2048)");
		}
	}
	
	public Coords getPosition() {
		return board.getPositionOfTile(this);
	}
	
	/*
	public Collection<Tile> getNeighbors() {
		Coords pos = getPosition();
		Collection<Tile> neighbors = new HashSet<Tile>();
		
		if (board.containsPosition(pos.getX(), pos.getY()-1))
			neighbors.add(board.getTile(pos.getX(), pos.getY()-1));
		if (board.containsPosition(pos.getX(), pos.getY()+1))
			neighbors.add(board.getTile(pos.getX(), pos.getY()+1));
		if (board.containsPosition(pos.getX()-1, pos.getY()))
			neighbors.add(board.getTile(pos.getX()-1, pos.getY()));
		if (board.containsPosition(pos.getX()+1, pos.getY()))
			neighbors.add(board.getTile(pos.getX()+1, pos.getY()));
		
		return neighbors;
	}*/
	
	public int getTier() {
		return tier;
	}
	
	public int getValue() {
		return (int) Math.pow(2, tier);
	}
	
	public String getDisplayText() {
		return String.valueOf(getValue());
	}
	
	public void increaseValue() {
		tier++;
	}
	
	public boolean canMergeWith(Tile other) {
		return tier == other.getTier();
	}
}
