package tjueførtiåtte.fxui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import tjueførtiåtte.model.Direction;
import tjueførtiåtte.model.Game;

public class GameController {
	private Game game;
	
	@FXML Pane gamePane;
	
	@FXML Label scoreText;
	
	@FXML
	private void initialize() {
		game = new Game();
	}

	@FXML
	private void keyPressed(KeyEvent keyEvt) {
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
	
	private void updateUI() {
		scoreText.setText(String.valueOf(game.getScore()));
	}
}
