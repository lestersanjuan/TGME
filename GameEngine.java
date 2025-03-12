package BaseFolder;

public abstract class GameEngine {
	protected String gameName;
	protected Board gameBoard;
	protected Integer gameId;
	protected Integer score;
	
	abstract boolean Action(String command);
	
	public Integer GetGameId() {
		return this.gameId;
	}
	
	public Board GetBoardState() {
		return this.gameBoard; //Should return copy if possible
	}
	
	public String GetGameName() {
		return this.gameName;
	}
	
	abstract void MatchTiles();
}
