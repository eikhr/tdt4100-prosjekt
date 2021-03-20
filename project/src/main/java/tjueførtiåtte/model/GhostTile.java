package tjueførtiåtte.model;

public class GhostTile extends Tile {
	private Tile mergedInto;
	
	public GhostTile(Tile tile, Tile mergedInto) {
		super(tile.board, tile.tier);
		
		this.mergedInto = mergedInto;
		super.previousPosition = tile.previousPosition;
		super.previousTier = tile.previousTier;
	}
	
	@Override
	public Coords getPosition() {
		return mergedInto.getPosition();
	}
}
