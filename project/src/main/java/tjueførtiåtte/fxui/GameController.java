package tjueførtiåtte.fxui;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import tjueførtiåtte.model.Direction;
import tjueførtiåtte.model.Game;
import tjueførtiåtte.model.GameManager;
import tjueførtiåtte.model.Tile;

public class GameController {
	private GameManager gameManager;
	
	@FXML Pane gamePane;
	
	@FXML Label scoreText;
	
	@FXML
	private void initialize() {
		gameManager = new GameManager();
		updateUI();
		
		System.out.println(gamePane.getScene());
		System.out.println(scoreText.getScene());
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
		drawTiles();
		scoreText.setText(String.valueOf(gameManager.getGame().getScore()));
	}
	
	private void drawTiles() {
		TileGenerator generator = new TileGenerator(gameManager.getGame(), gamePane.getWidth(), gamePane.getHeight());
		
		gamePane.getChildren().clear();
		gamePane.getChildren().addAll(generator.generateTiles());
	}
}
