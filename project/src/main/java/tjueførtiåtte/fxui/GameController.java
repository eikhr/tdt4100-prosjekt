package tjueførtiåtte.fxui;

import java.util.ArrayList;
import java.util.Collection;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import tjueførtiåtte.model.Direction;
import tjueførtiåtte.model.GameManager;
import tjueførtiåtte.model.GameState;
import tjueførtiåtte.model.GameStateListener;
import tjueførtiåtte.model.HighScoreListener;
import tjueførtiåtte.model.RenderableTile;

public class GameController implements HighScoreListener, GameStateListener {
	private GameManager gameManager;
	
	@FXML Pane gamePane;
	
	@FXML GridPane overlayPane;
	
	@FXML Label scoreText;
	
	@FXML Label highScoreText;
	
	private final IGameFileReading fileSupport = new GameFileSupport();
	
	private TileGenerator tileGenerator;
	
	private Collection<RenderableTile> tiles = new ArrayList<RenderableTile>();
	
	private static int gameWidth = 4;
	private static int gameHeight = 4;
	
	@FXML
	private void initialize() {
		int highScore;
		try {
			highScore = fileSupport.readHighScore();
		} catch (Throwable e) {
			highScore = 0;
		}
		
		gameManager = new GameManager(highScore);
		gameManager.addHighScoreListener(this);
		resizedUI();
		startNewGame();
		gamePane.requestFocus();
	}
	
	private void startNewGame() {
		clearOverlay();
		gameManager.startNewGame();
		gameManager.addGameStateListener(this);
		tilesMoved(gameManager.getTiles());
	}
	
	@FXML
	private void onNewGameClick() {
		gameManager.removeGameStateListener(this);
		startNewGame();
		gamePane.requestFocus();
	}
	
	private void onKeepPlayingClick() {
		gameManager.continueGame();
		gamePane.requestFocus();
	}

	@FXML
	private void keyPressed(KeyEvent keyEvt) {
		switch (keyEvt.getCode()) {
		case UP:
		case W:
			doMove(keyEvt, Direction.UP);
			break;
		case DOWN:
		case S:
			doMove(keyEvt, Direction.DOWN);
			break;
		case LEFT:
		case A:
			doMove(keyEvt, Direction.LEFT);
			break;
		case RIGHT:
		case D:
			doMove(keyEvt, Direction.RIGHT);
			break;
		default:
			break;
		}
	}
	
	private void doMove(KeyEvent keyEvt, Direction direction) {
		gameManager.move(direction);
		// TODO: handle exception
		keyEvt.consume();
	}
	
	@FXML
	private void gameClicked(MouseEvent evt) {
		gamePane.requestFocus();
	}
	
	public void resizedUI() {
		tileGenerator = new TileGenerator(gameWidth, gameHeight, gamePane.getWidth(), gamePane.getHeight());
		drawTiles(false);
	}
	
	// add overlay, either won or lost
	private void addOverlay(boolean won) {
		overlayPane.getChildren().clear();
		overlayPane.setStyle(won ? "-fx-background-color: #edc40044; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(237, 196, 0, 0.8), 40, 0, 0, 0);" : "-fx-background-color: #00000044; -fx-background-radius: 15;");
		
		
		StackPane textPane = new StackPane();
		Label text = new Label();
		text.setText(won ? "You Won!" : "You lost!");
		text.setTextFill(Color.WHITE);
		text.setFont(new Font(40));
		textPane.getChildren().add(text);
		textPane.setAlignment(Pos.BOTTOM_CENTER);
		
		overlayPane.add(textPane, 0, 0);
		
		StackPane buttonPane = new StackPane();
		Button button = new Button();
		button.setText(won ? "Keep Playing" : "New Game");
		button.setTextFill(Color.WHITE);
		button.setOnAction(won ? (action) -> onKeepPlayingClick() : (action) -> onNewGameClick());
		button.getStylesheets().add(getClass().getResource("style.css").toString());
		button.getStyleClass().add("overlayButton");
		button.setFont(new Font(16));
		buttonPane.getChildren().add(button);
		buttonPane.setAlignment(Pos.TOP_CENTER);
		
		overlayPane.add(buttonPane, 0, 1);
	}
	
	private void clearOverlay() {
		overlayPane.getChildren().clear();
		overlayPane.setStyle("");
	}
	
	
	private void drawTiles(boolean animate) {		
		gamePane.getChildren().clear();
		gamePane.getChildren().addAll(tileGenerator.getBackgroundTiles());
		gamePane.getChildren().addAll(tileGenerator.generateTileNodes(tiles, animate));
	}

	@Override
	public void highScoreUpdated(int newHighScore) {
		try {
			fileSupport.writeHighScore(newHighScore);
			highScoreText.setText(String.valueOf(newHighScore));
			throw new IllegalArgumentException("test");
		} catch (Throwable e) {
			highScoreText.setText("Save error");
		}
		
	}

	@Override
	public void stateUpdated(GameState newState) {
		switch (newState) {
		case LOST:
			addOverlay(false);
			break;
		case WON:
			addOverlay(true);
			break;
		default:
			clearOverlay();
			break;
		} 
	}

	@Override
	public void tilesMoved(Collection<RenderableTile> updatedTiles) {
		tiles = updatedTiles;
		drawTiles(true);
	}

	@Override
	public void scoreUpdated(int newScore) {
		scoreText.setText(String.valueOf(newScore));
	}
}
