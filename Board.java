import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game board that can hold tiles in a grid structure.
 * Each position on the board can contain a stack of tiles.
 */
public class Board {
    // 3D list representing the board: rows x columns x stacks of tiles
    protected List<List<List<Tile>>> currentBoard;
    private Integer width;  // Width of the board (number of columns)
    private Integer height; // Height of the board (number of rows)
    
    /**
     * Constructs a new board with the specified dimensions.
     * Initializes each position with an empty list to hold tiles.
     * 
     * @param width The width (number of columns) of the board
     * @param height The height (number of rows) of the board
     */
    public Board(Integer width, Integer height) {
        this.width = width;
        this.height = height;
        this.currentBoard = new ArrayList<List<List<Tile>>>();
        
        // Initialize the board with empty tile lists at each position
        for (int row = 0; row < height; row++) {
            List<List<Tile>> curRow = new ArrayList<List<Tile>>();
            for (int col = 0; col < width; col++) {
                curRow.add(new ArrayList<Tile>());
            }
            this.currentBoard.add(curRow);
        }
    }
    
    /**
     * Places a tile at the specified position on the board.
     * This replaces any existing tile at index 0.
     * 
     * @param tile The tile to place
     * @param row The row position
     * @param col The column position
     */
    public void PlaceTile(Tile tile, Integer row, Integer col) {
        this.currentBoard.get(row).get(col).set(0, tile);
    }
    
    /**
     * Removes and returns the top tile from the specified position.
     * 
     * @param row The row position
     * @param col The column position
     * @return The removed tile
     */
    public Tile RemoveTile(Integer row, Integer col) {
        Tile tileRemoved = this.currentBoard.get(row).get(col).remove(0);
        return tileRemoved;
    }
    
    /**
     * Checks if the specified position is within the board boundaries.
     * 
     * @param row The row position to check
     * @param col The column position to check
     * @return True if the position is valid, false otherwise
     */
    public Boolean IsInBounds(Integer row, Integer col) {
        return (row >= 0) && (row < this.width) && (col >= 0) && (col < this.height);
    }
    
    /**
     * Returns the width of the board.
     * 
     * @return The width (number of columns)
     */
    public Integer GetWidth() {
        return this.width;
    }
    
    /**
     * Returns the height of the board.
     * 
     * @return The height (number of rows)
     */
    public Integer GetHeight() {
        return this.height;
    }
    
    /**
     * Returns the current state of the board.
     * 
     * @return A 3D list representing the current board state
     */
    public List<List<List<Tile>>> GetBoardState() {
        return this.currentBoard;
    }
}
