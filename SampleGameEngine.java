import java.util.List;

public class SampleGameEngine extends GameEngine{
	
	public SampleGameEngine(List<User> users) {
		this.gameBoard = new Board(1, 1);
	}
	
	@Override
	boolean Action(String command) {
		return command.equals("action");
	}

	@Override
	void MatchTiles() {
		// TODO Auto-generated method stub
		
	}

}
