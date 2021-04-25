package tjueførtiåtte.model;

public class Tile implements RenderableTile {
	private int tier = 0;
	private int previousTier = 0;
	private Position position;
	private Position previousPosition;
	private boolean turnInProgress;
	
	public Tile(int tier) throws IllegalArgumentException {
		this.validateTier(tier);
		this.tier = tier;
		this.previousTier = 0;
	}
	
	public Tile(String serialized) throws IllegalArgumentException {
		try {
			int tier = Integer.valueOf(serialized);
			this.validateTier(tier);
			this.tier = tier;
			this.previousTier = 0;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("serialized Tile must be a valid number, null Tiles cannot be deserialized");
		}
	}
	
	private void validateTier(int tier) throws IllegalArgumentException {
		if (tier < 1) {
			throw new IllegalArgumentException("Tile tier must be at least 1 (displays as 2)");
		}
	}
	
	/*
	 * Sets the current position of the tile on the board
	 * @param Position position 
	 */
	public void setPosition(Position position) throws IllegalStateException {
		if (this.position != null && !turnInProgress) {
			throw new IllegalStateException("Position can only be updated during a turn");
		}
		
		this.position = position;
	}
	
	/*
	 * Sets the current position of the tile on the board
	 * @param Position position 
	 */
	public void startNewTurn() {
		previousPosition = position;
		previousTier = tier;
		turnInProgress = true;
	}
	
	public void endTurn() {
		turnInProgress = false;
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
	public Position getPosition() throws IllegalStateException {
		if (turnInProgress) throw new IllegalStateException("Tile position is not accessible while a turn is in progress");
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
	
	public static Tile[] reverseRow(Tile row[]) throws IllegalArgumentException {
		if (row == null) throw new IllegalArgumentException("Cannot reverse null row");
		
		Tile newRow[] = new Tile[row.length];
		
		for (int i = 0; i < row.length; i++) {
			newRow[i] = row[row.length - 1 - i];
		}
		
		return newRow;
	}
	
	public static boolean areEqualRows(Tile line1[], Tile line2[]) throws IllegalArgumentException {
		if (line1 == null || line2 == null) throw new IllegalArgumentException("Cannot compare null rows");
		if (line1.length != line2.length) throw new IllegalArgumentException("Cannot compare rows of different length");
		
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
	
	public String serialize() throws IllegalStateException {
		if (turnInProgress) throw new IllegalStateException("Cannot serialize during a turn");
		
		return String.valueOf(tier);
	}
}
