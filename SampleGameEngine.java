import java.util.List;

public class SampleGameEngine extends GameEngine{
	
	public SampleGameEngine(List<User> users) {
		this.gameBoard = new Board();
		this.users = users;
	}
	
	@Override
	boolean Action(String command) {
		return command.equals("action");
	}

}
