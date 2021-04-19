package tjueførtiåtte.model;

import java.util.ArrayList;
import java.util.Collection;

public class GameManager implements GameScoreListener{
	private int highScore;
	private Game game;
	
	private Collection<HighScoreListener> highScoreListeners = new ArrayList<HighScoreListener>();
	
	public GameManager(int loadedHighScore) {
		highScore = loadedHighScore;
		startNewGame();
	}
	
	public void startNewGame() {
		// remove old score listener from previous game
		if (game != null) {
			game.removeScoreListener(this);
		}
		
		game = new Game();
		
		game.addScoreListener(this);
	}
	
	public int getHighScore() {
		return highScore;
	}
	
	private void updateHighScore(int newHighScore) {
		highScore = newHighScore;
		fireHighScoreUpdated(newHighScore);
	}
	
	public Game getGame() {
		return game;
	}
	
	public void gameScoreUpdated(int newScore) {
		if (newScore > highScore) {
			updateHighScore(newScore);
		}
	}
	
	private void fireHighScoreUpdated(int newHighScore) {
		for(HighScoreListener listener : highScoreListeners) {
			listener.highScoreUpdated(newHighScore);
		}
	}
	
	public void addHighScoreListener(HighScoreListener listener) {
		highScoreListeners.add(listener);
	}
	
	public void removeHighScoreListener(HighScoreListener listener) {
		highScoreListeners.remove(listener);
	}
}
