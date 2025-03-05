
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
	private HomeScreen homeScreen;
	private GameEngine gameEngine;
	
	public GameManager(GameEngine gameEngine) {
		this.status = GameState.NotStarted;
		this.result = Result.Unset;
		this.homeScreen = new HomeScreen();
		this.gameEngine = gameEngine;
	}
}
