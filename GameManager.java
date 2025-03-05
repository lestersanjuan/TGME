import java.util.List;

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
	private List<User> users;
	private IInputHandler inputHandler;
	
	public GameManager(List<User> users) {
		this.status = GameState.NotStarted;
		this.result = Result.Unset;
		this.homeScreen = new HomeScreen();
		this.users = users;
	}
	
	public void SetGameEngine(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}
	
	public void GetGameEngine() {
		// Ask for Game engine through input
		this.homeScreen.GetGameEngine(null);
	}
	
	public Board GetBoardState() {
		return this.gameEngine.GetBoardState();
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
}
