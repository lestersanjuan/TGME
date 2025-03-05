import java.util.List;

public abstract class GameEngine {
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
}
