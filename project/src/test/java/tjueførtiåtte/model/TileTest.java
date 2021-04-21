package tjueførtiåtte.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class TileTest {
	
	Tile tile1;
	Tile tile2;
	
	@BeforeEach
	public void SetUp() {
		tile1 = new Tile(1);
		tile2 = new Tile(2);
	}
	
	@Test
	@DisplayName("Sjekk at konstruktør setter opp Tile-objektet riktig")
	public void testConstructor() {
		assertNull(tile1.getPosition());
		assertNull(tile2.getPosition());
		assertNull(tile1.getPreviousPosition());
		assertNull(tile2.getPreviousPosition());
		assertEquals(1, tile1.getTier());
		assertEquals(2, tile2.getTier());
		assertEquals(0, tile1.getPreviousTier());
		assertEquals(0, tile2.getPreviousTier());
	}
	
	@Test
	@DisplayName("Sjekk at Tile gir rette score-verdier")
	public void testScoreValue() {
		assertEquals(2, tile1.getScoreValue());
		assertEquals(4, tile2.getScoreValue());
		
		for (int i = 2; i < 20; i++) {
			tile1.increaseTier();
			assertEquals((int) Math.pow(2, i), tile1.getScoreValue());
		}
	}
	
	@Test
	@DisplayName("Sjekk at Tile gir rette displayText-tekststrenger")
	public void testDisplayText() {
		assertEquals("2", tile1.getDisplayText());
		assertEquals("4", tile2.getDisplayText());
		
		for (int i = 2; i < 20; i++) {
			tile1.increaseTier();
			assertEquals(String.valueOf((int) Math.pow(2, i)), tile1.getDisplayText());
		}
	}
	
	@Test
	@DisplayName("Sjekk at canMerge metoden på Tile fungerer riktig")
	public void testMergeCheck() {
		assertFalse(tile1.canMergeWith(tile2));
		assertFalse(tile2.canMergeWith(tile1));
		tile1.increaseTier();
		assertTrue(tile1.canMergeWith(tile2));
		assertTrue(tile2.canMergeWith(tile1));
		tile1.increaseTier();
		assertFalse(tile1.canMergeWith(tile2));
		assertFalse(tile2.canMergeWith(tile1));
	}

	@Test
	@DisplayName("Sjekk at setPosition og getPosition funker som det skal")
	public void testPosition() {
		assertNull(tile1.getPosition());
		Board board = new Board(2,2);
		Position position1 = new Position(board, 0, 1);
		Position position2 = new Position(board, 1, 1);
		
		tile1.startNewTurn();
		tile1.setPosition(position1);
		tile1.endTurn();
		
		assertEquals(position1, tile1.getPosition());
		assertNotEquals(position2, tile1.getPosition());
		
		tile1.startNewTurn();
		tile1.setPosition(position2);
		tile1.endTurn();
		
		assertEquals(position2, tile1.getPosition());
		assertNotEquals(position1, tile1.getPosition());
	}

	@Test
	@DisplayName("Sjekk at funksjonen for å hente verdier fra forrige tur fungerer")
	public void testPrevious() {
		Board board = new Board(2,2);
		Position position1 = new Position(board, 0, 1);
		Position position2 = new Position(board, 1, 1);
		tile1.setPosition(position1);
		
		tile1.startNewTurn();
		
		tile1.setPosition(position2);
		tile1.increaseTier();

		tile1.endTurn();
		
		assertEquals(position1, tile1.getPreviousPosition());
		assertEquals(1, tile1.getPreviousTier());
		assertEquals("2", tile1.getPreviousDisplayText());

		tile1.startNewTurn();
		tile1.endTurn();
		
		assertEquals(position2, tile1.getPreviousPosition());
		assertEquals(2, tile1.getPreviousTier());
		assertEquals("4", tile1.getPreviousDisplayText());
	}
	
	@SuppressWarnings("unused")
	@Test
	@DisplayName("Sjekk at konstruktør kaster IllegalArgumentException ved ugyldig tier")
	public void testInvalidTier() {
		assertThrows(IllegalArgumentException.class, () -> {
			Tile invalid = new Tile(0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Tile invalid = new Tile(-1);
		});
	}
	
	@Test
	@DisplayName("Sjekk at den statiske funksjonen for å reversere en array med Tiles fungerer")
	public void testReverseRow() {
		Tile[] tileArray = {tile1, tile2};
		
		tileArray = Tile.reverseRow(tileArray);
		
		assertEquals(tile2, tileArray[0]);
		assertEquals(tile1, tileArray[1]);
	}
	
	@Test
	@DisplayName("Sjekk at den statiske funksjonen for å sjekke om to arrays av Tiles er like fungerer")
	public void testRowsEqual() {
		Tile[] tileArray1 = {tile1, tile2};
		Tile[] tileArray2 = {tile2, tile1};
		Tile[] shortTileArray = {tile2};
		
		assertFalse(Tile.areEqualRows(tileArray1, tileArray2));
		
		tileArray2[0] = tile1;
		tileArray2[1] = tile2;
		
		assertTrue(Tile.areEqualRows(tileArray1, tileArray2));
		
		assertThrows(IllegalArgumentException.class, () -> Tile.areEqualRows(null, tileArray1));
		assertThrows(IllegalArgumentException.class, () -> Tile.areEqualRows(shortTileArray, tileArray1));
	}
}
