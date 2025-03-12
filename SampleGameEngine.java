package BaseFolder;
import java.util.List;

public class SampleGameEngine extends GameEngine{
	
	public SampleGameEngine(String name) {
		this.gameBoard = new Board(1, 1);
		this.gameName = name;
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
