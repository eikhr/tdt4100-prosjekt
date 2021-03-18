package tjueførtiåtte;

public class Tile {
	private int value;
	
	public Tile(int value) {
		this.validateValue(value);
		this.value = value;
	}

	private void validateValue(int value) {
		if (value < 1) {
			throw new IllegalArgumentException("Tile value must be at least 1 (displays as 2)");
		}
		if (value > 11) {
			throw new IllegalArgumentException("Tile value cannot be larger than 11 (displays as 2048)");
		}
	}
	
	public int getValue() {
		return value;
	}
	
	public String getDisplayValue() {
		return String.valueOf((int) Math.pow(2, value));
	}
	
	public void increaseValue() {
		value++;
	}
	
	public boolean canMergeWith(Tile other) {
		return value == other.getValue();
	}
}
