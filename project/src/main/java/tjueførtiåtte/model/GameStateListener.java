package tjueførtiåtte.model;

import java.util.Collection;

public interface GameStateListener extends GameScoreListener {
	public void stateUpdated(GameState newState);
	public void tilesMoved(Collection<RenderableTile> tiles);
}
