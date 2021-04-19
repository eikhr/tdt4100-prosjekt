package tjueførtiåtte.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GhostTileTest {
	Board board;
	Position position1;
	Position position2;
	Tile tile1;
	Tile tile2;
	GhostTile ghostTile;
	
	@BeforeEach
	public void setUp() {
		board = new Board(4,4);
		position1 = new Position(board, 0, 1);
		position2 = new Position(board, 3, 0);
		tile1 = new Tile(1);
		tile1.setPosition(position1);
		tile2 = new Tile(1);
		tile2.setPosition(position2);
		tile1.startNewTurn();
		tile2.startNewTurn();
		tile2.increaseTier(); // simulerer at de blir merget
		ghostTile = new GhostTile(tile1, tile2);
	}
	
	@Test
	@DisplayName("Sjekk at konstruktør kaster unntak dersom den mangler en av de to mergede Tile-objektene")
	public void testInvalidCoords() {
		assertThrows(IllegalArgumentException.class, () -> new GhostTile(null, null));
		assertThrows(IllegalArgumentException.class, () -> new GhostTile(tile1, null));
		assertThrows(IllegalArgumentException.class, () -> new GhostTile(null, tile1));
	}
	
	@Test
	@DisplayName("Sjekk at objektet gir riktige verdier for den nåverende tilstanden")
	public void testCurrentValues() {
		assertEquals(position2, ghostTile.getPosition());
		assertEquals(2, ghostTile.getTier());
		assertEquals("4", ghostTile.getDisplayText());
	}
	
	@Test
	@DisplayName("Sjekk at objektet gir riktige verdier for tilstanden forrige tur")
	public void testPreviousValues() {
		assertEquals(position1, ghostTile.getPreviousPosition());
		assertEquals(1, ghostTile.getPreviousTier());
		assertEquals("2", ghostTile.getPreviousDisplayText());
	}
}
