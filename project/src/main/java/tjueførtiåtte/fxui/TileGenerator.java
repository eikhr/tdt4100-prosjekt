package tjueførtiåtte.fxui;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import tjueførtiåtte.model.Coords;
import tjueførtiåtte.model.Game;
import tjueførtiåtte.model.GhostTile;
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
	
	public List<Node> generateTiles() {		
		List<Node> backgroundTiles = new ArrayList<Node>();
		List<Node> ghostTiles = new ArrayList<Node>();
		List<Node> tiles = new ArrayList<Node>();
		
		double padding = 10;
		
		// find the tile size
		double usableWidth = containerWidth - padding*(game.getBoardWidth()+1);
		double usableHeight = containerHeight - padding*(game.getBoardHeight()+1);
		
		double tileWidth = usableWidth / game.getBoardWidth();
		double tileHeight = usableHeight / game.getBoardHeight();
		
		double tileSize = Math.min(tileWidth, tileHeight);
		
		// create all ghost tiles
		for (GhostTile tile : game.getGhostTiles()) {
			Coords pos = tile.getPosition();
			
			double xPos = (pos.getX()+1)*padding + pos.getX()*tileSize;
			double yPos = (pos.getY()+1)*padding + pos.getY()*tileSize;

			Node tileNode = generateTile(xPos, yPos, tileSize, padding, tile);
			ghostTiles.add(tileNode);
		}
		
		// create all tiles
		for (int x = 0; x < game.getBoardWidth(); x++) {
			for (int y = 0; y < game.getBoardHeight(); y++) {
				double xPos = (x+1)*padding + x*tileSize;
				double yPos = (y+1)*padding + y*tileSize;
				
				backgroundTiles.add(generateBackgroundTile(xPos, yPos, tileSize));
				
				if (game.boardPositionHasTile(x, y)) {
					Tile tile = game.getTile(x, y);
					
					Node tileNode = generateTile(xPos, yPos, tileSize, padding, tile);
					tiles.add(tileNode);
				}
			}
		}
		

		List<Node> allTiles = new ArrayList<Node>();
		allTiles.addAll(backgroundTiles);
		allTiles.addAll(ghostTiles);
		allTiles.addAll(tiles);
		
		
		return allTiles;
	}
	
	private Pane generateBackgroundTile(double posX, double posY, double size) {
		Pane tile = new Pane();
		tile.setLayoutX(posX);
		tile.setLayoutY(posY);
		tile.setStyle("-fx-background-color: #CCC1B3; -fx-background-radius: 10;");
		tile.setPrefHeight(size);
		tile.setPrefWidth(size);
		
		return tile;
	}
	
	private Label generateTile(double posX, double posY, double size, double padding, Tile tile) {
		Coords from = tile.getPreviousPosition();
		
		double fromPosX;
		double fromPosY;
		if (from != null) {
			fromPosX = (from.getX()+1)*padding + from.getX()*size;
			fromPosY = (from.getY()+1)*padding + from.getY()*size;
		} else {
			fromPosX = posX;
			fromPosY = posY;
		}
		
		String text = String.valueOf(tile.getValue());
		String prevText = String.valueOf(tile.getPreviousValue());
		
		String color = tileColors[tile.getTier()];
		String prevColor = tileColors[tile.getPreviousTier()];
		
		boolean darkText = tile.getTier() < 3;
		boolean prevDarkText = tile.getPreviousTier() < 3;
		
		
		
		Label tileNode = new Label();
		tileNode.setText(prevText); // gets changed to "text" after movement animation
		tileNode.setFont(new Font(40));
		tileNode.setTextFill(prevDarkText ? Color.BLACK : Color.WHITE);
		tileNode.setAlignment(Pos.CENTER);
		tileNode.setLayoutX(posX);
		tileNode.setLayoutY(posY);
		tileNode.setStyle("-fx-background-color: "+prevColor+"; -fx-background-radius: 10;");
		tileNode.setPrefHeight(size);
		tileNode.setPrefWidth(size);
		
		TranslateTransition movementTransition = new TranslateTransition(Duration.millis(120), tileNode);
		movementTransition.setCycleCount(1);
		movementTransition.setOnFinished(event -> {
			tileNode.setText(text);
			tileNode.setOpacity(1);
			tileNode.setStyle("-fx-background-color: "+color+"; -fx-background-radius: 10;");
			tileNode.setTextFill(darkText ? Color.BLACK : Color.WHITE);
		});
		
		if (prevText.equals("1")) {
			// the tile did not exist before, it should only be shown after the movement transition
			tileNode.setOpacity(0);
		}
		
		
		if (posX != fromPosX) {
			movementTransition.setFromX(fromPosX-posX);
			movementTransition.setToX(0);
		}
		
		if (posY != fromPosY) {
			movementTransition.setFromY(fromPosY-posY);
			movementTransition.setToY(0);
		}
		
		if (!text.equals(prevText)) {
			// we have a tile with updated value or a new tile
			// we need to play the "bump" transition
			
			ScaleTransition scaleUp = new ScaleTransition(Duration.millis(50));
			ScaleTransition scaleDown = new ScaleTransition(Duration.millis(50));
			
			scaleUp.setToX(1.15);
			scaleUp.setToY(1.15);
			
			scaleDown.setToX(1);
			scaleDown.setToY(1);
			
			
			SequentialTransition transition = new SequentialTransition(tileNode, movementTransition, scaleUp, scaleDown);
			transition.play();
			
		} else {
			movementTransition.play();
		}
		
		return tileNode;
	}
}
