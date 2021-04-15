package tjueførtiåtte.model;

public class GameManager implements GameScoreListener{
	private int highScore;
	private Game game;
	
	public GameManager() {
		highScore = 1337; // TODO: load high score from persistent storage
		startNewGame();
	}
	
	public void startNewGame() {
		// remove old score listener from previous game
		if (game != null) {
			game.removeScoreListener(this);
		}
		
		game.addScoreListener(this);
	}
	
	public int getHighScore() {
		return highScore;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void gameScoreUpdated(int newScore) {
		int score = game.getScore();
		if (score > highScore) {
			highScore = score;
		}
	}
}
