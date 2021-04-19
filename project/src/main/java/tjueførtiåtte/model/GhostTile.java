package tjueførtiåtte.model;

/*
 * A RenderableTile implementation for tiles that don't exist anymore as they have merged into another tile.
 * Used for rendering the animation of the tile merging.
 */
public class GhostTile implements RenderableTile {
	private Tile mergedInto;
	
	private Position previousPosition;
	
	private int previousTier;
	
	public GhostTile(Tile tile, Tile mergedInto) {
		validateTile(tile);
		validateTile(mergedInto);
		
		this.mergedInto = mergedInto;
		previousPosition = tile.getPreviousPosition();
		previousTier = tile.getPreviousTier();
	}
	
	private void validateTile(Tile tile) {
		if (tile == null) throw new IllegalArgumentException("Both the tile, and the tile that is merged into must not be null");
	}
	
	@Override
	public Position getPosition() {
		return mergedInto.getPosition();
	}

	@Override
	public Position getPreviousPosition() {
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
