package GameModules;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import Games.ColumnsGameEngine;

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
	private Map<Integer, GameLeaderBoard> leaderboard; //GameId: UserId:Score
	private final static Integer DEFAULTSCORE = 0;

	
	public GameManager() {
		this.status = GameState.NotStarted;
		this.result = Result.Unset;
		this.leaderboard = new HashMap<Integer, GameLeaderBoard>();
		this.users = new HashMap<Integer, User>();
		this.games = new HashMap<Integer, GameEngine>();
		
		this.currentGameEngine = new ColumnsGameEngine(1);
		this.games.put(1, currentGameEngine);
		this.games.put(2, new SampleGameEngine("Game2", 2));
		
		for (Integer gameId : this.games.keySet()) {
			GameLeaderBoard gameLeaderBoard = new GameLeaderBoard(new ArrayList<Integer>(this.users.keySet()));
			this.leaderboard.put(gameId, gameLeaderBoard);
		}
	}
	
	public void AddUser(String name, Integer id) {
		for (Integer gameId : this.leaderboard.keySet()) {
			GameLeaderBoard gameLeaderboard = this.leaderboard.get(gameId);
			gameLeaderboard.SetUserScore(id, DEFAULTSCORE);
		}
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
		this.leaderboard.get(gameId).SetUserScore(userId, score);
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
	
	public Boolean IsGameEnded() {
		return ! this.currentGameEngine.IsGameRunning();
	}
	
	public void SetScoresAfterGame() {
		GameLeaderBoard gameLeaderboard = this.leaderboard.get(currentGameEngine.GetGameId());
		gameLeaderboard.SetUserScore(this.currentUser.GetUserId(), this.currentGameEngine.GetScore());
	}
	
	public Integer GetScore() {
		return this.currentGameEngine.GetScore();
	}
	
	public GameLeaderBoard GetLeaderboard(Integer gameId) {
		return this.leaderboard.get(gameId);
	}
	
	public class GameLeaderBoard {
		private Map<Integer, Integer> gameLeaderboard;
		
		public GameLeaderBoard(List<Integer> userIds) {
			this.gameLeaderboard = new TreeMap<Integer, Integer>();
			for (Integer userId : userIds) {
				this.gameLeaderboard.put(userId, DEFAULTSCORE);
			}
		}
		
		public Integer GetUserScore(Integer id) {
			return this.gameLeaderboard.get(id);
		}
		
		public void SetUserScore(Integer id, Integer score) {
			this.gameLeaderboard.put(id, score);
		}
		
		public List<Entry<Integer, Integer>> GetSortedLeaderBoard(){
			List<Entry<Integer, Integer>> userScorePairs = new ArrayList<Entry<Integer, Integer>>(this.gameLeaderboard.entrySet());
			userScorePairs.sort(Entry.comparingByValue());
			Collections.reverse(userScorePairs);
			
			return userScorePairs;
		}
	}
}
