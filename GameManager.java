import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GameManager {
    
    public enum GameState { NotStarted, InProgress, Ended } // Game states
    public enum Result { Unset, Win, Lose } // Game results

    protected GameState status;
    protected Result result;
    protected GameEngine currentGameEngine;
    protected Map<Integer, User> users;
    protected IInputHandler inputHandler;
    protected Map<Integer, GameEngine> games;
    protected Map<Integer, Map<Integer, Integer>> leaderboard; // GameId -> (UserId -> Score)

    public GameManager() {
        this.status = GameState.NotStarted;
        this.result = Result.Unset;
        this.users = new HashMap<>();
        this.games = new HashMap<>();
        this.leaderboard = new HashMap<>();
    }
    
    abstract void AddUser(String name, Integer id, List<String> data); // Add a user
    
    public User GetUser(Integer id) { return this.users.get(id); } // Retrieve a user
    
    public void UpdateUser(User changedUser) { this.users.put(changedUser.userId, changedUser); } // Update user info
    
    public void SetGameEngine(GameEngine gameEngine) { this.currentGameEngine = gameEngine; } // Set current game engine
    
    public Board GetBoardState() { return this.currentGameEngine.GetBoardState(); } // Get current board state
    
    public Result GetResult() { return this.result; } // Get game result
    
    public GameState GetStatus() { return this.status; } // Get game status
    
    public String GetInput() { return this.inputHandler.GetInput(); } // Get user input
    
    public GameEngine GetGameEngine(Integer gameId) { return this.games.get(gameId); } // Get a game engine by ID
    
    public void SetNewScore(Integer gameId, Integer userId, Integer score) { 
        this.leaderboard.get(gameId).put(userId, score); // Update leaderboard
    }
}
