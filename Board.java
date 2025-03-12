package BaseFolder;
import java.util.ArrayList;
import java.util.List;

public class Board {
	private List<List<List<ITile>>> currentBoard;
	
	private Integer width;
	private Integer height;
	
	public Board(Integer width, Integer height) {
		this.width = width;
		this.height = height;
		
		this.currentBoard = new ArrayList<List<List<ITile>>>();
		for (int row = 0; row < height; row++) {
			List<List<ITile>> curRow = new ArrayList<List<ITile>>();
			
			for (int col = 0; col < width; col++) {
				curRow.add(new ArrayList<ITile>());
			}
			this.currentBoard.add(curRow);
		}
	}
	
	public void PlaceTile(ITile tile, Integer row, Integer col) {
		this.currentBoard.get(row).get(col).add(tile);
	}
	
	public ITile RemoveTile(Integer row, Integer col) {
		
		ITile tileRemoved = this.currentBoard.get(row).get(col).remove(0);
		return tileRemoved;
	}
	
	public Boolean IsInBounds(Integer row, Integer col) {
		return (row >= 0) && (row < this.width) && (col >= 0) && (col < this.height);
	}
	
	public Integer GetWidth(){
		return this.width;
	}
	
	public Integer GetHeight(){
		return this.height;
	}
	
	public List<List<List<ITile>>> GetBoardState() {
		return this.currentBoard;
	}
}
