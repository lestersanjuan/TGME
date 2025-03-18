/**
 * Abstract class representing a generic game engine.
 * This class provides the base structure for various game implementations.
 */
public abstract class GameEngine {
    // The game board containing the game state
    protected Board gameBoard;
    
    // Unique identifier for the game instance
    protected Integer gameId;
    
    // Current score in the game
    protected Integer score;
    
    /**
     * Executes a game action based on the provided command.
     * This method should be implemented by specific game engines.
     * 
     * @param command The command string representing the action to perform
     * @return boolean indicating if the action was successful
     */
    abstract boolean Action(String command);
    
    /**
     * Returns the unique identifier for this game instance.
     * 
     * @return The game ID
     */
    public Integer GetGameId() {
        return this.gameId;
    }
    
    /**
     * Returns the current state of the game board.
     * Note: Implementations should consider returning a defensive copy
     * of the board to prevent external modification.
     * 
     * @return The current game board
     */
    public Board GetBoardState() {
        return this.gameBoard; // Should return copy if possible
    }
    
    /**
     * Logic for matching tiles in the game.
     * This method should be implemented by specific game engines
     * to define how tiles are matched according to game rules.
     */
    abstract void MatchTiles();
}
