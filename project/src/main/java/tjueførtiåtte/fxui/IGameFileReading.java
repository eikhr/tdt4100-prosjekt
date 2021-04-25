package tjueførtiåtte.fxui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import tjueførtiåtte.model.Game;

public interface IGameFileReading {
	/**
	 * Read a high score from a given InputStream.
	 * @param ios The input stream to read from.
	 * @return The high score from the InputStream.
	 */
	Integer readHighScore(InputStream is);
	/**
	 * Read a high score, from a default (implementation-specific) location.
	 * @return The high score
	 * @throws IOException if the high score can't be found.
	 */
	Integer readHighScore() throws IOException;
	
	/**
	 * Write a high score to a given OutputStream
	 * @param highScore The score to write
	 * @param os The stream to write to
	 */
	void writeHighScore(Integer highScore, OutputStream os);
	/**
	 * Write a high score to a file in a default (implementation specific) location.
	 * @param highScore The score to write
	 * @throws IOException If a file at the proper location can't be written to
	 */
	void writeHighScore(Integer highScore) throws IOException;

	/**
	 * Read a game from a given InputStream.
	 * @param ios The input stream to read from.
	 * @return The game from the InputStream.
	 */
	Game readSaveGame(InputStream is);
	/**
	 * Read a game, from a default (implementation-specific) location.
	 * @return The read Game
	 * @throws IOException if the game can't be found.
	 */
	Game readSaveGame() throws IOException;
	
	/**
	 * Write a game to a given OutputStream
	 * @param game The Game to be written
	 * @param os The stream to write to
	 */
	void writeSaveGame(Game game, OutputStream os);
	/**
	 * Write a high score to a file in a default (implementation specific) location.
	 * @param game The Game to be written
	 * @throws IOException If a file at the proper location can't be written to
	 */
	void writeSaveGame(Game game) throws IOException;
}
