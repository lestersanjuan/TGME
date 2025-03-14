package columns;

import GameEngine.ITile;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ColumnsGameEngine extends GameEngine.GameEngine{
    private ColumnsBoard board;
    private TileFactory tileFactory;
    private boolean gameRunning;
    private static final int INVISIBLE_HEIGHT = 16; // Invisible height limit
    private int score = 0;
    private final int BASE_SCORE = 150;
    private final int MATCH_BONUS = 75; // For every match greater than 3, each bonus tile will receive an extra 75 points
    
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

    @Override
    public void MatchTiles() {
        checkAndClearMatches();
    }

    @Override
    public boolean Action(String command) {
        List<ITile> columnPiece = tileFactory.createColumn();
        int middleCol = board.getWidth() / 2;
        Scanner scanner = new Scanner(System.in);
        boolean pieceDropped = dropPieceWithUserControl(columnPiece, middleCol, scanner);
        
        // Game is lost if piece can't be dropped
        if (!pieceDropped) {
            gameRunning = false;
            drawBoard(); // Final board state
            System.out.println("Game Over! A column has exceeded the height limit. Final Score: " + score);
            return false;
        }
    
        MatchTiles();    
        return true;
    }
    
    /**
     * Start the game loop
     */
    public void startGame() {
        Scanner scanner = new Scanner(System.in);
    
        System.out.println("Welcome to Columns!");
        System.out.println("Pieces will fall one block at a time.");
        System.out.println("Controls: 'a' or j=left, 'd' or l=right, 's'=rotate down");
        System.out.println("Press Enter to start the game (press Ctrl+C to quit)...");
        scanner.nextLine();
        
        gameRunning = true;

        while (gameRunning) {
            boolean success = Action(""); 
            
            // If Action returns false, the game is over
            if (!success) {
                gameRunning = false;
                System.out.println("Press Enter to exit...");
                scanner.nextLine();
                break;
            }
        }
        
        scanner.close();
    }
    
    /**
     * Drop a piece with user control
     * @param columnPiece The column piece (list of 3 tiles)
     * @param startCol The starting column
     * @param scanner Scanner for user input
     * @return true if the piece was dropped successfully, false otherwise
     */
    private boolean dropPieceWithUserControl(List<ITile> columnPiece, int startCol, Scanner scanner) {
        int pieceLength = columnPiece.size(); // 3
        int currentCol = startCol;
        
        // Start with the piece just above the board
        int topRow = -pieceLength;
        
        // Check if the starting column is already too high
        if (columnHeights[currentCol] >= INVISIBLE_HEIGHT - pieceLength) {
            // Game is already over, can't place the piece
            return false;
        }
        
        // Continue dropping until the piece lands
        while (true) {
            // Clear the previous position if needed
            if (topRow > -pieceLength) {
                for (int i = 0; i < pieceLength; i++) {
                    int row = topRow + i - 1;
                    if (row >= 0 && row < board.getHeight()) {
                        board.placeTile(null, row, currentCol);
                    }
                }
            }
            
            // Place the piece at the new position
            for (int i = 0; i < pieceLength; i++) {
                int row = topRow + i;
                if (row >= 0 && row < board.getHeight()) {
                    board.placeTile(columnPiece.get(i), row, currentCol);
                }
            }
            
            drawBoard();
            
            // Check for collision for the next position below
            if (isCollision(topRow + 1, currentCol, pieceLength)) {
                // Make sure all tiles are properly placed on the board
                for (int i = 0; i < pieceLength; i++) {
                    int row = topRow + i;
                    if (row >= 0 && row < board.getHeight()) {
                        board.placeTile(columnPiece.get(i), row, currentCol);
                    }
                }
                
                // Update column height - count all tiles in the column
                int newHeight = 0;
                for (int row = 0; row < board.getHeight(); row++) {
                    if (!board.isEmpty(row, currentCol)) {
                        newHeight++;
                    }
                }
                columnHeights[currentCol] = newHeight;
                
                // Check if the game is over
                if (columnHeights[currentCol] >= INVISIBLE_HEIGHT) {
                    // Game over - don't modify the board further
                    return false;
                }
                return true;
            }
            
            // Get user input
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("a") || input.equals("j")) { // 'a' or 'j' for left
                if (currentCol > 0) {
                    // Check if there's an existing column in the target position
                    int landingHeight = findLandingHeight(currentCol - 1);
                    
                    // If landing height is high enough that we'd land on top of the column
                    if (landingHeight >= 0 && landingHeight < topRow + pieceLength) {
                        // Clear current position
                        for (int i = 0; i < pieceLength; i++) {
                            int row = topRow + i;
                            if (row >= 0 && row < board.getHeight()) {
                                board.placeTile(null, row, currentCol);
                            }
                        }
                        
                        // Place the piece on top of the existing column
                        int newTopRow = landingHeight - pieceLength + 1;
                        for (int i = 0; i < pieceLength; i++) {
                            int row = newTopRow + i;
                            if (row >= 0 && row < board.getHeight()) {
                                board.placeTile(columnPiece.get(i), row, currentCol - 1);
                            }
                        }
                        
                        // Update column height
                        int newHeight = 0;
                        for (int row = 0; row < board.getHeight(); row++) {
                            if (!board.isEmpty(row, currentCol - 1)) {
                                newHeight++;
                            }
                        }
                        columnHeights[currentCol - 1] = newHeight;
                        
                        drawBoard();
                        return true; // Piece has landed
                    }
                    // Normal left movement if there's no collision
                    else if (!isCollision(topRow, currentCol - 1, pieceLength)) {
                        // Clear current position
                        for (int i = 0; i < pieceLength; i++) {
                            int row = topRow + i;
                            if (row >= 0 && row < board.getHeight()) {
                                board.placeTile(null, row, currentCol);
                            }
                        }
                        currentCol--;
                    }
                }
            } else if (input.equals("d") || input.equals("l")) { // 'd' or 'l' for right
                if (currentCol < board.getWidth() - 1) {
                    // Check if there's an existing column in the target position
                    int landingHeight = findLandingHeight(currentCol + 1);
                    
                    // If landing height is high enough that we'd land on top of the column
                    if (landingHeight >= 0 && landingHeight < topRow + pieceLength) {
                        // Clear current position
                        for (int i = 0; i < pieceLength; i++) {
                            int row = topRow + i;
                            if (row >= 0 && row < board.getHeight()) {
                                board.placeTile(null, row, currentCol);
                            }
                        }
                        
                        // Place the piece on top of the existing column
                        int newTopRow = landingHeight - pieceLength + 1;
                        for (int i = 0; i < pieceLength; i++) {
                            int row = newTopRow + i;
                            if (row >= 0 && row < board.getHeight()) {
                                board.placeTile(columnPiece.get(i), row, currentCol + 1);
                            }
                        }
                        
                        // Update column height
                        int newHeight = 0;
                        for (int row = 0; row < board.getHeight(); row++) {
                            if (!board.isEmpty(row, currentCol + 1)) {
                                newHeight++;
                            }
                        }
                        columnHeights[currentCol + 1] = newHeight;
                        
                        drawBoard();
                        return true; // Piece has landed
                    }
                    // Normal right movement if there's no collision
                    else if (!isCollision(topRow, currentCol + 1, pieceLength)) {
                        // Clear current position
                        for (int i = 0; i < pieceLength; i++) {
                            int row = topRow + i;
                            if (row >= 0 && row < board.getHeight()) {
                                board.placeTile(null, row, currentCol);
                            }
                        }
                        currentCol++;
                    }
                }
            } else if (input.equals("s") || input.equals("k")) { // 's' or 'k' for rotate down
                // Rotate the tiles down (bottom becomes top, others move down)
                for (int i = 0; i < pieceLength; i++) {
                    int row = topRow + i;
                    if (row >= 0 && row < board.getHeight()) {
                        board.placeTile(null, row, currentCol);
                    }
                }
                
                // Perform the rotation: bottom -> top, middle -> bottom, top -> middle
                ITile temp = columnPiece.get(pieceLength - 1); // Save the bottom tile
                for (int i = pieceLength - 1; i > 0; i--) {
                    columnPiece.set(i, columnPiece.get(i - 1)); // Move each tile down
                }
    
                columnPiece.set(0, temp); // Move saved bottom to top
                continue;
            }
            
            // Only move down if we didn't rotate
            topRow++;
        }
    }
    
    /**
     * Check if placing the piece would cause a collision
     * @param topRow The top row of the piece
     * @param col The column of the piece
     * @param pieceLength The length of the piece
     * @return true if there would be a collision, false otherwise
     */
    private boolean isCollision(int topRow, int col, int pieceLength) {
        // Check if piece would go out of bounds at the bottom
        if (topRow + pieceLength > board.getHeight()) {
            return true;
        }

        // Check if any part of the piece would collide with existing tiles
        for (int i = 0; i < pieceLength; i++) {
            int row = topRow + i;
            if (row >= 0 && row < board.getHeight()) {
                // Only check for collision with tiles that aren't part of the current piece
                if (board.getTile(row, col) != null) {
                    boolean isCurrentPiece = false;
                    for (int j = 0; j < pieceLength; j++) {
                        int currentPieceRow = topRow - 1 + j;
                        if (currentPieceRow == row) {
                            isCurrentPiece = true;
                            break;
                        }
                    }
                    if (!isCurrentPiece) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

    /**
     * Special collision check for horizontal movement
     * When moving horizontally, we need to check if the piece would land on top of an existing column
     */
    private int findLandingHeight(int col) {
        // Find the first empty row from the bottom
        for (int row = board.getHeight() - 1; row >= 0; row--) {
            if (board.isEmpty(row, col)) {
                return row;
            }
        }
        return -1;
    }

    /**
     * Calculate the score based on the number of tiles cleared
     * @param tilesCleared The number of tiles cleared
     * @return The calculated score
     */
    private int calculateScore(int tilesCleared) {
        if (tilesCleared < 3) return 0;
        
        // Base score for 3 tiles
        int calculatedScore = BASE_SCORE;
        
        // Bonus for additional tiles
        if (tilesCleared > 3) {
            calculatedScore += (tilesCleared - 3) * MATCH_BONUS;
        }
        
        return calculatedScore;
    }
    
    /**
     * Check for matches and clear them
     */
    private void checkAndClearMatches() {
        boolean matchesFound = false;
        boolean[][] tilesToClear = new boolean[board.getHeight()][board.getWidth()];
        
        // Check for horizontal matches (3 or more in a row)
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth() - 2; col++) {
                ITile tile = board.getTile(row, col);
                if (tile != null) {
                    // Find the maximum length of the match
                    int matchLength = findMaxMatchLength(row, col, 0, 1);
                    if (matchLength >= 3) {
                        for (int i = 0; i < matchLength; i++) {
                            tilesToClear[row][col + i] = true;
                        }
                        matchesFound = true;
                    }
                }
            }
        }
        
        // Check for vertical matches (3 or more in a column)
        for (int row = 0; row < board.getHeight() - 2; row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                ITile tile = board.getTile(row, col);
                if (tile != null) {
                    // Find the maximum length of the match
                    int matchLength = findMaxMatchLength(row, col, 1, 0);
                    if (matchLength >= 3) {
                        for (int i = 0; i < matchLength; i++) {
                            tilesToClear[row + i][col] = true;
                        }
                        matchesFound = true;
                    }
                }
            }
        }
        
        // Check for diagonal matches (top-left to bottom-right)
        for (int row = 0; row < board.getHeight() - 2; row++) {
            for (int col = 0; col < board.getWidth() - 2; col++) {
                ITile tile = board.getTile(row, col);
                if (tile != null) {
                    // Find the maximum length of the match
                    int matchLength = findMaxMatchLength(row, col, 1, 1);
                    if (matchLength >= 3) {
                        for (int i = 0; i < matchLength; i++) {
                            tilesToClear[row + i][col + i] = true;
                        }
                        matchesFound = true;
                    }
                }
            }
        }
        
        // Check for diagonal matches (top-right to bottom-left)
        for (int row = 0; row < board.getHeight() - 2; row++) {
            for (int col = 2; col < board.getWidth(); col++) {
                ITile tile = board.getTile(row, col);
                if (tile != null) {
                    // Find the maximum length of the match
                    int matchLength = findMaxMatchLength(row, col, 1, -1);
                    if (matchLength >= 3) {
                        for (int i = 0; i < matchLength; i++) {
                            tilesToClear[row + i][col - i] = true;
                        }
                        matchesFound = true;
                    }
                }
            }
        }
        
        // If matches were found, clear the tiles and move others down
        if (matchesFound) {
            // Count total tiles to clear for scoring
            int totalTilesCleared = 0;
            for (int row = 0; row < board.getHeight(); row++) {
                for (int col = 0; col < board.getWidth(); col++) {
                    if (tilesToClear[row][col]) {
                        totalTilesCleared++;
                    }
                }
            }
            
            // Calculate score
            int matchScore = calculateScore(totalTilesCleared);
            score += matchScore;
            
            // Display the matches before clearing
            replaceMatches(tilesToClear);
            
            // Show score earned
            System.out.println("Cleared " + totalTilesCleared + " tiles! +" + matchScore + " points!");
            
            // Clear matched tiles
            for (int col = 0; col < board.getWidth(); col++) {
                // Count how many tiles to remove in this column
                int tilesRemoved = 0;
                for (int row = 0; row < board.getHeight(); row++) {
                    if (tilesToClear[row][col]) {
                        tilesRemoved++;
                        board.placeTile(null, row, col);
                    }
                }
                
                // Update column height
                if (tilesRemoved > 0) {
                    columnHeights[col] -= tilesRemoved;
                    if (columnHeights[col] < 0) columnHeights[col] = 0;
                }
            }
            
            applyGravity();
            MatchTiles();
        }
    }
    
    /**
     * Find the maximum length of a match starting from (row,col) and moving in the direction (rowDir,colDir)
     * @param row Starting row
     * @param col Starting column
     * @param rowDir Row direction (0 for horizontal, 1 for vertical or diagonal)
     * @param colDir Column direction (0 for vertical, 1 for right diagonal, -1 for left diagonal)
     * @return The maximum length of the match (1 if no match)
     */
    private int findMaxMatchLength(int row, int col, int rowDir, int colDir) {
        ITile firstTile = board.getTile(row, col);
        if (firstTile == null) return 0;
        
        int maxLength = 1;
        String value = firstTile.GetValue();
        
        // Keep checking tiles in the specified direction until we find one that doesn't match
        while (true) {
            int newRow = row + maxLength * rowDir;
            int newCol = col + maxLength * colDir;
            
            // Check bounds
            if (newRow < 0 || newRow >= board.getHeight() || newCol < 0 || newCol >= board.getWidth()) {
                break;
            }
            
            ITile nextTile = board.getTile(newRow, newCol);
            if (nextTile == null || !nextTile.GetValue().equals(value)) {
                break;
            }
            
            maxLength++;
        }
        
        return maxLength;
    }

    /**
     * Replace matches before clearing them by replacing them with 'X' characters
     */
    private void replaceMatches(boolean[][] tilesToClear) {
        // Create a copy of the board for replacement
        ITile[][] boardCopy = new ITile[board.getHeight()][board.getWidth()];
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                boardCopy[row][col] = board.getTile(row, col);
                
                // If this is a tile to clear, create a temporary copy for the visual effect
                if (tilesToClear[row][col] && boardCopy[row][col] != null) {
                    // Use the existing tile but change its value to 'X'
                    ITile originalTile = board.getTile(row, col);
                    originalTile.SetValue("X");
                }
            }
        }
        
        drawBoard();
        System.out.println("Matches found! Clearing...");
        
        // Pause briefly to show the highlights
        try {
            TimeUnit.MILLISECONDS.sleep(875);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Restore the original board (the matches will be cleared in the calling method)
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                // If this was a tile to clear, restore its original value
                if (tilesToClear[row][col] && boardCopy[row][col] != null) {
                    // We'll place the original tile back - it will be cleared in the calling method
                    board.placeTile(boardCopy[row][col], row, col);
                }
            }
        }
    }

    /**
     * Apply gravity to make tiles fall down to fill empty spaces
     */
    private void applyGravity() {
        // Process each column independently
        for (int col = 0; col < board.getWidth(); col++) {
            // Start from the bottom and move upward
            int emptyRow = -1;
            
            for (int row = board.getHeight() - 1; row >= 0; row--) {
                if (board.isEmpty(row, col) && emptyRow == -1) {
                    // Found an empty cell
                    emptyRow = row;
                } else if (!board.isEmpty(row, col) && emptyRow != -1) {
                    // Found a tile that needs to fall
                    board.placeTile(board.getTile(row, col), emptyRow, col);
                    board.placeTile(null, row, col);
                    
                    // Look for the next empty cell starting from the current one
                    emptyRow--;
                }
            }
        }
        
        // Recalculate column heights
        recalculateColumnHeights();
        
        // Show the updated board
        drawBoard();
        System.out.println("Tiles have fallen to fill gaps.");
        
        // Pause briefly to show the movement
        try {
            TimeUnit.MILLISECONDS.sleep(675);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Recalculate the height of each column
     */
    private void recalculateColumnHeights() {
        for (int col = 0; col < board.getWidth(); col++) {
            columnHeights[col] = 0;
            for (int row = 0; row < board.getHeight(); row++) {
                if (!board.isEmpty(row, col)) {
                    columnHeights[col]++;
                }
            }
        }
    }

    
    /**
     * Draw the board in the terminal
     */
    private void drawBoard() {
        clearScreen();
        System.out.println("Columns Game");
        System.out.println("------------");
        
        // Draw the score at the top
        System.out.println("Score: " + score);
        System.out.println();
        
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
            
            // Add score and other info on the side of the board
            if (row == 2) {
                System.out.println("|   Score: " + score);
            } else if (row == 3) {
                System.out.println("|   -----");
            } else if (row == 5) {
                System.out.println("|   Controls:");
            } else if (row == 6) {
                System.out.println("|   a - left");
            } else if (row == 7) {
                System.out.println("|   d - right");
            } else if (row == 8) {
                System.out.println("|   s - rotate");
            }
            else if (row == 9) {
                System.out.println("|   Enter - drop");
            } else {
                System.out.println("|");
            }
        }
        
        // Draw the bottom border
        System.out.print("+");
        for (int col = 0; col < board.getWidth(); col++) {
            System.out.print("-");
        }
        System.out.println("+");
    }
    
    /**
     * Clear the terminal screen
     */
    private void clearScreen() {
        System.out.print("\033[2J\033[1;1H");
        System.out.flush();
    }
}