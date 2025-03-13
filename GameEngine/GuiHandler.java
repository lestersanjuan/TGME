package GameEngine;

public abstract class GuiHandler {
	protected GameEngine gameEngine;
	
	abstract void UpdateUI(Board boardState);
	
	abstract void AffectGameEngine(String command);
	
	public void SetGameEngine(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}
}
