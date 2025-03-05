import java.util.List;

public class Board {
	private List<List<ITile>> currentBoard;
	
	private Integer width;
	private Integer height;
	
	public void PlaceTile(ITile tile, Integer row, Integer col) {
		this.currentBoard.get(row).set(col, tile);
	}
	
	public ITile RemoveTile(Integer row, Integer col) {
		
		ITile tileRemoved = this.currentBoard.get(row).get(col);
		this.currentBoard.get(row).remove(col.intValue());
		return tileRemoved;
	}
	
	public Boolean IsInBounds(Integer row, Integer col) {
		return (row >= 0) && (row < this.width) && (col >= 0) && (col < this.height);
	}
}
