package columns;

import GameEngine.ITile;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ColumnsGameEngine2pINP extends GameEngine.GameEngine {
    private ColumnsBoard board1;
    private ColumnsBoard board2;
    private TileFactory tileFactory;
    private boolean gameRunning;
    private static final int INVISIBLE_HEIGHT = 16;
    private int score1 = 0;
    private int score2 = 0;
    private final int BASE_SCORE = 150;
    private final int MATCH_BONUS = 75;
    private boolean twoPlayerMode = false;
    private boolean player1Lost = false;
    private boolean player2Lost = false;
    private int[] columnHeights1;
    private int[] columnHeights2;
    private InputHandler inputHandler;
    
    public ColumnsGameEngine2pINP(InputHandler inputHandler) {
        this.board1 = new ColumnsBoard();
        this.board2 = new ColumnsBoard();
        this.tileFactory = new TileFactory();
        this.gameRunning = true;
        this.columnHeights1 = new int[board1.getWidth()];
        this.columnHeights2 = new int[board2.getWidth()];
        this.inputHandler = inputHandler;
        
        for (int i = 0; i < columnHeights1.length; i++) {
            columnHeights1[i] = 0;
            columnHeights2[i] = 0;
        }
    }

    @Override
    public void MatchTiles() {
        checkAndClearMatches(board1, columnHeights1, 1); // Check matches for player 1
        if (twoPlayerMode) {
            checkAndClearMatches(board2, columnHeights2, 2); // Check matches for player 2
        }
    }

    @Override
    public boolean Action(String command) {
        List<ITile> columnPiece1 = null;
        List<ITile> columnPiece2 = null;
        
        if (!player1Lost) {
            columnPiece1 = tileFactory.createColumn();
        }
        
        if (!player2Lost && twoPlayerMode) {
            columnPiece2 = tileFactory.createColumn();
        }
        
        int middleCol = board1.getWidth() / 2;
        
        if (twoPlayerMode) {
            boolean result = handleTwoPlayerTurn(columnPiece1, columnPiece2, middleCol);
            if (!result) {
                gameRunning = false;
                drawBoards();
                determineWinner();
                return false;
            }
            return true;
        } else {
            boolean pieceDropped = handleSinglePlayerTurn(columnPiece1, middleCol);
            if (!pieceDropped) {
                gameRunning = false;
                drawBoards();
                System.out.println("Game Over! A column has exceeded the height limit. Final Score: " + score1);
                return false;
            }
            
            checkAndClearMatches(board1, columnHeights1, 1);
            return true;
        }
    }
    
    public void startGame() {
        System.out.println("Welcome to Columns!");
        System.out.println("Select game mode:");
        System.out.println("1. Single Player");
        System.out.println("2. Two Players");
        
        int choice = inputHandler.getIntChoice(1, 2);
        
        twoPlayerMode = (choice == 2);
        
        System.out.println("\nPieces will fall one block at a time.");
        if (twoPlayerMode) {
            System.out.println("Player 1 Controls: 'a' - left, 'd' - right, 's' - rotate, Enter - down");
            System.out.println("Player 2 Controls: 'j' - left, 'l' - right, 'k' - rotate, Enter - down");
        } else {
            System.out.println("Controls: 'a'=left, 'd'=right, 's'=rotate, 'Enter'=down");
        }
        System.out.println("Press Enter to start the game (press Ctrl+C to quit)...");
        inputHandler.waitForEnter();
        
        gameRunning = true;
        
        while (gameRunning) {
            boolean success = Action("");
            
            if (!success) {
                gameRunning = false;
                System.out.println("Press Enter to exit...");
                inputHandler.waitForEnter();
                break;
            }
        }
    }
    
    private void determineWinner() {
        if (!twoPlayerMode) {
            System.out.println("Game Over! Final Score: " + score1);
            return;
        }
        
        System.out.println("\n=== GAME OVER ===");
        if (score1 > score2) {
            System.out.println("Player 1 wins with a higher score!");
        } else if (score2 > score1) {
            System.out.println("Player 2 wins with a higher score!");
        } else {
            System.out.println("It's a tie!");
        }
        
        System.out.println("Player 1 Score: " + score1);
        System.out.println("Player 2 Score: " + score2);
    }
    
    private boolean handleTwoPlayerTurn(List<ITile> columnPiece1, List<ITile> columnPiece2, int startCol) {     
        if (!player1Lost) {
            System.out.println("\n--- PLAYER 1'S TURN ---");
            boolean player1Success = handlePlayerTurn(board1, columnHeights1, columnPiece1, startCol, 1);
            if (!player1Success) {
                player1Lost = true;
                System.out.println("Player 1 has lost! Player 2 can continue playing.");
            } else {
                checkAndClearMatches(board1, columnHeights1, 1);
            }
        }

        if (!player2Lost) {
            System.out.println("\n--- PLAYER 2'S TURN ---");
            boolean player2Success = handlePlayerTurn(board2, columnHeights2, columnPiece2, startCol, 2);
            if (!player2Success) {
                player2Lost = true;
                System.out.println("Player 2 has lost!");
            } else {
                checkAndClearMatches(board2, columnHeights2, 2);
            }
        }
        
        // If both players have lost, end the game
        if (player1Lost && player2Lost) {
            return false;
        }
        
        return true;
    }

    private boolean handleSinglePlayerTurn(List<ITile> columnPiece, int startCol) {
        return handlePlayerTurn(board1, columnHeights1, columnPiece, startCol, 1);
    }

    private void rotatePiece(ColumnsBoard board, List<ITile> columnPiece, int topRow, int currentCol, int pieceLength) {
        // Clear the current position
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
    }

    private boolean handlePlayerTurn(ColumnsBoard board, int[] columnHeights, List<ITile> columnPiece, 
                                     int startCol, int playerNum) {
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
            
            drawBoards();
            
            // Check for collision for the next position below
            if (isCollision(board, topRow + 1, currentCol, pieceLength)) {
                for (int i = 0; i < pieceLength; i++) {
                    int row = topRow + i;
                    if (row >= 0 && row < board.getHeight()) {
                        board.placeTile(columnPiece.get(i), row, currentCol);
                    }
                }
                
                // Update column height 
                int newHeight = 0;
                for (int row = 0; row < board.getHeight(); row++) {
                    if (!board.isEmpty(row, currentCol)) {
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
            
            // Get user input for the next move
            String input = inputHandler.getNextMove(playerNum);
            
            // Handle controls based on player number
            if (playerNum == 1) {
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
            } else { // Player 2
                if (input.equals("j")) { // Left
                    if (moveLeft(board, columnPiece, topRow, currentCol, pieceLength, columnHeights)) {
                        currentCol--;
                    }
                } else if (input.equals("l")) { // Right
                    if (moveRight(board, columnPiece, topRow, currentCol, pieceLength, columnHeights)) {
                        currentCol++;
                    }
                } else if (input.equals("k")) { // Rotate
                    rotatePiece(board, columnPiece, topRow, currentCol, pieceLength);
                    continue;
                }
            }
            
            // Move down
            topRow++;
        }
        
        return true;
    }

    private boolean moveLeft(ColumnsBoard board, List<ITile> columnPiece, int topRow, int currentCol, 
                        int pieceLength, int[] columnHeights) {
        if (currentCol <= 0) return false;
        int landingHeight = findLandingHeight(board, currentCol - 1);
        
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
            
            drawBoards();
            return false;
        }
        // Normal left movement if there's no collision
        else if (!isCollision(board, topRow, currentCol - 1, pieceLength)) {
            // Clear current position
            for (int i = 0; i < pieceLength; i++) {
                int row = topRow + i;
                if (row >= 0 && row < board.getHeight()) {
                    board.placeTile(null, row, currentCol);
                }
            }
            return true;
        }
        
        return false;
    }

    private boolean moveRight(ColumnsBoard board, List<ITile> columnPiece, int topRow, int currentCol, 
                        int pieceLength, int[] columnHeights) {
        if (currentCol >= board.getWidth() - 1) return false;
        int landingHeight = findLandingHeight(board, currentCol + 1);
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
            
            drawBoards();
            return false;
        }
        // Normal right movement if there's no collision
        else if (!isCollision(board, topRow, currentCol + 1, pieceLength)) {
            // Clear current position
            for (int i = 0; i < pieceLength; i++) {
                int row = topRow + i;
                if (row >= 0 && row < board.getHeight()) {
                    board.placeTile(null, row, currentCol);
                }
            }
            return true;
        }
        
        return false;
    }
    
    private boolean isCollision(ColumnsBoard board, int topRow, int col, int pieceLength) {
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

    private int findLandingHeight(ColumnsBoard board, int col) {
        // Find the first empty row from the bottom
        for (int row = board.getHeight() - 1; row >= 0; row--) {
            if (board.isEmpty(row, col)) {
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
    
    private void checkAndClearMatches(ColumnsBoard board, int[] columnHeights, int playerNum) {
        boolean matchesFound = false;
        boolean[][] tilesToClear = new boolean[board.getHeight()][board.getWidth()];
        
        // Check for horizontal matches (3 or more in a row)
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth() - 2; col++) {
                ITile tile = board.getTile(row, col);
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
        for (int row = 0; row < board.getHeight() - 2; row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                ITile tile = board.getTile(row, col);
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
        for (int row = 0; row < board.getHeight() - 2; row++) {
            for (int col = 0; col < board.getWidth() - 2; col++) {
                ITile tile = board.getTile(row, col);
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
        for (int row = 0; row < board.getHeight() - 2; row++) {
            for (int col = 2; col < board.getWidth(); col++) {
                ITile tile = board.getTile(row, col);
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
            for (int row = 0; row < board.getHeight(); row++) {
                for (int col = 0; col < board.getWidth(); col++) {
                    if (tilesToClear[row][col]) {
                        totalTilesCleared++;
                    }
                }
            }
            
            // Calculate score
            int matchScore = calculateScore(totalTilesCleared);
            if (playerNum == 1) {
                score1 += matchScore;
            } else {
                score2 += matchScore;
            }
            
            // Display the matches before clearing
            replaceMatches(board, tilesToClear, playerNum);
            
            // Show score earned
            System.out.println("Player " + playerNum + " cleared " + totalTilesCleared + " tiles! +" + matchScore + " points!");
            
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
            
            applyGravity(board, columnHeights, playerNum);
            
            // Recursively check for more matches
            checkAndClearMatches(board, columnHeights, playerNum);
        }
    }
    
    private int findMaxMatchLength(ColumnsBoard board, int row, int col, int rowDir, int colDir) {
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

    private void replaceMatches(ColumnsBoard board, boolean[][] tilesToClear, int playerNum) {
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
        
        drawBoards();
        System.out.println("Player " + playerNum + " found matches! Clearing...");
        
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

    private void applyGravity(ColumnsBoard board, int[] columnHeights, int playerNum) {
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
        recalculateColumnHeights(board, columnHeights);
        
        // Show the updated board
        drawBoards();
        System.out.println("Player " + playerNum + " tiles have fallen to fill gaps.");
        
        // Pause briefly to show the movement
        try {
            TimeUnit.MILLISECONDS.sleep(675);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void recalculateColumnHeights(ColumnsBoard board, int[] columnHeights) {
        for (int col = 0; col < board.getWidth(); col++) {
            columnHeights[col] = 0;
            for (int row = 0; row < board.getHeight(); row++) {
                if (!board.isEmpty(row, col)) {
                    columnHeights[col]++;
                }
            }
        }
    }

    private void drawBoards() {
        clearScreen();
        
        if (!twoPlayerMode) {
            // Single player mode
            System.out.println("Columns Game - Score: " + score1);
            System.out.println("------------");
            
            // Draw the board
            for (int row = 0; row < board1.getHeight(); row++) {
                System.out.print("|");
                for (int col = 0; col < board1.getWidth(); col++) {
                    ITile tile = board1.getTile(row, col);
                    if (tile == null) {
                        System.out.print(" ");
                    } else {
                        System.out.print(tile.GetValue());
                    }
                }

                if (row == 2) {
                    System.out.println("|   Score: " + score1);
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
                } else {
                    System.out.println("|");
                }
            }
            
            // Draw the bottom border
            System.out.print("+");
            for (int col = 0; col < board1.getWidth(); col++) {
                System.out.print("-");
            }
            System.out.println("+");
        } else {
            // Two player mode
            System.out.println("Columns Game - Two Player Mode");
            System.out.println("Player 1: " + score1 + " points" + (player1Lost ? " (OUT)" : "") + 
                             "          Player 2: " + score2 + " points" + (player2Lost ? " (OUT)" : ""));
            System.out.println("--------------------");
            
            // Draw the boards side by side
            for (int row = 0; row < board1.getHeight(); row++) {
                // Player 1 board dot replacement
                System.out.print("|");
                for (int col = 0; col < board1.getWidth(); col++) {
                    ITile tile = board1.getTile(row, col);
                    if (tile == null) {
                        System.out.print(" ");
                    } else {
                        System.out.print(player1Lost ? "." : tile.GetValue());
                    }
                }
                System.out.print("|    ");
                
                // Player 2 board dot replacement
                System.out.print("|");
                for (int col = 0; col < board2.getWidth(); col++) {
                    ITile tile = board2.getTile(row, col);
                    if (tile == null) {
                        System.out.print(" ");
                    } else {
                        System.out.print(player2Lost ? "." : tile.GetValue());
                    }
                }
                System.out.println("|");
            }
            
            // Draw the bottom borders
            System.out.print("+");
            for (int col = 0; col < board1.getWidth(); col++) {
                System.out.print("-");
            }
            System.out.print("+    +");
            for (int col = 0; col < board2.getWidth(); col++) {
                System.out.print("-");
            }
            System.out.println("+");
            
            // Controls reminder (only show for active players)
            if (!player1Lost) {
                System.out.println("\nPlayer 1: a=left, d=right, s=rotate, Enter=down");
            }
            if (!player2Lost) {
                System.out.println("Player 2: j=left, l=right, k=rotate, Enter=down");
            }
        }
    }
    
    private void clearScreen() {
        System.out.print("\033[2J\033[1;1H");
        System.out.flush();
    }
}