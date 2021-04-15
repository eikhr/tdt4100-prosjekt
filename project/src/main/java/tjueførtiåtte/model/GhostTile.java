package tjueførtiåtte.model;

public class GhostTile implements RenderableTile {
	private Tile mergedInto;
	
	private Coordinates previousPosition;
	
	private int previousTier;
	
	public GhostTile(Tile tile, Tile mergedInto) {
		this.mergedInto = mergedInto;
		previousPosition = tile.getPreviousPosition();
		previousTier = tile.getPreviousTier();
	}
	
	@Override
	public Coordinates getPosition() {
		return mergedInto.getPosition();
	}

	@Override
	public Coordinates getPreviousPosition() {
		return previousPosition;
	}

	@Override
	public int getTier() {
		return mergedInto.getTier();
	}

	@Override
	public int getPreviousTier() {
		return previousTier;
	}

	@Override
	public String getDisplayText() {
		return mergedInto.getDisplayText();
	}

	@Override
	public String getPreviousDisplayText() {
		return Integer.toString((int) Math.pow(2, getPreviousTier()));
	}
}
