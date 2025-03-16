package GameModules;
import java.util.List;

public class SampleGameEngine extends GameEngine{
	
	public SampleGameEngine(String name) {
		this.gameRunning = true;
		this.score = 0;
		this.gameBoard = new Board(4, 8);
		this.gameName = name;
	}
	
	@Override
	public boolean Action(String command) {
		System.out.println(command);
		this.gameRunning = false;
		return command.equals("action");
	}

	@Override
	public void MatchTiles() {
		// TODO Auto-generated method stub
		
	}

}
