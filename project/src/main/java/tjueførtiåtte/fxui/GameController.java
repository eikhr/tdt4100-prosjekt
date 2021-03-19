package tjueførtiåtte.fxui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
		updateUI();
	}
	
	@FXML
	private void onNewGameClick() {
		gameManager.startNewGame();
		updateUI();
	}
	
	@FXML
	private void onContinueGameClick() {
		gameManager.getGame().continueGame();
	}

	@FXML
	private void keyPressed(KeyEvent keyEvt) {
		Game game = gameManager.getGame();
		
		switch (keyEvt.getCode()) {
		case UP:
		case W:
			game.move(Direction.UP);
			keyEvt.consume();
			updateUI();
			break;
		case DOWN:
		case S:
			game.move(Direction.DOWN);
			keyEvt.consume();
			updateUI();
			break;
		case LEFT:
		case A:
			game.move(Direction.LEFT);
			keyEvt.consume();
			updateUI();
			break;
		case RIGHT:
		case D:
			game.move(Direction.RIGHT);
			keyEvt.consume();
			updateUI();
			break;
		default:
			break;
		}
	}
	
	@FXML
	private void gameClicked(MouseEvent evt) {
		gamePane.requestFocus();
	}
	
	public void updateUI() {
		Game game = gameManager.getGame();
		drawTiles();
		scoreText.setText(String.valueOf(game.getScore()));
		highScoreText.setText(String.valueOf(gameManager.getHighScore()));
		
		switch (game.getState()) {
		case LOST:
			addOverlay();
			break;
		default:
			clearOverlay();
			break;
		} 
	}
	
	public void addOverlay() {
		overlayPane.getChildren().clear();
		overlayPane.setStyle("-fx-background-color: #00000044; -fx-background-radius: 15;");
		
		
		StackPane textPane = new StackPane();
		Label text = new Label();
		text.setText("You lost!");
		text.setTextFill(Color.WHITE);
		text.setFont(new Font(40));
		textPane.getChildren().add(text);
		textPane.setAlignment(Pos.BOTTOM_CENTER);
		
		overlayPane.add(textPane, 0, 0);
		
		StackPane buttonPane = new StackPane();
		Button button = new Button();
		button.setText("New Game");
		button.setTextFill(Color.WHITE);
		button.setOnAction((action) -> onNewGameClick());
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
	
	
	private void drawTiles() {
		TileGenerator generator = new TileGenerator(gameManager.getGame(), gamePane.getWidth(), gamePane.getHeight());
		
		gamePane.getChildren().clear();
		gamePane.getChildren().addAll(generator.generateTiles());
	}
}
