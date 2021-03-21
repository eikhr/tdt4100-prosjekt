package tjueførtiåtte.fxui;

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
import tjueførtiåtte.model.Game;
import tjueførtiåtte.model.GameManager;
import tjueførtiåtte.model.GameState;

public class GameController {
	private GameManager gameManager;
	
	@FXML Pane gamePane;
	
	@FXML GridPane overlayPane;
	
	@FXML Label scoreText;
	
	@FXML Label highScoreText;
	
	@FXML
	private void initialize() {
		gameManager = new GameManager();
		updateUI(true);
		gamePane.requestFocus();
	}
	
	@FXML
	private void onNewGameClick() {
		gameManager.startNewGame();
		updateUI(true);
	}
	
	private void onKeepPlayingClick() {
		gameManager.getGame().continueGame();
		updateUI(false);
		gamePane.requestFocus();
	}
	
	@FXML
	private void onContinueGameClick() {
		gameManager.getGame().continueGame();
		gamePane.requestFocus();
	}

	@FXML
	private void keyPressed(KeyEvent keyEvt) {
		Game game = gameManager.getGame();
		
		if (game.getState() != GameState.ONGOING && game.getState() != GameState.CONTINUED) {
			return;
		}
		
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
		gameManager.getGame().move(direction);
		keyEvt.consume();
		updateUI(true);
	}
	
	@FXML
	private void gameClicked(MouseEvent evt) {
		gamePane.requestFocus();
	}
	
	public void updateUI(boolean animate) {
		Game game = gameManager.getGame();
		drawTiles(animate);
		scoreText.setText(String.valueOf(game.getScore()));
		highScoreText.setText(String.valueOf(gameManager.getHighScore()));
		
		switch (game.getState()) {
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
	
	// add overlay, either won or lost
	public void addOverlay(boolean won) {
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
	
	public void clearOverlay() {
		overlayPane.getChildren().clear();
		overlayPane.setStyle("");
	}
	
	
	private void drawTiles(boolean animate) {
		TileGenerator generator = new TileGenerator(gameManager.getGame(), gamePane.getWidth(), gamePane.getHeight(), animate);
		
		gamePane.getChildren().clear();
		gamePane.getChildren().addAll(generator.generateTiles());
	}
}
