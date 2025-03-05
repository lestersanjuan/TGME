import java.util.List;

public class SampleGameEngine extends GameEngine{
	
	public SampleGameEngine(List<User> users) {
		this.gameBoard = new Board();
	}
	
	@Override
	boolean Action(String command) {
		return command.equals("action");
	}

}
