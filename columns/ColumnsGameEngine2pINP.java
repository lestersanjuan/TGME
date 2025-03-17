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
    
    // Game state variables
    private List<ITile> currentPiece1;
    private List<ITile> currentPiece2;
    private int currentCol1;
    private int currentCol2;
    private int topRow1;
    private int topRow2;
    private boolean needNewPiece1 = true;
    private boolean needNewPiece2 = true;
    private boolean gameInitialized = false;
    private int activePlayer = 1;
    
    public ColumnsGameEngine2pINP() {
        this.board1 = new ColumnsBoard();
        this.board2 = new ColumnsBoard();
        this.tileFactory = new TileFactory();
        this.gameRunning = true;
        this.columnHeights1 = new int[board1.getWidth()];
        this.columnHeights2 = new int[board2.getWidth()];
        
        for (int i = 0; i < columnHeights1.length; i++) {
            columnHeights1[i] = 0;
            columnHeights2[i] = 0;
        }
    }

    @Override
    public void MatchTiles() {
        checkAndClearMatches(board1, columnHeights1, 1);
        if (twoPlayerMode) {
            checkAndClearMatches(board2, columnHeights2, 2);
        }
    }

    public void initializeGame(int mode) {
        twoPlayerMode = (mode == 2);
        gameRunning = true;
        gameInitialized = true;
        
        // Initialize game state
        initializeGameState();
        
        // Draw initial board
        drawBoards();
    }
    
    private void initializeGameState() {
        // Reset scores and player status if needed
        if (!gameRunning) {
            score1 = 0;
            score2 = 0;
            player1Lost = false;
            player2Lost = false;
            
            // Reset boards
            for (int row = 0; row < board1.getHeight(); row++) {
                for (int col = 0; col < board1.getWidth(); col++) {
                    board1.placeTile(null, row, col);
                    if (twoPlayerMode) {
                        board2.placeTile(null, row, col);
                    }
                }
            }
            
            // Reset column heights
            for (int i = 0; i < columnHeights1.length; i++) {
                columnHeights1[i] = 0;
                columnHeights2[i] = 0;
            }
        }
        
        // Set up initial pieces
        if (!player1Lost) {
            currentPiece1 = tileFactory.createColumn();
            currentCol1 = board1.getWidth() / 2;
            topRow1 = -currentPiece1.size();
            needNewPiece1 = false;
        }
        
        if (twoPlayerMode && !player2Lost) {
            currentPiece2 = tileFactory.createColumn();
            currentCol2 = board2.getWidth() / 2;
            topRow2 = -currentPiece2.size();
            needNewPiece2 = false;
        }
        
        // Set active player
        activePlayer = 1;
    }

    @Override
    public boolean Action(String command) {
        // If game is not initialized or already over, return
        if (!gameInitialized || !gameRunning) {
            return gameRunning;
        }
        
        // Process the command for the active player
        if (activePlayer == 1 && !player1Lost) {
            handlePlayerCommand(command, 1);
        } else if (activePlayer == 2 && !player2Lost) {
            handlePlayerCommand(command, 2);
        }
        
        // Update game state after processing command
        updateGameState();
        
        return gameRunning;
    }
    
    private void handlePlayerCommand(String command, int playerNum) {
        // Get the player-specific variables
        ColumnsBoard board = (playerNum == 1) ? board1 : board2;
        List<ITile> currentPiece = (playerNum == 1) ? currentPiece1 : currentPiece2;
        int currentCol = (playerNum == 1) ? currentCol1 : currentCol2;
        int topRow = (playerNum == 1) ? topRow1 : topRow2;
        int pieceLength = currentPiece.size();
        
        // Clear the current piece from the board
        clearPieceFromBoard(board, currentPiece, topRow, currentCol);
        
        // Process the command
        if ((playerNum == 1 && command.equals("a")) || (playerNum == 2 && command.equals("j"))) {
            // Move left
            if (canMoveLeft(board, currentPiece, topRow, currentCol, pieceLength)) {
                currentCol--;
            }
        } else if ((playerNum == 1 && command.equals("d")) || (playerNum == 2 && command.equals("l"))) {
            // Move right
            if (canMoveRight(board, currentPiece, topRow, currentCol, pieceLength)) {
                currentCol++;
            }
        } else if ((playerNum == 1 && command.equals("s")) || (playerNum == 2 && command.equals("k"))) {
            // Rotate
            rotatePiece(currentPiece);
        } else {
            // Move down (default action for any other input)
            topRow++;
        }
        
        // Update the player-specific variables
        if (playerNum == 1) {
            currentCol1 = currentCol;
            topRow1 = topRow;
        } else {
            currentCol2 = currentCol;
            topRow2 = topRow;
        }
    }

    private void updateGameState() {
        // Update player 1's state if active
        if (activePlayer == 1 && !player1Lost) {
            updatePlayerState(1);
        }
        // Update player 2's state if active
        else if (activePlayer == 2 && !player2Lost) {
            updatePlayerState(2);
        }
        
        // Switch active player if needed in two-player mode
        if (twoPlayerMode) {
            if (activePlayer == 1 && needNewPiece1) {
                // Switch to player 2 if they haven't lost
                if (!player2Lost) {
                    activePlayer = 2;
                }
            } else if (activePlayer == 2 && needNewPiece2) {
                // Switch to player 1 if they haven't lost
                if (!player1Lost) {
                    activePlayer = 1;
                }
            }
        }
        
        // Check if the game is over - consistent logic for both modes
        if ((twoPlayerMode && player1Lost && player2Lost) || 
            (!twoPlayerMode && player1Lost)) {
            gameRunning = false;
            drawBoards();
            determineWinner();
            return; // Exit early since game is over
        }
        
        // Create new pieces if needed
        if (needNewPiece1 && !player1Lost) {
            currentPiece1 = tileFactory.createColumn();
            currentCol1 = board1.getWidth() / 2;
            topRow1 = -currentPiece1.size();
            
            // Check if the starting column is already too high
            if (columnHeights1[currentCol1] >= INVISIBLE_HEIGHT - currentPiece1.size()) {
                player1Lost = true;
                System.out.println("Player 1 has lost!");
                
                // Check if the game is over after this player lost
                if (!twoPlayerMode || (twoPlayerMode && player2Lost)) {
                    gameRunning = false;
                    drawBoards();
                    determineWinner();
                    return;
                }
            } else {
                needNewPiece1 = false;
            }
        }
        
        if (twoPlayerMode && needNewPiece2 && !player2Lost) {
            currentPiece2 = tileFactory.createColumn();
            currentCol2 = board2.getWidth() / 2;
            topRow2 = -currentPiece2.size();
            
            // Check if the starting column is already too high
            if (columnHeights2[currentCol2] >= INVISIBLE_HEIGHT - currentPiece2.size()) {
                player2Lost = true;
                System.out.println("Player 2 has lost!");
                
                // Check if the game is over after this player lost
                if (player1Lost) {
                    gameRunning = false;
                    drawBoards();
                    determineWinner();
                    return;
                }
            } else {
                needNewPiece2 = false;
            }
        }
        
        // Place the active pieces on the board and draw
        if (!player1Lost) {
            placePieceOnBoard(board1, currentPiece1, topRow1, currentCol1);
        }
        if (twoPlayerMode && !player2Lost) {
            placePieceOnBoard(board2, currentPiece2, topRow2, currentCol2);
        }
        
        drawBoards();
    }
    
    private void updatePlayerState(int playerNum) {
        ColumnsBoard board = (playerNum == 1) ? board1 : board2;
        int[] columnHeights = (playerNum == 1) ? columnHeights1 : columnHeights2;
        List<ITile> currentPiece = (playerNum == 1) ? currentPiece1 : currentPiece2;
        int currentCol = (playerNum == 1) ? currentCol1 : currentCol2;
        int topRow = (playerNum == 1) ? topRow1 : topRow2;
        int pieceLength = currentPiece.size();
        
        // Check for collision for the next position below
        if (isCollision(board, topRow + 1, currentCol, pieceLength)) {
            placePieceOnBoard(board, currentPiece, topRow, currentCol);
            
            // Update column height
            int newHeight = 0;
            for (int row = 0; row < board.getHeight(); row++) {
                if (!board.isEmpty(row, currentCol)) {
                    newHeight++;
                }
            }
            columnHeights[currentCol] = newHeight;
            
            // Check if the game is over for this player
            if (columnHeights[currentCol] >= INVISIBLE_HEIGHT) {
                if (playerNum == 1) {
                    player1Lost = true;
                    System.out.println("Player 1 has lost!");
                    
                    // If single player or both players lost, end the game
                    if (!twoPlayerMode || player2Lost) {
                        gameRunning = false;
                    }
                } else {
                    player2Lost = true;
                    System.out.println("Player 2 has lost!");
                    
                    // If both players lost, end the game
                    if (player1Lost) {
                        gameRunning = false;
                    }
                }
            } else {
                checkAndClearMatches(board, columnHeights, playerNum);
            }
            
            // Need a new piece for next turn
            if (playerNum == 1) {
                needNewPiece1 = true;
            } else {
                needNewPiece2 = true;
            }
        }
    }
    
    private void clearPieceFromBoard(ColumnsBoard board, List<ITile> piece, int topRow, int currentCol) {
        for (int i = 0; i < piece.size(); i++) {
            int row = topRow + i;
            if (row >= 0 && row < board.getHeight()) {
                board.placeTile(null, row, currentCol);
            }
        }
    }
    
    private void placePieceOnBoard(ColumnsBoard board, List<ITile> piece, int topRow, int currentCol) {
        for (int i = 0; i < piece.size(); i++) {
            int row = topRow + i;
            if (row >= 0 && row < board.getHeight()) {
                board.placeTile(piece.get(i), row, currentCol);
            }
        }
    }
    
    private boolean canMoveLeft(ColumnsBoard board, List<ITile> piece, int topRow, int currentCol, int pieceLength) {
        if (currentCol <= 0) return false;
        return !isCollision(board, topRow, currentCol - 1, pieceLength);
    }
    
    private boolean canMoveRight(ColumnsBoard board, List<ITile> piece, int topRow, int currentCol, int pieceLength) {
        if (currentCol >= board.getWidth() - 1) return false;
        return !isCollision(board, topRow, currentCol + 1, pieceLength);
    }
    
    private void rotatePiece(List<ITile> columnPiece) {
        // Perform the rotation: bottom -> top, middle -> bottom, top -> middle
        ITile temp = columnPiece.get(columnPiece.size() - 1); // Save the bottom tile
        for (int i = columnPiece.size() - 1; i > 0; i--) {
            columnPiece.set(i, columnPiece.get(i - 1)); // Move each tile down
        }
        columnPiece.set(0, temp); // Move saved bottom to top
    }
    
    private void determineWinner() {
        if (!twoPlayerMode) {
            System.out.println("Game Over! Final Score: " + score1);
        } else {
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
    }

    public String getCurrentPrompt() {
        if (!gameInitialized || !gameRunning) {
            return "";
        }
        
        if (twoPlayerMode) {
            if (activePlayer == 1 && !player1Lost) {
                return "Player 1 Move (a=left, d=right, s=rotate, enter=down): ";
            } else if (activePlayer == 2 && !player2Lost) {
                return "Player 2 Move (j=left, l=right, k=rotate, enter=down): ";
            } else if (player1Lost && !player2Lost) {
                return "Player 2 Move (j=left, l=right, k=rotate, enter=down): ";
            } else if (!player1Lost && player2Lost) {
                return "Player 1 Move (a=left, d=right, s=rotate, enter=down): ";
            } else {
                return "Game Over!";
            }
        } else {
            return "Move (a=left, d=right, s=rotate, enter=down): ";
        }
    }

    public boolean isTwoPlayerMode() {
        return twoPlayerMode;
    }
    
    public int getActivePlayer() {
        return activePlayer;
    }
    
    public boolean isGameRunning() {
        return gameRunning;
    }

    public boolean isGameInitialized() {
        return gameInitialized;
    }
    
    public boolean hasPlayerLost(int playerNum) {
        return playerNum == 1 ? player1Lost : player2Lost;
    }
    
    public int getScore(int playerNum) {
        return playerNum == 1 ? score1 : score2;
    }
    
    public boolean needsNewPiece(int playerNum) {
        return playerNum == 1 ? needNewPiece1 : needNewPiece2;
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
        }
    }
    
    private void clearScreen() {
        System.out.print("\033[2J\033[1;1H");
        System.out.flush();
    }
}