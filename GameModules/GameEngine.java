package GameModules;

public abstract class GameEngine {
	protected String gameName;
	protected Board gameBoard;
	protected Integer gameId;
	protected Integer score;
	protected Boolean gameRunning;
	
	public GameEngine(String name, Integer id) {
		this.gameName = name;
		this.gameId = id;
		this.gameRunning = true;
		this.score = 0;
	}
	
	public abstract void ResetEngine();
	
	public abstract boolean Action(String command);
	
	public Integer GetGameId() {
		return this.gameId;
	}
	
	public Board GetBoardState() {
		return this.gameBoard; //Should return copy if possible
	}
	
	public String GetGameName() {
		return this.gameName;
	}
	
	public abstract void MatchTiles();
	
	public Boolean IsGameRunning() {
		return this.gameRunning;
	}
	
	public Integer GetScore() {
		return this.score;
	}
}
