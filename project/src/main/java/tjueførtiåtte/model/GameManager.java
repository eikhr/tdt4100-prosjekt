package tjueførtiåtte.model;

public class GameManager {
	private int highScore;
	private Game game;
	
	public GameManager() {
		highScore = 1337; // TODO: load high score from persistent storage
		startNewGame();
	}
	
	public void startNewGame() {
		game = new Game(this);
	}
	
	public int getHighScore() {
		return highScore;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void onScoreUpdated() {
		int score = game.getScore();
		if (score > highScore) {
			highScore = score;
		}
	}
}
