package Games;

import GameModules.Board;
import GameModules.ITile;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ColumnsGameEngine extends GameModules.GameEngine{
    private TileFactory tileFactory;
    private boolean gameRunning;
    private static final int INVISIBLE_HEIGHT = 16; // Invisible height limit
    private int score = 0;
    private final int BASE_SCORE = 150;
    private final int MATCH_BONUS = 75; // For every match greater than 3, each bonus tile will receive an extra 75 points

    private boolean twoPlayerMode = false;
    private boolean player1Lost = false;
    private int[] columnHeights1;
    // Track the height of each column separately from the visible gameBoard
    private int[] columnHeights;
    
    /**
     * Constructor
     */
    public ColumnsGameEngine(Integer id) {
    	super("Columns", id);
        this.gameBoard = new Board(6, 13);
        this.tileFactory = new TileFactory();
        this.columnHeights = new int[gameBoard.GetWidth()];
        
        for (int i = 0; i < columnHeights.length; i++) {
            columnHeights[i] = 0;
        }
    }

    @Override
    public void MatchTiles() {
        checkAndClearMatches(this.gameBoard, columnHeights1, 1); // Check matches for player 1
    }

    @Override
    public boolean Action(String command) {
        List<ITile> columnPiece1 = null;
        List<ITile> columnPiece2 = null;
        
        if (!player1Lost) {
            columnPiece1 = tileFactory.createColumn();
        }
        
        int middleCol = this.gameBoard.GetWidth() / 2;
        
            boolean pieceDropped = handleSinglePlayerTurn(columnPiece1, middleCol, command);
            if (!pieceDropped) {
                gameRunning = false;
                
                System.out.println("Game Over! A column has exceeded the height limit. Final Score: " + this.score);
                return false;
            }
            
            checkAndClearMatches(this.gameBoard, columnHeights1, 1);
            return true;
        
    }
    
    public void startGame() {
        System.out.println("Welcome to Columns!");
        
        gameRunning = true;
        
        while (gameRunning) {
            boolean success = Action("");
            
            if (!success) {
                gameRunning = false;
                System.out.println("Press Enter to exit...");
                break;
            }
        }
    }
    
    private void determineWinner() {
        System.out.println("Player 1 Score: " + this.score);
    }

    private boolean handleSinglePlayerTurn(List<ITile> columnPiece, int startCol, String command) {
        return handlePlayerTurn(this.gameBoard, columnHeights1, columnPiece, startCol, command);
    }

    private void rotatePiece(Board board, List<ITile> columnPiece, int topRow, int currentCol, int pieceLength) {
        // Clear the current position
        for (int i = 0; i < pieceLength; i++) {
            int row = topRow + i;
            if (row >= 0 && row < board.GetHeight()) {
                board.PlaceTile(null, row, currentCol);
            }
        }
        
        // Perform the rotation: bottom -> top, middle -> bottom, top -> middle
        ITile temp = columnPiece.get(pieceLength - 1); // Save the bottom tile
        for (int i = pieceLength - 1; i > 0; i--) {
            columnPiece.set(i, columnPiece.get(i - 1)); // Move each tile down
        }
        columnPiece.set(0, temp); // Move saved bottom to top
    }

    private boolean handlePlayerTurn(Board board, int[] columnHeights, List<ITile> columnPiece, 
                                     int startCol, String input) {
        int pieceLength = columnPiece.size();
        int currentCol = startCol;
        int topRow = -pieceLength;
        
        // Check if the starting column is already too high
        if (columnHeights[currentCol] >= INVISIBLE_HEIGHT - pieceLength) {
            return false;
        }
        
        boolean pieceLanded = false;
        
        while (!pieceLanded) {
            // Clear the previous position if needed
            if (topRow > -pieceLength) {
                for (int i = 0; i < pieceLength; i++) {
                    int row = topRow + i - 1;
                    if (row >= 0 && row < board.GetHeight()) {
                        board.PlaceTile(null, row, currentCol);
                    }
                }
            }
            
            // Place the piece at the new position
            for (int i = 0; i < pieceLength; i++) {
                int row = topRow + i;
                if (row >= 0 && row < board.GetHeight()) {
                    board.PlaceTile(columnPiece.get(i), row, currentCol);
                }
            }
            
            
            
            // Check for collision for the next position below
            if (isCollision(board, topRow + 1, currentCol, pieceLength)) {
                for (int i = 0; i < pieceLength; i++) {
                    int row = topRow + i;
                    if (row >= 0 && row < board.GetHeight()) {
                        board.PlaceTile(columnPiece.get(i), row, currentCol);
                    }
                }
                
                // Update column height 
                int newHeight = 0;
                for (int row = 0; row < board.GetHeight(); row++) {
                    if (!board.GetSpot(row, currentCol).IsEmpty()) {
                        newHeight++;
                    }
                }
                columnHeights[currentCol] = newHeight;
                
                // Check if the game is over
                if (columnHeights[currentCol] >= INVISIBLE_HEIGHT) {
                    return false;
                }
                
                pieceLanded = true;
                return true;
            }
            
            
            // Handle controls based on player number
                if (input.equals("a")) { // Left
                    if (moveLeft(board, columnPiece, topRow, currentCol, pieceLength, columnHeights)) {
                        currentCol--;
                    }
                } else if (input.equals("d")) { // Right
                    if (moveRight(board, columnPiece, topRow, currentCol, pieceLength, columnHeights)) {
                        currentCol++;
                    }
                } else if (input.equals("s")) { // Rotate
                    rotatePiece(board, columnPiece, topRow, currentCol, pieceLength);
                    continue;
                }
            
            // Move down
            topRow++;
        }
        
        return true;
    }

    private boolean moveLeft(Board board, List<ITile> columnPiece, int topRow, int currentCol, 
                        int pieceLength, int[] columnHeights) {
        if (currentCol <= 0) return false;
        int landingHeight = findLandingHeight(board, currentCol - 1);
        
        if (landingHeight >= 0 && landingHeight < topRow + pieceLength) {
            // Clear current position
            for (int i = 0; i < pieceLength; i++) {
                int row = topRow + i;
                if (row >= 0 && row < board.GetHeight()) {
                    board.PlaceTile(null, row, currentCol);
                }
            }
            // Place the piece on top of the existing column
            int newTopRow = landingHeight - pieceLength + 1;
            for (int i = 0; i < pieceLength; i++) {
                int row = newTopRow + i;
                if (row >= 0 && row < board.GetHeight()) {
                    board.PlaceTile(columnPiece.get(i), row, currentCol - 1);
                }
            }
            // Update column height
            int newHeight = 0;
            for (int row = 0; row < board.GetHeight(); row++) {
                if (!board.GetSpot(row, currentCol - 1).IsEmpty()) {
                    newHeight++;
                }
            }
            columnHeights[currentCol - 1] = newHeight;
            
            
            return false;
        }
        // Normal left movement if there's no collision
        else if (!isCollision(board, topRow, currentCol - 1, pieceLength)) {
            // Clear current position
            for (int i = 0; i < pieceLength; i++) {
                int row = topRow + i;
                if (row >= 0 && row < board.GetHeight()) {
                    board.PlaceTile(null, row, currentCol);
                }
            }
            return true;
        }
        
        return false;
    }

    private boolean moveRight(Board board, List<ITile> columnPiece, int topRow, int currentCol, 
                        int pieceLength, int[] columnHeights) {
        if (currentCol >= board.GetWidth() - 1) return false;
        int landingHeight = findLandingHeight(board, currentCol + 1);
        if (landingHeight >= 0 && landingHeight < topRow + pieceLength) {
            // Clear current position
            for (int i = 0; i < pieceLength; i++) {
                int row = topRow + i;
                if (row >= 0 && row < board.GetHeight()) {
                    board.PlaceTile(null, row, currentCol);
                }
            }
            
            // Place the piece on top of the existing column
            int newTopRow = landingHeight - pieceLength + 1;
            for (int i = 0; i < pieceLength; i++) {
                int row = newTopRow + i;
                if (row >= 0 && row < board.GetHeight()) {
                    board.PlaceTile(columnPiece.get(i), row, currentCol + 1);
                }
            }
            
            // Update column height
            int newHeight = 0;
            for (int row = 0; row < board.GetHeight(); row++) {
                if (!board.GetSpot(row, currentCol + 1).IsEmpty()) {
                    newHeight++;
                }
            }
            columnHeights[currentCol + 1] = newHeight;
            
            
            return false;
        }
        // Normal right movement if there's no collision
        else if (!isCollision(board, topRow, currentCol + 1, pieceLength)) {
            // Clear current position
            for (int i = 0; i < pieceLength; i++) {
                int row = topRow + i;
                if (row >= 0 && row < board.GetHeight()) {
                    board.PlaceTile(null, row, currentCol);
                }
            }
            return true;
        }
        
        return false;
    }
    
    private boolean isCollision(Board board, int topRow, int col, int pieceLength) {
        // Check if piece would go out of bounds at the bottom
        if (topRow + pieceLength > board.GetHeight()) {
            return true;
        }

        // Check if any part of the piece would collide with existing tiles
        for (int i = 0; i < pieceLength; i++) {
            int row = topRow + i;
            if (row >= 0 && row < board.GetHeight()) {
                // Only check for collision with tiles that aren't part of the current piece
                if (board.GetFirstTile(row, col) != null) {
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

    private int findLandingHeight(Board board, int col) {
        // Find the first empty row from the bottom
        for (int row = board.GetHeight() - 1; row >= 0; row--) {
            if (board.GetSpot(row, col).IsEmpty()) {
                return row;
            }
        }
        return -1;
    }

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
    
    private void checkAndClearMatches(Board board, int[] columnHeights, int playerNum) {
        boolean matchesFound = false;
        boolean[][] tilesToClear = new boolean[board.GetHeight()][board.GetWidth()];
        
        // Check for horizontal matches (3 or more in a row)
        for (int row = 0; row < board.GetHeight(); row++) {
            for (int col = 0; col < board.GetWidth() - 2; col++) {
                ITile tile = board.GetFirstTile(row, col);
                if (tile != null) {
                    // Find the maximum length of the match
                    int matchLength = findMaxMatchLength(board, row, col, 0, 1);
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
        for (int row = 0; row < board.GetHeight() - 2; row++) {
            for (int col = 0; col < board.GetWidth(); col++) {
                ITile tile = board.GetFirstTile(row, col);
                if (tile != null) {
                    // Find the maximum length of the match
                    int matchLength = findMaxMatchLength(board, row, col, 1, 0);
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
        for (int row = 0; row < board.GetHeight() - 2; row++) {
            for (int col = 0; col < board.GetWidth() - 2; col++) {
                ITile tile = board.GetFirstTile(row, col);
                if (tile != null) {
                    // Find the maximum length of the match
                    int matchLength = findMaxMatchLength(board, row, col, 1, 1);
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
        for (int row = 0; row < board.GetHeight() - 2; row++) {
            for (int col = 2; col < board.GetWidth(); col++) {
                ITile tile = board.GetFirstTile(row, col);
                if (tile != null) {
                    // Find the maximum length of the match
                    int matchLength = findMaxMatchLength(board, row, col, 1, -1);
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
            for (int row = 0; row < board.GetHeight(); row++) {
                for (int col = 0; col < board.GetWidth(); col++) {
                    if (tilesToClear[row][col]) {
                        totalTilesCleared++;
                    }
                }
            }
            
            // Calculate score
            int matchScore = calculateScore(totalTilesCleared);
            this.score += matchScore;
            
            // Display the matches before clearing
            replaceMatches(board, tilesToClear, playerNum);
            
            // Show score earned
            System.out.println("Player " + playerNum + " cleared " + totalTilesCleared + " tiles! +" + matchScore + " points!");
            
            // Clear matched tiles
            for (int col = 0; col < board.GetWidth(); col++) {
                // Count how many tiles to remove in this column
                int tilesRemoved = 0;
                for (int row = 0; row < board.GetHeight(); row++) {
                    if (tilesToClear[row][col]) {
                        tilesRemoved++;
                        board.PlaceTile(null, row, col);
                    }
                }
                
                // Update column height
                if (tilesRemoved > 0) {
                    columnHeights[col] -= tilesRemoved;
                    if (columnHeights[col] < 0) columnHeights[col] = 0;
                }
            }
            
            applyGravity(board, columnHeights, playerNum);
            
            // Recursively check for more matches
            checkAndClearMatches(board, columnHeights, playerNum);
        }
    }
    
    private int findMaxMatchLength(Board board, int row, int col, int rowDir, int colDir) {
        ITile firstTile = board.GetFirstTile(row, col);
        if (firstTile == null) return 0;
        
        int maxLength = 1;
        String value = firstTile.GetValue();
        
        // Keep checking tiles in the specified direction until we find one that doesn't match
        while (true) {
            int newRow = row + maxLength * rowDir;
            int newCol = col + maxLength * colDir;
            
            // Check bounds
            if (newRow < 0 || newRow >= board.GetHeight() || newCol < 0 || newCol >= board.GetWidth()) {
                break;
            }
            
            ITile nextTile = board.GetFirstTile(newRow, newCol);
            if (nextTile == null || !nextTile.GetValue().equals(value)) {
                break;
            }
            
            maxLength++;
        }
        
        return maxLength;
    }

    private void replaceMatches(Board board, boolean[][] tilesToClear, int playerNum) {
        // Create a copy of the board for replacement
        ITile[][] boardCopy = new ITile[board.GetHeight()][board.GetWidth()];
        for (int row = 0; row < board.GetHeight(); row++) {
            for (int col = 0; col < board.GetWidth(); col++) {
                boardCopy[row][col] = board.GetFirstTile(row, col);
                
                // If this is a tile to clear, create a temporary copy for the visual effect
                if (tilesToClear[row][col] && boardCopy[row][col] != null) {
                    // Use the existing tile but change its value to 'X'
                    ITile originalTile = board.GetFirstTile(row, col);
                    originalTile.SetValue("X");
                }
            }
        }
        
        
        System.out.println("Player " + playerNum + " found matches! Clearing...");
        
        
        // Restore the original board (the matches will be cleared in the calling method)
        for (int row = 0; row < board.GetHeight(); row++) {
            for (int col = 0; col < board.GetWidth(); col++) {
                // If this was a tile to clear, restore its original value
                if (tilesToClear[row][col] && boardCopy[row][col] != null) {
                    // We'll place the original tile back - it will be cleared in the calling method
                    board.PlaceTile(boardCopy[row][col], row, col);
                }
            }
        }
    }

    private void applyGravity(Board board, int[] columnHeights, int playerNum) {
        // Process each column independently
        for (int col = 0; col < board.GetWidth(); col++) {
            // Start from the bottom and move upward
            int emptyRow = -1;
            
            for (int row = board.GetHeight() - 1; row >= 0; row--) {
                if (board.GetSpot(row, col).IsEmpty() && emptyRow == -1) {
                    // Found an empty cell
                    emptyRow = row;
                } else if (!board.GetSpot(row, col).IsEmpty() && emptyRow != -1) {
                    // Found a tile that needs to fall
                    board.PlaceTile(board.GetFirstTile(row, col), emptyRow, col);
                    board.PlaceTile(null, row, col);
                    
                    // Look for the next empty cell starting from the current one
                    emptyRow--;
                }
            }
        }
        
        // Recalculate column heights
        recalculateColumnHeights(board, columnHeights);
        
        // Show the updated board
        
        System.out.println("Player " + playerNum + " tiles have fallen to fill gaps.");
        
        // Pause briefly to show the movement
        try {
            TimeUnit.MILLISECONDS.sleep(675);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void recalculateColumnHeights(Board board, int[] columnHeights) {
        for (int col = 0; col < board.GetWidth(); col++) {
            columnHeights[col] = 0;
            for (int row = 0; row < board.GetHeight(); row++) {
                if (!board.GetSpot(row, col).IsEmpty()) {
                    columnHeights[col]++;
                }
            }
        }
    }

}