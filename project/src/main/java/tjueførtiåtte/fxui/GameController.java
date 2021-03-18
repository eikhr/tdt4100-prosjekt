package tjueførtiåtte.fxui;

import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import tjueførtiåtte.model.Game;

public class GameController {
	private Game game;
	
	@FXML Pane gamePane;
	
	@FXML
	private void initialize() {
		game = new Game();
		System.out.println("init");
	}

	@FXML
	private void keyPressed(KeyEvent keyEvt) {
		System.out.println("key:");
		System.out.println(keyEvt.getCode().toString());
		keyEvt.consume();
	}
	
	@FXML
	private void gameClicked(MouseEvent evt) {
		gamePane.requestFocus();
	}
	
}
