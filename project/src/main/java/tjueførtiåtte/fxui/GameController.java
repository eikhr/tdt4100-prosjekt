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
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import tjueførtiåtte.model.Direction;
import tjueførtiåtte.model.Game;
import tjueførtiåtte.model.Tile;

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
		drawTiles();
	}
	
	private void drawTiles() {
		System.out.println(gamePane.getChildren().size());
		gamePane.getChildren().clear();
		gamePane.getChildren().addAll(generateTiles());
	}
	
	private List<Label> generateTiles() {
		List<Label> tiles = new ArrayList<Label>();
		
		double padding = 10;
		
		// find the tile size
		double width = gamePane.getWidth();
		double height = gamePane.getHeight();
		
		double usableWidth = width - padding*(game.getBoardWidth()+1);
		double usableHeight = height - padding*(game.getBoardHeight()+1);
		
		double tileWidth = usableWidth / game.getBoardWidth();
		double tileHeight = usableHeight / game.getBoardHeight();
		
		double tileSize = Math.min(tileWidth, tileHeight);
		
		
		// create all tiles
		for (int x = 0; x < game.getBoardWidth(); x++) {
			for (int y = 0; y < game.getBoardHeight(); y++) {
				if (game.boardPositionHasTile(x, y)) {
					Tile tile = game.getTile(x, y);
					
					double xPos = (x+1)*padding + x*tileSize;
					double yPos = (y+1)*padding + y*tileSize;
					
					String text = String.valueOf(tile.getValue());
					
					System.out.print(xPos);
					System.out.println(yPos);
					
					Label tileLabel = generateTile(xPos, yPos, tileSize, text, "#123456", false);
					tiles.add(tileLabel);
				}
			}
		}
		
		
		return tiles;
	}
	
	private Label generateTile(double posX, double posY, double size, String text, String color, boolean darkText) {
		Label tile = new Label();
		tile.setText(text);
		tile.setFont(new Font(30));
		tile.setTextFill(darkText ? Color.BLACK : Color.WHITE);
		tile.setAlignment(Pos.CENTER);
		tile.setLayoutX(posX);
		tile.setLayoutY(posY);
		tile.setStyle("-fx-background-color: "+color+"; -fx-background-radius: 10;");
		tile.setPrefHeight(size);
		tile.setPrefWidth(size);
		
		return tile;
	}
}
