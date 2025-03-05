import java.util.Map;

public class HomeScreen {
	private Map<Integer, GameEngine> games;
	private Map<Integer, Map<Integer, Integer>> leaderboard; //GameId: UserId:Score

	public GameEngine GetGameEngine(Integer gameId) {
		return this.games.get(gameId);
	}
	
	public void SetNewScore(Integer gameId, Integer userId, Integer score) {
		this.leaderboard.get(gameId).put(userId, score);
	}
}
