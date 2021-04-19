package tjueførtiåtte.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PositionTest {
	
	@Test
	@DisplayName("Sjekk at konstruktør setter opp Tile-objektet riktig")
	public void testValid() {
		Board board = new Board(4,4);
		Position position1 = new Position(board, 0, 1);
		Position position2 = new Position(board, 3, 3);
		
		assertEquals(0, position1.getX());
		assertEquals(1, position1.getY());
		assertEquals(board, position1.getBoard());
		
		assertEquals(3, position2.getX());
		assertEquals(3, position2.getY());
		assertEquals(board, position2.getBoard());
	}

	@Test
	@DisplayName("Sjekk at konstruktør kaster et unntak dersom det blir satt opp koordinater utenfor brettet")
	public void testInvalidCoords() {
		Board board = new Board(2,2);
		assertThrows(IllegalArgumentException.class, () -> new Position(board, -1, 1));
		assertThrows(IllegalArgumentException.class, () -> new Position(board, 3, 3));
	}
	
	@Test
	@DisplayName("Sjekk at konstruktør kaster et unntak dersom det blir satt opp uten et brett")
	public void testNoBoard() {
		assertThrows(IllegalArgumentException.class, () -> new Position(null, 0, 0));
	}

}
