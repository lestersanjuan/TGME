enum GameState {
	NotStarted,
	InProgress,
	Ended,
}

enum Result {
	Win,
	Lose,
}

public class GameManager {
	private GameState status;
	private Result result;
	private HomeScreen homeScreen;
}
