package tjueførtiåtte.fxui;

import java.io.IOException;

import tjueførtiåtte.model.Game;

public interface IGameFileReading {
	/**
	 * Read a high score, from a default (implementation-specific) location.
	 * @return The high score
	 * @throws IOException if the high score can't be found.
	 */
	Integer readHighScore() throws IOException;
	
	/**
	 * Write a high score to a file in a default (implementation specific) location.
	 * @param highScore The score to write
	 * @throws IOException If a file at the proper location can't be written to
	 */
	void writeHighScore(Integer highScore) throws IOException;
	
	/**
	 * Read a game, from a default (implementation-specific) location.
	 * @return The read Game
	 * @throws IOException if the game can't be found.
	 */
	Game readSaveGame() throws IOException;
	
	/**
	 * Write a high score to a file in a default (implementation specific) location.
	 * @param game The Game to be written
	 * @throws IOException If a file at the proper location can't be written to
	 */
	void writeSaveGame(Game game) throws IOException;
}
