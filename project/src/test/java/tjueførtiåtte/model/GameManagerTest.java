package tjueførtiåtte.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameManagerTest {
	GameManager manager;

	int observedScore;
	GameState observedGameState;
	Collection<RenderableTile> observedTiles;
	int observedHighScore;

	@BeforeEach
	public void SetUp() {
		observedHighScore = 0;
		observedScore = 0;
		observedGameState = null;
		observedTiles = null;
		manager = new GameManager(100);
	}
	
	@Test
	@DisplayName("Sjekk at new game fungerer")
	public void testNewGame() {
		GameStateListener listener = new GameStateListener() {
			@Override
			public void scoreUpdated(int newScore) {}
			@Override
			public void stateUpdated(GameState newState) {}
			@Override
			public void tilesMoved(Collection<RenderableTile> tiles) {
				assertTrue(false); // Burde aldri skje, fordi det er et nytt Game-objekt som skal flytte
			}
		};
		
		manager.startNewGame();
		assertNotNull(manager.getTiles());
		assertFalse(manager.getTiles().isEmpty());
		
		manager.addGameStateListener(listener);
		manager.startNewGame();
		assertNotNull(manager.getTiles());
		assertFalse(manager.getTiles().isEmpty());
		// Prøv å flytte, og se om eventen blir kjørt på det gamle Game-objektet
		manager.move(Direction.UP);
	}

	@Test
	@DisplayName("Sjekk at unntak blir kastet dersom det ikke er startet et spill")
	public void testNoGameExceptions() {
		assertThrows(IllegalStateException.class, () -> manager.move(Direction.UP));
		assertThrows(IllegalStateException.class, () -> manager.addGameStateListener(new GameStateListener() {
				@Override
				public void scoreUpdated(int newScore) {}
				@Override
				public void stateUpdated(GameState newState) {}
				@Override
				public void tilesMoved(Collection<RenderableTile> tiles) {}
			}));
		assertThrows(IllegalStateException.class, () -> manager.continueGame());
		assertThrows(IllegalStateException.class, () -> manager.getTiles());
	}

	@Test
	@DisplayName("Sjekk at lytter for high scores fungerer som den skal")
	public void testUpdateHighScoreListener() {
		HighScoreListener listener = (int score) -> observedHighScore = score;
		manager.addHighScoreListener(listener);
		
		manager.scoreUpdated(50);
		assertEquals(0, observedHighScore); // no events have been fired yet

		manager.scoreUpdated(200);
		assertEquals(200, observedHighScore);
		
		manager.removeHighScoreListener(listener);
		manager.scoreUpdated(300);
		assertEquals(200, observedHighScore);
	}
	
	@Test
	@DisplayName("Sjekk at lytter for game state fungerer som den skal")
	public void testAddGameStateListener() {
		GameStateListener listener = new GameStateListener() {
			@Override
			public void scoreUpdated(int newScore) {
				observedScore = newScore;
			}
			@Override
			public void stateUpdated(GameState newState) {
				observedGameState = newState;
			}
			@Override
			public void tilesMoved(Collection<RenderableTile> tiles) {
				observedTiles = tiles;
			}
		};
		
		assertNull(observedGameState);
		assertNull(observedTiles);
		assertEquals(0, observedScore);
		
		manager.startNewGame();
		manager.addGameStateListener(listener);
		
		// guaranteed at least one move
		manager.move(Direction.UP);
		manager.move(Direction.DOWN);
		
		// har vi hørt en event?
		assertNotNull(observedTiles);
	}
}
