package columns;

import GameEngine.ITile;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ColumnsGameEngine {
    private ColumnsBoard board;
    private TileFactory tileFactory;
    private boolean gameRunning;
    private static final int FALL_DELAY_MS = 1000; // 1 second delay between falls
    private static final int INVISIBLE_HEIGHT = 15; // Invisible height limit
    
    // Track the height of each column separately from the visible board
    private int[] columnHeights;
    
    /**
     * Constructor
     */
    public ColumnsGameEngine() {
        this.board = new ColumnsBoard();
        this.tileFactory = new TileFactory();
        this.gameRunning = true;
        this.columnHeights = new int[board.getWidth()];
        
        for (int i = 0; i < columnHeights.length; i++) {
            columnHeights[i] = 0;
        }
    }
    
    /**
     * Start the game loop
     */
    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Welcome to Columns!");
        System.out.println("Pieces will fall one block per second from the middle of the board.");
        System.out.println("Press Enter to start the game (press Ctrl+C to quit)...");
        scanner.nextLine();
        
        // Always drop from the middle column
        int middleCol = board.getWidth() / 2;
        
        while (gameRunning) {
            // Create a new column piece
            List<ITile> columnPiece = tileFactory.createColumn();
            
            // Start dropping the piece from the top
            boolean pieceDropped = dropPiece(columnPiece, middleCol);
            
            // If we couldn't drop the piece, the game is over
            if (!pieceDropped) {
                gameRunning = false;
                drawBoard(); // Final board state
                System.out.println("Game Over! A column has exceeded the height limit.");
                break;
            }
            
            // Check for matches and clear them (Placeholder!!!)
            checkAndClearMatches();
        }
        
        scanner.close();
    }
    
    /**
     * Drop a piece from the top to the bottom of the board
     * @param columnPiece The column piece (list of 3 tiles)
     * @param col The column to drop the piece in
     * @return true if the piece was dropped successfully, false otherwise
     */
    private boolean dropPiece(List<ITile> columnPiece, int col) {
        int pieceLength = columnPiece.size(); // Should be 3 for a standard column
        
        // Check if adding this piece would exceed the invisible height limit
        if (columnHeights[col] + pieceLength > INVISIBLE_HEIGHT) {
            return false;
        }
        
        // Find the lowest position where the piece can be placed
        int lowestEmptyRow = getLowestEmptyRow(col);
        
        // Calculate the final position of the top of the piece
        int finalTopRow = lowestEmptyRow - (pieceLength - 1);
        
        // Start with the piece just above the board
        int topRow = -pieceLength;
        
        // Animation loop - drop the piece one row at a time
        while (topRow <= finalTopRow) {
            // Clear previous position
            if (topRow > -pieceLength) {
                for (int i = 0; i < pieceLength; i++) {
                    int row = topRow + i - 1;
                    if (row >= 0 && row < board.getHeight()) {
                        board.placeTile(null, row, col);
                    }
                }
            }
            
            // Place the piece at new position
            for (int i = 0; i < pieceLength; i++) {
                int row = topRow + i;
                if (row >= 0 && row < board.getHeight()) {
                    board.placeTile(columnPiece.get(i), row, col);
                }
            }
            
            drawBoard();
            
            // Wait for 1 second
            try {
                TimeUnit.MILLISECONDS.sleep(FALL_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            topRow++;
        }
        columnHeights[col] += pieceLength;
        if (columnHeights[col] >= INVISIBLE_HEIGHT) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Find the lowest empty position in a column
     * @param col The column to check
     * @return The row index of the lowest empty position, or -1 if column is full
     */
    private int getLowestEmptyRow(int col) {
        for (int row = board.getHeight() - 1; row >= 0; row--) {
            if (board.isEmpty(row, col)) {
                return row;
            }
        }
        return -1; // Column is full
    }
    
    /**
     * Check for matches and clear them (Placeholder!!)
     */
    private void checkAndClearMatches() {
        // Placeholder for match detection
    }
    
    /**
     * Draw the board in the terminal
     */
    private void drawBoard() {
        clearScreen();
        System.out.println("Columns Game");
        System.out.println("------------");
        
        // Draw the board
        for (int row = 0; row < board.getHeight(); row++) {
            System.out.print("|");
            for (int col = 0; col < board.getWidth(); col++) {
                ITile tile = board.getTile(row, col);
                if (tile == null) {
                    System.out.print(" ");
                } else {
                    System.out.print(tile.GetValue());
                }
            }
            System.out.println("|");
        }
        
        // Draw the bottom border
        System.out.print("+");
        for (int col = 0; col < board.getWidth(); col++) {
            System.out.print("-");
        }
        System.out.println("+");
        
        // Debug info - show column heights (optional)
        // System.out.println("Column heights: " + Arrays.toString(columnHeights));
    }
    
    /**
     * Clear the terminal screen
     */
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}