package tjueførtiåtte.model;

public interface IRenderableTile {
	/**
	 * Gets the position of the tile.
	 * @return The Coordinates for the tile's position.
	 */
	public Position getPosition() throws IllegalStateException;

	/**
	 * Gets the position where the tile was the previous turn.
	 * @return The Coordinates for the tile's previous position.
	 */
	public Position getPreviousPosition();
	
	/**
	 * Gets the tier of the tile.
	 * @return int corresponding to the tile's tier.
	 */
	public int getTier();

	/**
	 * Gets the tier the tile has the previous turn.
	 * @return int corresponding to the tile's previous tier.
	 */
	public int getPreviousTier();

	/**
	 * Gets the text that should be displayed on the tile.
	 * @return display text of the tile.
	 */
	public String getDisplayText();

	/**
	 * Gets the text that was displayed on the tile the previous turn.
	 * @return previous display text of the tile.
	 */
	public String getPreviousDisplayText();
}
