package tjueførtiåtte.model;

import java.util.ArrayList;
import java.util.Collection;

public class GameManager implements GameScoreListener {
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

	public void continueGame() {
		game.continueGame();
	}
	
	public void move(Direction direction) {
		if (game.getState() != GameState.ONGOING && game.getState() != GameState.CONTINUED) {
			throw new IllegalStateException("You can only move while the game is ongoing");
		}
		
		game.move(direction);
	}
	
	public Collection<RenderableTile> getTiles() {
		return game.getRenderableTiles();
	}
	
	public void scoreUpdated(int newScore) {
		if (newScore > highScore) {
			updateHighScore(newScore);
		}
	}
	
	private void updateHighScore(int newHighScore) {
		highScore = newHighScore;
		fireHighScoreUpdated(newHighScore);
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
	
	public void addGameStateListener(GameStateListener listener) {
		game.addGameStateListener(listener);
	}
	
	public void removeGameStateListener(GameStateListener listener) {
		game.removeGameStateListener(listener);
	}
	
}
