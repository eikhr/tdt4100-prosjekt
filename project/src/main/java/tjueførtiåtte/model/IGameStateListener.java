package tjueførtiåtte.model;

import java.util.Collection;

public interface IGameStateListener extends IGameScoreListener {
	public void stateUpdated(GameState newState);
	public void tilesMoved(Collection<IRenderableTile> tiles);
}
