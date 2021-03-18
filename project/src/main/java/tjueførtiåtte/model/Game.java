package tjueførtiåtte.model;

import java.util.List;
import java.util.Random;

public class Game {
	private Board board;
	
	public Game() {
		board = new Board();
		
		addRandomTile();
		addRandomTile();
	}
	
	public void move(Direction direction) {
		boolean hasMoved = false;
		
		// when looking right or down, we need to reverse lookup order so that the tiles closest to the edge will move there first
		if (direction == Direction.DOWN || direction == Direction.RIGHT) {
			for (int x = board.getWidth()-1; x >=0; x--) {
				for (int y = board.getHeight()-1; y >= 0; y--) {
					boolean didMove = attemptMoveTile(x, y, direction);
					hasMoved = hasMoved || didMove;
				}
			}
		} else {
			for (int x = 0; x < board.getWidth(); x++) {
				for (int y = 0; y < board.getHeight(); y++) {
					boolean didMove = attemptMoveTile(x, y, direction);
					hasMoved = hasMoved || didMove;
				}
			}
		}
		
		if (hasMoved) {
			// after moving we add a new tile
			addRandomTile();
		} else {
			System.out.println("didn't move");
		}
	}
	
	// adds a random tile in a random empty spot
	private void addRandomTile() {
		List<int[]> emptyPositions = board.getEmptyPositions();
		
		if (emptyPositions.isEmpty()) {
			throw new IllegalStateException("No empty positions to place new tile in!");
		}
		
		// choose a random position
		Random random = new Random();
		int[] position = emptyPositions.get(random.nextInt(emptyPositions.size()));
		
		// add the tile
		addRandomTile(position[0], position[1]);
	}
	
	// adds a random tile in the specified location (either a 2 or a 4)
	private void addRandomTile(int x, int y) {
		Random random = new Random();
		if (random.nextInt(5) == 0) {
			board.addTile(x, y, new Tile(2));
		} else {
			board.addTile(x, y, new Tile(1));
		}
	}
	
	// If there is a tile in the location, move it as far to the specified location as possible and attempt merge on collision
	// return true if a tile moved
	private boolean attemptMoveTile(int x, int y, Direction direction) {
		if (board.isEmptyTile(x, y)) {
			return false;
		}
		
		Tile tile = board.getTile(x, y);
		
		int movementDistance = 0;
		
		// add to the movementDistance until we hit another tile or the edge of the board
		while (true) {
			// check if it is possible to move one tile more than the current movementDistance
			int newPos[] = getPositionAfterMovement(x, y, direction, movementDistance+1);
			int newX = newPos[0];
			int newY = newPos[1];
			
			if (!board.containsPosition(newX, newY)) {
				// we have hit the edge of the board
				break;
			}
			if (!board.isEmptyTile(newX, newY)) {
				// we have hit another tile
				Tile collidingTile = board.getTile(newX, newY);
				// if we can merge with it, we merge it into our tile, which will later be moved
				if (tile.canMergeWith(collidingTile)) {
					mergeTiles(collidingTile, tile);
					movementDistance++;
				}
				break;
			} else {
				movementDistance++;
			}
		}
		
		if (movementDistance > 0) {
			// move the distance
			int newPos[] = getPositionAfterMovement(x, y, direction, movementDistance);
			int newX = newPos[0];
			int newY = newPos[1];
			
			moveFromTo(x, y, newX, newY);
			return true;
		}
		return false;
	}
	
	// moves the tile at position x,y to empty position toX,toY
	private void moveFromTo(int x, int y, int toX, int toY) {
		if (!board.isEmptyTile(toX, toY)) {
			throw new IllegalStateException("There is alerady a tile in this position");
		}
		
		board.addTile(toX, toY, board.getTile(x, y));
		board.removeTile(x, y);
	}
	
	// merges the mergerTile into the mergeeTile, in other words the merged tile will end up in the second tile's position 
	private void mergeTiles(Tile mergerTile, Tile mergeeTile) {
		if (!mergerTile.canMergeWith(mergeeTile)) {
			throw new IllegalArgumentException("Unmergeable tiles attempted merge");
		}
		mergeeTile.increaseValue();
		board.removeTile(mergerTile);
	}
	
	private int[] getPositionAfterMovement(int x, int y, Direction direction, int distance) {
		int newPos[] = {x, y};
		
		if (direction == Direction.UP) {
			newPos[1] -= distance;
		} else if (direction == Direction.DOWN) {
			newPos[1] += distance;
		} else if (direction == Direction.LEFT) {
			newPos[0] -= distance;
		} else if (direction == Direction.RIGHT) {
			newPos[0] += distance;
		}
		
		return newPos;
	}
	
	public String toString() {
		return board.toString();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Tile tile = new Tile(1);

		System.out.println(tile.getDisplayValue());
		
		tile.increaseValue();

		System.out.println(tile.getDisplayValue());
		
		Tile tile2 = new Tile(1);
		
		System.out.println(tile.canMergeWith(tile2));
		
		tile2.increaseValue();

		System.out.println(tile.canMergeWith(tile2));
		
		Game game = new Game();
		
		System.out.println(game.toString());
		
		game.move(Direction.UP);
		
		System.out.println(game.toString());

		game.move(Direction.DOWN);
		
		System.out.println(game.toString());
		
		for (int i = 0; i < 5; i++) {
			game.move(Direction.DOWN);
			
			System.out.println(game.toString());
	
			game.move(Direction.RIGHT);
			
			System.out.println(game.toString());
		}
	}
}