package tjueførtiåtte.model;

public class Tile implements RenderableTile {
	private int tier;
	private int previousTier;
	private Position position;
	private Position previousPosition;
	
	public Tile(int tier) {
		this.validateTier(tier);
		this.tier = tier;
		this.previousTier = tier;
	}
	
	private void validateTier(int tier) {
		if (tier < 1) {
			throw new IllegalArgumentException("Tile tier must be at least 1 (displays as 2)");
		}
	}
	
	/*
	 * Sets the current position of the tile on the board
	 * @param Position position 
	 */
	public void setPosition(Position position) {
		this.position = position;
	}
	
	/*
	 * Sets the current position of the tile on the board
	 * @param Position position 
	 */
	public void startNewTurn() {
		previousPosition = position;
		previousTier = tier;
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

	public void increaseTier() {
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
		return position;
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
