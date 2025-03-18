import java.util.Map;

public class HomeScreen {
    private Map<Integer, GameEngine> games; // Game ID -> GameEngine
    private Map<Integer, Map<Integer, Integer>> leaderboard; // Game ID -> (User ID -> Score)

    public GameEngine GetGameEngine(Integer gameId) { 
        return this.games.get(gameId); // Retrieve a game engine by ID
    }

    public void SetNewScore(Integer gameId, Integer userId, Integer score) { 
        this.leaderboard.get(gameId).put(userId, score); // Update leaderboard score
    }
}
