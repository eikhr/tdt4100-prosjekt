package tjueførtiåtte.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BoardTest {
	private Board board;
	Tile tile1;
	Tile tile2;
	Tile tile3;
	Position pos1;
	Position pos2;
	Position pos3;

	@BeforeEach
	public void SetUp() {
		board = new Board(4,4);

		tile1 = new Tile(1);
		tile2 = new Tile(2);
		tile3 = new Tile(3);
		
		pos1 = new Position(board, 0, 0);
		pos2 = new Position(board, 1, 3);
		pos3 = new Position(board, 3, 3);
		
		board.addTile(pos1, tile1);
		board.addTile(pos2, tile2);
		board.addTile(pos3, tile3);
	}

	@Test
	@DisplayName("Sjekk at henting av Tile-objekter på brettet fungerer")
	public void testGetTile() {
		// sjekk at alle posisjonene på brettet stemmer
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				Position pos = new Position(board, x, y);
				if (x == pos1.getX() && y == pos1.getY()) {
					assertEquals(tile1, board.getTile(pos));
					assertEquals(tile1, board.getTile(x, y));
				} else if (x == pos2.getX() && y == pos2.getY()) {
					assertEquals(tile2, board.getTile(pos));
					assertEquals(tile2, board.getTile(x, y));
				} else if (x == pos3.getX() && y == pos3.getY()) {
					assertEquals(tile3, board.getTile(pos));
					assertEquals(tile3, board.getTile(x, y));
				} else {
					assertNull(board.getTile(pos));
					assertNull(board.getTile(x,y));
				}
			}
		}
	}
	
	@Test
	@DisplayName("Sjekk at intern state i Tile objektet blir oppdatert slik at det stemmer overens med brettet")
	public void testTilePositionState() {
		assertEquals(pos1, tile1.getPosition());
		assertEquals(pos2, tile2.getPosition());
		assertEquals(pos3, tile3.getPosition());
	}
	
	@Test
	@DisplayName("Sjekk at BoardIterator gir riktige tiles i rigtig rekkefølge")
	public void testBoardIterator() {
		Iterator<Tile> iterator = board.getIterator();
		
		int numberOfTiles = 0;
		
		while (iterator.hasNext()) {
			Tile tile = iterator.next();
			
			// Tiles skal komme fra venstre til høre, topp til bunn. De tre Tile på brettet blir dermed nr. 0, 13 og 15
			// (1: 0*4 + 0 = 0,    2: 3*4 + 1 = 13,    3: 3*4 + 3 = 15)
			if (numberOfTiles == 0)
				assertEquals(tile1, tile);
			else if (numberOfTiles == 13)
				assertEquals(tile2, tile);
			else if (numberOfTiles == 15)
				assertEquals(tile3, tile);
			else 
				assertNull(tile);
				
			numberOfTiles++;
		}
		
		assertEquals(16, numberOfTiles);
	}

	@Test
	@DisplayName("Sjekk at getTiles fungerer som den skal")
	public void testGetTiles() {
		Collection<Tile> tiles = board.getTiles();
		
		assertEquals(3, tiles.size());
		assertTrue(tiles.contains(tile1));
		assertTrue(tiles.contains(tile2));
		assertTrue(tiles.contains(tile3));
	}
	
	private void assertArrayEquals(Tile[] expected, Tile[] actual) {
		assertEquals(expected.length, actual.length);
		
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual[i]);
		}
	}

	@Test
	@DisplayName("Sjekk at getRow fungerer som den skal")
	public void testGetRow() {
		Tile[] row1 = {tile1, null, null, null};
		Tile[] row2 = {null, null, null, null};
		Tile[] row3 = {null, null, null, null};
		Tile[] row4 = {null, tile2, null, tile3};
		
		assertArrayEquals(row1, board.getRow(0));
		assertArrayEquals(row2, board.getRow(1));
		assertArrayEquals(row3, board.getRow(2));
		assertArrayEquals(row4, board.getRow(3));
	}

	@Test
	@DisplayName("Sjekk at getCol fungerer som den skal")
	public void testGetCol() {
		Tile[] col1 = {tile1, null, null, null};
		Tile[] col2 = {null, null, null, tile2};
		Tile[] col3 = {null, null, null, null};
		Tile[] col4 = {null, null, null, tile3};
		
		assertArrayEquals(col1, board.getCol(0));
		assertArrayEquals(col2, board.getCol(1));
		assertArrayEquals(col3, board.getCol(2));
		assertArrayEquals(col4, board.getCol(3));
	}

	@Test
	@DisplayName("Sjekk at setRow fungerer som den skal")
	public void testSetRow() {
		Tile[] row1Before = {tile1, null, null, null};
		Tile[] row1After = {null, null, tile1, null};
		
		assertArrayEquals(row1Before, board.getRow(0));
		
		tile1.startNewTurn();
		board.setRow(0, row1After);
		tile1.endTurn();
		
		assertArrayEquals(row1After, board.getRow(0));
		assertEquals(2, tile1.getPosition().getX());
		assertEquals(0, tile1.getPosition().getY());
	}

	@Test
	@DisplayName("Sjekk at setCol fungerer som den skal")
	public void testSetCol() {
		Tile[] col1Before = {tile1, null, null, null};
		Tile[] col1After = {null, null, tile1, null};
		
		assertArrayEquals(col1Before, board.getCol(0));
		
		// fake a new turn to avoid exception
		tile1.startNewTurn();
		board.setCol(0, col1After);
		tile1.endTurn();
		
		assertArrayEquals(col1After, board.getCol(0));
		assertEquals(0, tile1.getPosition().getX());
		assertEquals(2, tile1.getPosition().getY());
	}

	@Test
	@DisplayName("Sjekk at isEmptyTile fungerer")
	public void testIsEmptyTile() {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				Position pos = new Position(board, x, y);
				if (x == pos1.getX() && y == pos1.getY() || x == pos2.getX() && y == pos2.getY() || x == pos3.getX() && y == pos3.getY()) {
					assertFalse(board.isEmptyTile(pos));
				} else {
					assertTrue(board.isEmptyTile(pos));
				}
			}
		}
	}

	@Test
	@DisplayName("Sjekk at containsCoords fungerer som den skal")
	public void testContainsCoords() {
		assertTrue(board.containsCoords(0, 0));
		assertTrue(board.containsCoords(3, 3));
		assertTrue(board.containsCoords(0, 3));
		assertTrue(board.containsCoords(3, 0));
		assertTrue(board.containsCoords(2, 1));

		assertFalse(board.containsCoords(-1, 3));
		assertFalse(board.containsCoords(0, -1));
		assertFalse(board.containsCoords(-3, -1));
		assertFalse(board.containsCoords(4, 3));
		assertFalse(board.containsCoords(10, 10));
		
		Board smallBoard = new Board(2,2);
		assertTrue(smallBoard.containsCoords(0, 0));
		assertTrue(smallBoard.containsCoords(1, 1));
		assertFalse(smallBoard.containsCoords(0, 2));
		assertFalse(smallBoard.containsCoords(2, 0));
		

		Board longBoard = new Board(2,10);
		assertTrue(longBoard.containsCoords(0, 0));
		assertTrue(longBoard.containsCoords(1, 1));
		assertTrue(longBoard.containsCoords(9, 0));
		assertTrue(longBoard.containsCoords(9, 1));
		assertFalse(longBoard.containsCoords(0, 2));
		assertFalse(longBoard.containsCoords(10, 1));
	}

	@Test
	@DisplayName("Sjekk at getHeight og getWidth fungerer")
	public void testGetHeightWidth() {
		Board smallBoard = new Board(2,2);
		Board longBoard = new Board(2,10);
		
		assertEquals(4, board.getWidth());
		assertEquals(4, board.getHeight());
		assertEquals(2, smallBoard.getWidth());
		assertEquals(2, smallBoard.getHeight());
		assertEquals(10, longBoard.getWidth());
		assertEquals(2, longBoard.getHeight());
	}

	@Test
	@DisplayName("Sjekk at addTile kaster et unntak når man prøver å legge til en Tile oppå en annen")
	public void testAddTileOccupied() {
		Tile newTile = new Tile(1);
		
		assertThrows(IllegalArgumentException.class, () -> board.addTile(pos1, newTile));
	}
	
	@Test
	@DisplayName("Sjekk at henting fra ugyldig posisjon kaster unntak")
	public void testGetInvalidCoords() {
		assertThrows(IllegalArgumentException.class, () -> board.getTile(0,-1));
		assertThrows(IllegalArgumentException.class, () -> board.getTile(5,0));
		assertThrows(IllegalArgumentException.class, () -> board.getTile(4,10));
	}
	

	@Test
	@DisplayName("Sjekk at henting av beste Tile fungerer")
	public void testGetBestTile() {
		assertEquals(tile3, board.getBestTile());
	}
	
	@Test
	@DisplayName("Sjekk at serialisering og deserialisering fungerer")
	public void testSerialization() {
		String serialized = board.serialize();
		
		System.out.println(serialized);
		Board newBoard = new Board(serialized);
		
		assertEquals(board.getWidth(), newBoard.getWidth());
		assertEquals(board.getHeight(), newBoard.getHeight());
		for (int x = 0; x < board.getWidth(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				Tile original = board.getTile(x, y);
				Tile copy = newBoard.getTile(x, y);
				
				if (original == null) {
					assertNull(copy);
				} else {
					assertEquals(original.getTier(), copy.getTier());
					assertEquals(original.getDisplayText(), copy.getDisplayText());
					assertEquals(original.getPosition().getX(), copy.getPosition().getX());
					assertEquals(original.getPosition().getY(), copy.getPosition().getY());
				}
			}
		}
	}
}
