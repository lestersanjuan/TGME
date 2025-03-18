public abstract class GuiHandler {
    protected GameEngine gameEngine; // Reference to the game engine

    abstract void UpdateUI(Board boardState); // Update UI based on board state

    abstract void AffectGameEngine(String command); // Process user commands affecting the game

    public void SetGameEngine(GameEngine gameEngine) { 
        this.gameEngine = gameEngine; // Set the game engine
    }
}
