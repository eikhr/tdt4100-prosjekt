package tjueførtiåtte.model;

public class Tile implements RenderableTile {
	private int tier;
	private int previousTier = 0;
	private Board board;
	private Position previousPosition;
	
	public Tile(Board board, int tier) {
		this.board = board;
		this.validateTier(tier);
		this.tier = tier;
	}
	
	public void setPrevious(Position position) {
		previousPosition = position;
		previousTier = tier;
	}
	
	private void validateTier(int tier) {
		if (tier < 1) {
			throw new IllegalArgumentException("Tile tier must be at least 1 (displays as 2)");
		}
		if (tier > 11) {
			throw new IllegalArgumentException("Tile tier cannot be larger than 11 (displays as 2048)");
		}
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

	public void increaseValue() {
		tier++;
	}
	
	public boolean canMergeWith(Tile other) {
		return tier == other.getTier();
	}
	
	public int getScoreValue() {
		return (int) Math.pow(2, tier);
	}
	
	@Override
	public Position getPosition() {
		return board.getPositionOfTile(this);
	}
	
	@Override
	public Position getPreviousPosition() {
		return previousPosition;
	}

	@Override
	public int getTier() {
		return tier;
	}
	
	@Override
	public int getPreviousTier() {
		return previousTier;
	}

	@Override
	public String getDisplayText() {
		return String.valueOf((int) Math.pow(2, getTier()));
	}
	
	@Override
	public String getPreviousDisplayText() {
		return String.valueOf((int) Math.pow(2, getPreviousTier()));
	}
}
