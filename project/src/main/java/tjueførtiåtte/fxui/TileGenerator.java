package tjueførtiåtte.fxui;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import tjueførtiåtte.model.Game;
import tjueførtiåtte.model.Tile;

public class TileGenerator {
	private static String tileColors[] = new String[] {
		"#000000",
		"#eee4da",
		"#eee1c9",
		"#f3b27a",
		"#f69664",
		"#f77c5f",
		"#f75f3b",
		"#edd073",
		"#edcc62",
		"#edc950",
		"#edc53f",
		"#edc22e",
		"#3c3a33",
		"#1b2528",
		"#171b70"
	};
	
	private Game game;
	private double containerWidth;
	private double containerHeight;
	
	public TileGenerator(Game game, double containerWidth, double containerHeight) {
		this.game = game;
		this.containerWidth = containerWidth;
		this.containerHeight = containerHeight;
	}
	
	public List<Label> generateTiles() {		
		List<Label> tiles = new ArrayList<Label>();
		
		double padding = 10;
		
		// find the tile size
		double usableWidth = containerWidth - padding*(game.getBoardWidth()+1);
		double usableHeight = containerHeight - padding*(game.getBoardHeight()+1);
		
		double tileWidth = usableWidth / game.getBoardWidth();
		double tileHeight = usableHeight / game.getBoardHeight();
		
		double tileSize = Math.min(tileWidth, tileHeight);
		
		
		// create all tiles
		for (int x = 0; x < game.getBoardWidth(); x++) {
			for (int y = 0; y < game.getBoardHeight(); y++) {
				double xPos = (x+1)*padding + x*tileSize;
				double yPos = (y+1)*padding + y*tileSize;
				if (game.boardPositionHasTile(x, y)) {
					Tile tile = game.getTile(x, y);
					
					String text = String.valueOf(tile.getValue());
					
					String color = tileColors[tile.getTier()];
					
					boolean darkText = tile.getTier() < 3;
					
					Label tileLabel = generateTile(xPos, yPos, tileSize, text, color, darkText);
					tiles.add(tileLabel);
				} else {
					tiles.add(generateTile(xPos, yPos, tileSize, "", "#CCC1B3", false));
				}
			}
		}
		
		
		return tiles;
	}
	
	private Label generateTile(double posX, double posY, double size, String text, String color, boolean darkText) {
		Label tile = new Label();
		tile.setText(text);
		tile.setFont(new Font(40));
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
