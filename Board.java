import java.util.List;

public class Board {
	protected List<List<Tile>> currentBoard; //Will have a new 3rd list
	
	private Integer width;
	private Integer height;
	
	public void PlaceTile(Tile tile, Integer row, Integer col) {
		System.out.println("testing");
		this.currentBoard.get(row).set(col, tile);
	}
	
	public Tile RemoveTile(Integer row, Integer col) {
		
		Tile tileRemoved = this.currentBoard.get(row).get(col);
		this.currentBoard.get(row).remove(col.intValue());
		return tileRemoved;
	}
	
	public Boolean IsInBounds(Integer row, Integer col) {
		return (row >= 0) && (row < this.width) && (col >= 0) && (col < this.height);
	}

	public void setWidth(Integer width){
		this.width = width;
	}

	public void setHeight(Integer height){
		this.height = height;
	}
}

