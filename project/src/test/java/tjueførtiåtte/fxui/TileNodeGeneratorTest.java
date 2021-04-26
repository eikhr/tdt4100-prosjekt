package tjueførtiåtte.fxui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javafx.scene.Node;

public class TileNodeGeneratorTest {
	@Test
	@DisplayName("Test generering av bakgrunns-Tiles")
	public void testBackgroundTiles() {
		TileNodeGenerator generator = new TileNodeGenerator(4, 4, 130, 130);
		
		Collection<Node> backgroundTiles = generator.getBackgroundTiles();
		
		assertEquals(16, backgroundTiles.size());
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				assertTrue(containsNodeAtPosition(10.0 + 30.0*i, 10.0 + 30.0*j, backgroundTiles));
			}
		}
	}
	
	private boolean containsNodeAtPosition(double x, double y, Collection<Node> nodes) {
		for(Node node : nodes) {
			if (node.getLayoutX() == x && node.getLayoutY() == y) {
				return true;
			}
		}
		return false;
	}
	
	@Test
	@DisplayName("Test generering av bakgrunns-Tiles med unormalt brett")
	public void testBackgroundTilesNonSquare() {
		double width = 100;
		double height = 100;
		
		TileNodeGenerator generator = new TileNodeGenerator(3, 5, width, height);
		
		Collection<Node> backgroundTiles = generator.getBackgroundTiles();
		
		assertEquals(15, backgroundTiles.size());
		
		for(Node node : backgroundTiles) {
			assertTrue(node.getLayoutX() < width);
			assertTrue(node.getLayoutY() < height);
		}
	}
}
