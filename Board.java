import java.util.ArrayList;
import java.util.List;

public class Board {
	protected List<List<List<Tile>>> currentBoard;

	private Integer width;
	private Integer height;

	public Board(Integer width, Integer height) {
		this.width = width;
		this.height = height;

		this.currentBoard = new ArrayList<List<List<Tile>>>();
		for (int row = 0; row < height; row++) {
			List<List<Tile>> curRow = new ArrayList<List<Tile>>();

			for (int col = 0; col < width; col++) {
				curRow.add(new ArrayList<Tile>());
			}
			this.currentBoard.add(curRow);
		}
	}

	public void PlaceTile(Tile tile, Integer row, Integer col) {
		this.currentBoard.get(row).get(col).set(0, tile);
	}

	public Tile RemoveTile(Integer row, Integer col) {

		Tile tileRemoved = this.currentBoard.get(row).get(col).remove(0);
		return tileRemoved;
	}

	public Boolean IsInBounds(Integer row, Integer col) {
		return (row >= 0) && (row < this.width) && (col >= 0) && (col < this.height);
	}

	public Integer GetWidth() {
		return this.width;
	}

	public Integer GetHeight() {
		return this.height;
	}

	public List<List<List<Tile>>> GetBoardState() {
		return this.currentBoard;
	}
}
