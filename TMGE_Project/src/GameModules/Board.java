package GameModules;
import java.util.ArrayList;
import java.util.List;

public class Board {
	private List<Row> currentBoard;
	
	private Integer width;
	private Integer height;
	
	public Board(Integer width, Integer height) {
		this.width = width;
		this.height = height;
		
		this.currentBoard = new ArrayList<Row>();
		for (int row = 0; row < height; row++) {
			Row newRow = new Row(this.width);
			this.currentBoard.add(newRow);
		}
	}
	
	public void PlaceTile(ITile tile, Integer row, Integer col) {
		this.currentBoard.get(row).GetSpot(col).PushTile(tile);
	}
	
	public ITile RemoveTile(Integer row, Integer col) {
		
		ITile tileRemoved = this.currentBoard.get(row).GetSpot(col).PopTile();
		return tileRemoved;
	}
	
	public Spot GetSpot(Integer row, Integer col) {
		return this.currentBoard.get(row).GetSpot(col);
	}
	
	public ITile GetFirstTile(Integer row, Integer col) {
		if (this.currentBoard.get(row).GetSpot(col).IsEmpty()) {
			return null;
		}
		
		return this.currentBoard.get(row).GetSpot(col).GetTiles().get(0);
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
	
	public List<Row> GetBoardState() {
		return this.currentBoard;
	}
	
	public class Row {
		private List<Spot> spots;
		
		public Row(Integer rowLength) {
			this.spots = new ArrayList<Spot>();
			for (int i = 0; i < rowLength; i++) {
				this.spots.add(new Spot());
			}
		}
		
		public List<Spot> GetSpots() {
			return this.spots;
		}
		
		public Spot GetSpot(Integer col) {
			return this.spots.get(col);
		}
	}
	
	public class Spot {
		public List<ITile> tiles;
		
		public Spot() {
			this.tiles = new ArrayList<ITile>();
		}
		
		public List<ITile> GetTiles() {
			return this.tiles;
		}
		
		public Boolean IsEmpty() {
			return this.tiles.isEmpty();
		}
		
		public void PushTile(ITile tile) {
			this.tiles.add(tile);
		}
		
		public ITile PopTile() {
			return this.tiles.remove(0);
		}
		
		public ITile GetFirstTile() {
			if (this.tiles.isEmpty()) {
				return null;
			}
			
			return this.tiles.get(0);
		}
	}
}
