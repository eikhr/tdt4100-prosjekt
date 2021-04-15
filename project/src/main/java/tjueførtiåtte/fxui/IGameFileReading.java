package tjueførtiåtte.fxui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IGameFileReading {
	/**
	 * Read a TodoList from a given InputStream.
	 * @param ios The input stream to read from.
	 * @return The TodoList from the InputStream.
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
}
