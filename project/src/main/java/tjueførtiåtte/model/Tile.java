package tjueførtiåtte.model;

public class Tile {
	private int tier;
	
	public Tile(int tier) {
		this.validateTier(tier);
		this.tier = tier;
	}

	private void validateTier(int tier) {
		if (tier < 1) {
			throw new IllegalArgumentException("Tile tier must be at least 1 (displays as 2)");
		}
		if (tier > 11) {
			throw new IllegalArgumentException("Tile tier cannot be larger than 11 (displays as 2048)");
		}
	}
	
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
