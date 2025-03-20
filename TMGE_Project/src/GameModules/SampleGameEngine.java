package GameModules;

public class SampleGameEngine extends GameEngine{
	
	public SampleGameEngine(String name, Integer id) {
		super(name, id);
		this.gameBoard = new Board(4, 8);
	}
	
	public void ResetEngine() {
		this.score = 0;
		this.gameRunning = true;
	}
	
	@Override
	public boolean Action(String command) {
		System.out.println(command);
		this.score ++;
		this.gameRunning = false;
		return command.equals("action");
	}

	@Override
	public void MatchTiles() {
		// TODO Auto-generated method stub
		
	}

}
