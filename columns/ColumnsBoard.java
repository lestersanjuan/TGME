package columns;

import GameEngine.ITile;
import java.util.ArrayList;
import java.util.List;

public class ColumnsBoard {
    private List<List<ITile>> currentBoard;
    private Integer width;
    private Integer height;

    /**
     * Constructor
     */
    public ColumnsBoard() {
        this.width = 6;
        this.height = 13;
        initializeBoard();
    }

    /**
     * Initializes the board with empty tiles
     */
    private void initializeBoard() {
        currentBoard = new ArrayList<>(height);

        for (int row = 0; row < height; row++) {
            List<ITile> rowList = new ArrayList<>(width);
            for (int j = 0; j < width; j++) {
                rowList.add(null);
            }
            currentBoard.add(rowList);
        }
    }

    /**
     * Check if coordinates are within board boundaries
     * @param row Row index
     * @param col Column index
     * @return true if coordinates are valid, false otherwise
     */
    public Boolean IsInBounds(Integer row, Integer col) {
        return (row >= 0) && (row < this.height) && (col >= 0) && (col < this.width);
    }
    
    /**
     * Place a tile at the specified position
     * @param tile The tile to place
     * @param row Row index
     * @param col Column index
     */
    public void placeTile(ITile tile, Integer row, Integer col) {
        if (IsInBounds(row, col)) {
            this.currentBoard.get(row).set(col, tile);
        }
    }

    /**
     * Get the tile at the specified position
     * @param row Row index
     * @param col Column index
     * @return The tile at the position, or null if no tile exists
     */
    public ITile getTile(Integer row, Integer col) {
        if (IsInBounds(row, col)) {
            return this.currentBoard.get(row).get(col);
        }
        return null;
    }

    /**
     * Check if a position is empty (has no tile)
     * @param row Row index
     * @param col Column index
     * @return true if position is empty, false otherwise
     */
    public Boolean isEmpty(Integer row, Integer col) {
        if (!IsInBounds(row, col)) {
            return false;
        }
        return this.currentBoard.get(row).get(col) == null;
    }
    
    /**
     * Get the width of the board
     * @return Width (number of columns)
     */
    public Integer getWidth() {
        return this.width;
    }
    
    /**
     * Get the height of the board
     * @return Height (number of rows)
     */
    public Integer getHeight() {
        return this.height;
    }
    
    /**
     * Get the current board state
     * @return 2D list representing the board
     */
    public List<List<ITile>> getBoard() {
        return this.currentBoard;
    }
}