package tjueførtiåtte.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameManagerTest {
	GameManager manager;
	int observedHighScore;

	@BeforeEach
	public void SetUp() {
		observedHighScore = 0;
		manager = new GameManager(100);
	}
	
	@Test
	@DisplayName("Sjekk at konstruktør setter opp objektet riktig")
	public void testConstructor() {
		assertNotNull(manager.getGame());
		assertEquals(100, manager.getHighScore());
	}
	
	@Test
	@DisplayName("Sjekk at new game fungerer")
	public void testNewGame() {
		Game firstGame = manager.getGame();
		manager.startNewGame();
		assertNotEquals(firstGame, manager.getGame());
		assertNotNull(manager.getGame());
	}
	
	@Test
	@DisplayName("Sjekk at oppdatering av high score fungerer")
	public void testUpdateHighScore() {
		manager.gameScoreUpdated(50);
		assertEquals(100, manager.getHighScore());

		manager.gameScoreUpdated(200);
		assertEquals(200, manager.getHighScore());
	}

	@Test
	@DisplayName("Sjekk at lytter for high scores fungerer som den skal")
	public void testUpdateHighScoreListener() {
		HighScoreListener listener = (int score) -> observedHighScore = score;
		manager.addHighScoreListener(listener);
		
		manager.gameScoreUpdated(50);
		assertEquals(0, observedHighScore); // no events have been fired yet

		manager.gameScoreUpdated(200);
		assertEquals(200, observedHighScore);
		
		manager.removeHighScoreListener(listener);
		manager.gameScoreUpdated(300);
		assertEquals(200, observedHighScore);
	}
}
