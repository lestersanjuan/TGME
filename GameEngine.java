import java.util.List;

public abstract class GameEngine {
	protected Board gameBoard;
	protected List<User> users;
	
	abstract boolean Action(String command);
}
