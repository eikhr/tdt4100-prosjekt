package tjueførtiåtte.model;

import java.util.ArrayList;
import java.util.Collection;

public class GameManager implements IGameScoreListener {
	private int highScore;
	private Game game;
	
	private Collection<IHighScoreListener> highScoreListeners = new ArrayList<IHighScoreListener>();
	
	public GameManager(int loadedHighScore) {
		highScore = loadedHighScore;
	}
	
	public void startNewGame() {
		// remove old score listener from previous game
		if (game != null) {
			game.removeScoreListener(this);
		}
		
		game = new Game();
		
		game.addScoreListener(this);
	}
	
	public void startLoadedGame(Game game) {
		// remove old score listener from previous game
		if (this.game != null) {
			this.game.removeScoreListener(this);
		}
		
		this.game = game;
		
		this.game.addScoreListener(this);
	}

	public Game getGame() {
		return game;
	}
	
	private void validateGame() throws IllegalStateException {
		if (game == null) throw new IllegalStateException("No game is started");
	}

	public void continueGame() throws IllegalStateException {
		validateGame();
		
		game.continueGame();
	}
	
	/*
	 * Does a move in the direction specified. If no tiles can move in the specified direction, nothing happens
	 * @param direction The direction to move
	 * @throws IllegalStateException If no game is running or the game is in a paused state
	 */
	public void move(Direction direction) throws IllegalStateException {
		validateGame();
		
		game.move(direction);
	}

	/*
	 * Gets all the tiles from the board as objects implementing RenderableTile
	 * @throws IllegalStateException If no game is running
	 */
	public Collection<IRenderableTile> getTiles() throws IllegalStateException {
		validateGame();
		
		return game.getRenderableTiles();
	}
	
	@Override
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
		for(IHighScoreListener listener : highScoreListeners) {
			listener.highScoreUpdated(newHighScore);
		}
	}
	
	public void addHighScoreListener(IHighScoreListener listener) {
		highScoreListeners.add(listener);
	}
	
	public void removeHighScoreListener(IHighScoreListener listener) {
		highScoreListeners.remove(listener);
	}
	
	public void addGameStateListener(IGameStateListener listener) throws IllegalStateException {
		validateGame();
		
		game.addGameStateListener(listener);
	}
	
	public void removeGameStateListener(IGameStateListener listener) {
		if (getGame() == null) {
			return;
		}
		
		game.removeGameStateListener(listener);
	}
	
}
