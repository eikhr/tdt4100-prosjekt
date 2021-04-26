package tjueførtiåtte.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.util.Collection;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tjueførtiåtte.fxui.GameFileSupport;


public class FileSupportTest {
	private int savedScore;
	private Game savedGame;
	
	@BeforeEach
	public void init() {
		// keep the actual saved score and game, so that they can be written back to files after testing
		GameFileSupport fs = new GameFileSupport();
		try {
			savedScore = fs.readHighScore();
			savedGame = fs.readSaveGame();
		} catch (IOException e) {
			System.out.println("Could not persist saved data because of an IOException");
		}
	}
	
	@AfterEach
	public void finish() {
		// write the actual high score and saved game back to the file system
		GameFileSupport fs = new GameFileSupport();
		try {
			fs.writeHighScore(savedScore);
			fs.writeSaveGame(savedGame);
		} catch (IOException e) {
			System.out.println("Could not persist saved data because of an IOException");
		}
	}
	
	@Test
	@DisplayName("Test that reading and writing high score works as expected")
	public void testHighScore() throws IOException {
		GameFileSupport fs = new GameFileSupport();
		int testScore = new Random().nextInt();
		fs.writeHighScore(testScore);
		
		GameFileSupport fs2 = new GameFileSupport();
		assertEquals(testScore, fs2.readHighScore());
	}
	
	private Tile getTileWithCoords(Collection<Tile> tiles, int x, int y) {
		for (Tile tile : tiles) {
			Position pos = tile.getPosition();
			if (pos.getX() == x && pos.getY() == y) {
				return tile;
			}
		}
		return null;
	}
	
	@Test
	@DisplayName("Test that reading and writing game works as expected")
	public void testSaveGame() throws IOException {
		GameFileSupport fs = new GameFileSupport();
		Game testGame = new Game();
		testGame.move(Direction.UP);
		testGame.move(Direction.DOWN);
		testGame.move(Direction.LEFT);
		testGame.move(Direction.RIGHT);
		fs.writeSaveGame(testGame);
		
		GameFileSupport fs2 = new GameFileSupport();
		Game readGame = fs2.readSaveGame();
		
		assertEquals(testGame.getScore(), readGame.getScore());
		assertEquals(testGame.getState(), readGame.getState());
		
		Collection<Tile> testGameTiles = testGame.getTiles();
		Collection<Tile> readGameTiles = readGame.getTiles();
		
		assertEquals(testGameTiles.size(), readGameTiles.size());
		for(Tile testGameTile : testGameTiles) {
			Position pos = testGameTile.getPosition();
			Tile readGameEquivelant = getTileWithCoords(readGameTiles, pos.getX(), pos.getY());
			assertNotNull(readGameEquivelant);
			assertEquals(testGameTile.getScoreValue(), readGameEquivelant.getScoreValue());
			assertNull(readGameEquivelant.getPreviousPosition());
		}
		
	}
}
