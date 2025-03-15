import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GameManager {
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

	protected GameState status;
	protected Result result;
	protected GameEngine currentGameEngine;
	protected Map<Integer, User> users;
	protected IInputHandler inputHandler;
	protected Map<Integer, GameEngine> games;
	protected Map<Integer, Map<Integer, Integer>> leaderboard; //GameId: UserId:Score

	public GameManager() {
		this.status = GameState.NotStarted;
		this.result = Result.Unset;
		this.users = new HashMap<Integer, User>();
		this.games = new HashMap<Integer, GameEngine>();
		this.leaderboard = new HashMap<Integer, Map<Integer, Integer>>();
	}
	
	abstract void AddUser(String name, Integer id, List<String> data);
	
	public User GetUser(Integer id) {
		return this.users.get(id);
	}
	
	public void UpdateUser(User changedUser) {
		this.users.put(changedUser.userId, changedUser);
	}
	
	public void SetGameEngine(GameEngine gameEngine) {
		this.currentGameEngine = gameEngine;
	}
	
	//
	public Board GetBoardState() {
		return this.currentGameEngine.GetBoardState();
	}
	
	public Result GetResult() {
		return this.result;
	}
	
	public GameState GetStatus() {
		return this.status;
	}
	
	public String GetInput() { //May not be used if a gui is being used
		return this.inputHandler.GetInput();
	}

	public GameEngine GetGameEngine(Integer gameId) {
		return this.games.get(gameId);
	}
	
	public void SetNewScore(Integer gameId, Integer userId, Integer score) {
		this.leaderboard.get(gameId).put(userId, score);
	}
}
