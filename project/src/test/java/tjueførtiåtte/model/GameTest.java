package tjueførtiåtte.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameTest {
	Game game;
	
	@BeforeEach
	public void SetUp() {
		game = new Game();
	}
	
	@Test
	@DisplayName("Sjekk at konstruktør setter opp Game-objektet riktig")
	public void testConstructor() {
		assertEquals(GameState.ONGOING, game.getState());
		assertEquals(4, game.getBoardHeight());
		assertEquals(4, game.getBoardWidth());
		assertEquals(2, game.getNumberOfTiles());
		assertTrue(game.getGhostTiles().isEmpty());
		assertEquals(0, game.getScore());
		
		Collection<Tile> tiles = game.getTiles();
		assertEquals(2, tiles.size());
		assertTileTiersAreCorrect(tiles);
	}
	
	@RepeatedTest(10)
	@DisplayName("Test that generated tiles are only tier 1 and 2")
	public void testGeneratedTileTiers() {
		Collection<Tile> tiles = game.getTiles();
		assertEquals(2, tiles.size());
		assertTileTiersAreCorrect(tiles);
	}
	
	private void assertTileTiersAreCorrect(Collection<Tile> tiles) {
		for (Tile tile : tiles) {
			assertTrue(tile.getTier() == 1 || tile.getTier() == 2);
		}
	}
	
	private boolean tileIsBlocked(Game game, Tile tile, Direction direction) {
		int testX = 0;
		int testY = 0;
		Position tilePosition = tile.getPosition();
		switch (direction) {
			case UP:
				testX = tilePosition.getX();
				testY = tilePosition.getY()-1;
				break;
			case DOWN:
				testX = tilePosition.getX();
				testY = tilePosition.getY()+1;
				break;
			case LEFT:
				testX = tilePosition.getX()-1;
				testY = tilePosition.getY();
				break;
			case RIGHT:
				testX = tilePosition.getX()+1;
				testY = tilePosition.getY();
				break;
		}
		
		if (testX < 0 || testX >= game.getBoardWidth()) return true;
		if (testY < 0 || testY >= game.getBoardHeight()) return true;
		
		if (game.boardPositionHasTile(new Position(tilePosition.getBoard(), testX, testY))) return true;
		
		System.out.println(game);
		return false;
	}
	
	private void assertNonNewTilesAreBlocked(Game game, Direction direction) {
		for (Tile tile : game.getTiles()) {
			if (tile.getPreviousTier() != 0)
				assertTrue(tileIsBlocked(game, tile, direction));
		}
	}
	
	@RepeatedTest(10)
	@DisplayName("Sjekk at move metoden flytter tiles på rett måte")
	public void testMove() {
		game.move(Direction.UP);
		assertNonNewTilesAreBlocked(game, Direction.UP);
		
		game.move(Direction.RIGHT);
		assertNonNewTilesAreBlocked(game, Direction.RIGHT);

		game.move(Direction.DOWN);
		assertNonNewTilesAreBlocked(game, Direction.DOWN);

		game.move(Direction.LEFT);
		assertNonNewTilesAreBlocked(game, Direction.LEFT);
	}
	
	/*
	 * Lager brett helt til det kommer et der to Tiles med tier 1 kan merges ved å flytte oppover
	 */
	private Game getMergableGame() {
		while (true) {
			Game game = new Game();
			game.move(Direction.LEFT);
			
			boolean good = true;
			for (Tile tile : game.getTiles()) {
				if (tile.getPreviousTier() == 0) {
					if (tile.getPosition().getX() == 0)
						good = false;
				} else {
					if (tile.getPosition().getX() != 0)
						good = false;
					if (tile.getTier() != 1)
						good = false;
				}
			}
			
			if (good) {
				return game;
			}
		}
	}

	@RepeatedTest(10)
	@DisplayName("Sjekk at merging fungerer")
	public void testMerge() {
		game = getMergableGame();
				
		game.move(Direction.UP);
		assertNonNewTilesAreBlocked(game, Direction.UP);
		
		// finn Tile øverst i venstre hjørne, og sjekk at tier har økt til 2
		boolean mergedTileCorrect = false;
		for (Tile tile : game.getTiles()) {
			if (tile.getPosition().getX() == 0 && tile.getPosition().getY() == 0) {
				if (tile.getTier() == 2) 
					mergedTileCorrect = true;
			}
		}
		assertTrue(mergedTileCorrect);
		
		// det ble lagt til en ghost tile fra den som ble merget
		assertEquals(1, game.getGhostTiles().size());
	}
	
	@RepeatedTest(10)
	@DisplayName("Sjekk at game state blir oppdatert når spillet tapes (eller kanskje vinnes)")
	public void testStateEnd() {
		int moves = 0;
		// flytt i sikrler til spillet vinnes eller tapes (etter 1000 trekk uten å tape antar vi at noe er feil)
		while(game.getState() == GameState.ONGOING) {
			game.move(Direction.values()[moves % 4]);
			moves++;
			
			assertTrue(moves < 1000);
		}
	}
}
