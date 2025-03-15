package GameModules;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {
	public enum GameState {
		NotStarted,
		InProgress,
		Ended,
	}

	public enum Result {
		Unset,
		Win,
		Lose,
	}

	
	private GameState status;
	private Result result;
	private GameEngine currentGameEngine;
	private User currentUser;
	private Map<Integer, User> users;
	private Map<Integer, GameEngine> games;
	private Map<Integer, Map<Integer, Integer>> leaderboard; //GameId: UserId:Score

	
	public GameManager() {
		this.status = GameState.NotStarted;
		this.result = Result.Unset;
		this.users = new HashMap<Integer, User>();
		this.games = new HashMap<Integer, GameEngine>();
		
		this.currentGameEngine = new SampleGameEngine("Game1");
		this.games.put(1, currentGameEngine);
		this.games.put(2, new SampleGameEngine("Game2"));
	}
	
	public void AddUser(String name, Integer id) {
		this.users.put(id, new User(name, id));
	}
	
	public User GetUser(Integer id) {
		return this.users.get(id);
	}
	
	public void UpdateUser(User changedUser) {
		this.users.put(changedUser.userId, changedUser);
	}
	public void SetGameEngine(GameEngine gameEngine) {
		this.currentGameEngine = gameEngine;
	}
	
	public Board GetBoardState() {
		return this.currentGameEngine.GetBoardState();
	}
	
	public Result GetResult() {
		return this.result;
	}
	
	public GameState GetStatus() {
		return this.status;
	}

	public GameEngine GetGameEngine(Integer gameId) {
		return this.games.get(gameId);
	}
	
	public void SetNewScore(Integer gameId, Integer userId, Integer score) {
		this.leaderboard.get(gameId).put(userId, score);
	}
	
	public List<GameEngine> GetAllGameEngines() {
		return new ArrayList<GameEngine>(this.games.values());
	}
	
	public boolean IsUser(Integer userId) {
		return this.users.containsKey(userId);
	}
	
	public void SetCurrentUser(Integer userId) {
		this.currentUser = this.users.get(userId);
	}
	
	public User GetCurrentUser() {
		return this.currentUser;
	}
	
}
