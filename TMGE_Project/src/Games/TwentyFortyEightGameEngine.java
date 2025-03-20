package Games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import GameModules.Board;
import GameModules.GameEngine;
import GameModules.ITile;
import GameModules.Tile;
import GameModules.User;

public class TwentyFortyEightGameEngine extends GameEngine {
    private final Integer WIDTH = 4;
    private final Integer HEIGHT = 4;
    protected User currentPlayer;

    public TwentyFortyEightGameEngine(Integer gameId){
    	super("2048", gameId);
        this.gameBoard = new Board(WIDTH, HEIGHT);
        for (int row = 0; row < WIDTH; row ++) {
        	for (int col = 0; col < HEIGHT; col ++) {
        		ITile newTile = new Tile("0");
        		this.gameBoard.PlaceTile(newTile, row, col);
        	}
        }
        addRandomTile();
    }
    
    public void ResetEngine() {
		this.gameRunning = true;
        this.score = 0;
        this.gameBoard = new Board(WIDTH, HEIGHT);
        for (int row = 0; row < WIDTH; row ++) {
        	for (int col = 0; col < HEIGHT; col ++) {
        		ITile newTile = new Tile("0");
        		this.gameBoard.PlaceTile(newTile, row, col);
        	}
        }
        addRandomTile();
    }
    
    public void addRandomTile() {
        Integer TWELVE_TRIES = 0;
        while (TWELVE_TRIES <= 12) {
            TWELVE_TRIES++;
            Random random = new Random();
            int randomHeight = random.nextInt(HEIGHT);
            int randomWidth = random.nextInt(WIDTH);
            int randomStart = (random.nextInt(2) + 1) * 2; // 2 or 4

            ITile currentTile = this.gameBoard.GetBoardState().get(randomHeight).GetSpot(randomWidth).GetFirstTile();

            if (currentTile.GetValue().equals("0")) { // ✅ Only place on empty spaces
                Tile newTile = new Tile(Integer.toString(randomStart));
                this.gameBoard.GetSpot(randomHeight, randomWidth).PopTile();
                this.gameBoard.PlaceTile(newTile, randomHeight, randomWidth);
                break;
            }
        }
    }
    
    public Boolean IsGameRunning() {
    	return this.gameRunning;
    }
    
    public boolean isGameOver() {
        int size = 4;
    
        // 1) Check for any empty (0) tile
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                ITile tile = this.gameBoard.GetBoardState().get(row).GetSpot(col).GetFirstTile();
                int value = Integer.parseInt(tile.GetValue());
                if (value == 0) {
                    return false;  // Found an empty space, so not game over
                }
            }
        }
    
        // 2) Check for possible merges
        //    (If any two adjacent tiles match, we can still make a move)
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                ITile currentTile = this.gameBoard.GetBoardState().get(row).GetSpot(col).GetFirstTile();
                int currentValue = Integer.parseInt(currentTile.GetValue());
    
                // Check right neighbor if exists
                if (col + 1 < size) {
                    ITile rightTile = this.gameBoard.GetBoardState().get(row).GetSpot(col + 1).GetFirstTile();
                    int rightValue = Integer.parseInt(rightTile.GetValue());
                    if (currentValue == rightValue) {
                        return false;  // Merge possible with right neighbor
                    }
                }
    
                // Check down neighbor if exists
                if (row + 1 < size) {
                    ITile downTile = this.gameBoard.GetBoardState().get(row + 1).GetSpot(col).GetFirstTile();
                    int downValue = Integer.parseInt(downTile.GetValue());
                    if (currentValue == downValue) {
                        return false;  // Merge possible with down neighbor
                    }
                }
            }
        }
    
        // If we reach here, no empty spaces and no merges => game over
        this.endGame();
        this.gameRunning = false;
        return true;
    }

    //List<List<List<ITile>>>                                     v WILL ALWAYS BE 0 FOR THIS CASE
    //TFEFGameBoard to access this.gameBoard.get(col).get(row).get(0)
    // [ [[0],[0],[0],[0]],
    //   [[0],[0],[0],[0]],
    //   [[0],[0],[0],[0]],
    //   [[0],[0],[0],[0]] ]
    public void left() {
        // For each row in the board
        for (int row = 0; row < HEIGHT; row++) {
            // 1) Collect non-zero tile values in a temporary list.
            List<Integer> rowValues = new ArrayList<>();
            for (int col = 0; col < WIDTH; col++) {
                ITile currentTile = this.gameBoard.GetBoardState().get(row).GetSpot(col).GetFirstTile();
                int val = Integer.parseInt(currentTile.GetValue());
                if (val != 0) {
                    rowValues.add(val);
                }
            }

            // 2) Merge adjacent duplicates from left to right.
            for (int i = 0; i < rowValues.size() - 1; i++) {
                if (rowValues.get(i).equals(rowValues.get(i + 1))) {
                    int mergedValue = rowValues.get(i) * 2;
                    rowValues.set(i, mergedValue);  // double the left tile
                    rowValues.remove(i + 1);
                    this.score += mergedValue; // Add to score when tiles merge
                }
            }
            for (int col = 0; col < WIDTH; col++) {
                int newVal = (col < rowValues.size()) ? rowValues.get(col) : 0;
                ITile newTile = new Tile(String.valueOf(newVal));
                this.gameBoard.GetSpot(row, col).PopTile();
                this.gameBoard.PlaceTile(newTile, row, col);
            }
        }
    }
    // [ [[0],[0],[0],[0]],
    //   [[0],[0],[0],[0]],
    //   [[0],[0],[0],[0]],
    //   [[0],[0],[0],[0]] ]
    public void right(){
        for (int row = 0; row < HEIGHT; row++) {
            // 1) Collect non-zero tile values in a temporary list.
            List<Integer> rowValues = new ArrayList<>();
            for (int col = 0; col < WIDTH; col++) {
                ITile currentTile = this.gameBoard.GetBoardState().get(row).GetSpot(col).GetFirstTile();
                int val = Integer.parseInt(currentTile.GetValue());
                if (val != 0) {
                    rowValues.add(val);
                }
            }
            Collections.reverse(rowValues);

            // 2) Merge adjacent duplicates from left to right.
            for (int i = 0; i < rowValues.size() - 1; i++) {
                if (rowValues.get(i).equals(rowValues.get(i + 1))) {
                    int mergedValue = rowValues.get(i) * 2;
                    rowValues.set(i, mergedValue);  // double the left tile
                    rowValues.remove(i + 1);
                    this.score += mergedValue; // Add to score when tiles merge
                }
            }
            Integer reversedCol = WIDTH - 1;
            for (Integer curr: rowValues){
                ITile newTile = new Tile(String.valueOf(curr));
                this.gameBoard.GetSpot(row, reversedCol).PopTile();
                this.gameBoard.PlaceTile(newTile, row, reversedCol);
                reversedCol--;
            }
            while (reversedCol >= 0){
                ITile dummyTile = new Tile("0");
                this.gameBoard.GetSpot(row, reversedCol).PopTile();
                this.gameBoard.PlaceTile(dummyTile, row, reversedCol);
                reversedCol--;
            }
        }
    }

    public void up() {
        // For each column
        for (int col = 0; col < WIDTH; col++) {
            List<Integer> colValues = new ArrayList<>();
            for (int row = 0; row < HEIGHT; row++) {
                ITile currentTile = this.gameBoard.GetBoardState().get(row).GetSpot(col).GetFirstTile();
                int val = Integer.parseInt(currentTile.GetValue());
                if (val != 0) {
                    colValues.add(val);
                }
            }
            for (int i = 0; i < colValues.size() - 1; i++) {
                if (colValues.get(i).equals(colValues.get(i + 1))) {
                    int mergedValue = colValues.get(i) * 2;
                    colValues.set(i, mergedValue);  // double the top tile
                    colValues.remove(i + 1);        // remove the merged tile
                    this.score += mergedValue; // Add to score when tiles merge
                }
            }
            int writeRow = 0;
            // Write all merged tiles
            for (int val : colValues) {
                ITile newTile = new Tile(String.valueOf(val));
                this.gameBoard.GetSpot(writeRow, col).PopTile();
                this.gameBoard.PlaceTile(newTile, writeRow, col);
                writeRow++;
            }
            // Fill the rest with 0
            while (writeRow < HEIGHT) {
                ITile zeroTile = new Tile("0");
                this.gameBoard.GetSpot(writeRow, col).PopTile();
                this.gameBoard.PlaceTile(zeroTile, writeRow, col);
                writeRow++;
            }
        }
    }
    

    public void down() {
        // For each column
        for (int col = 0; col < WIDTH; col++) {
            // 1) Gather all non-zero values in this column (top to bottom).
            List<Integer> colValues = new ArrayList<>();
            for (int row = 0; row < HEIGHT; row++) {
                ITile currentTile = this.gameBoard.GetBoardState().get(row).GetSpot(col).GetFirstTile();
                int val = Integer.parseInt(currentTile.GetValue());
                if (val != 0) {
                    colValues.add(val);
                }
            }
            Collections.reverse(colValues);
    
            // 3) Merge adjacent duplicates (now left to right in the reversed list).
            for (int i = 0; i < colValues.size() - 1; i++) {
                if (colValues.get(i).equals(colValues.get(i + 1))) {
                    int mergedValue = colValues.get(i) * 2;
                    colValues.set(i, mergedValue);
                    colValues.remove(i + 1);
                    this.score += mergedValue; // Add to score when tiles merge
                }
            }
    
    
            int writeRow = HEIGHT - 1;  // start at bottom row
            for (int val : colValues) {
                ITile newTile = new Tile(String.valueOf(val));
                this.gameBoard.GetSpot(writeRow, col).PopTile();
                this.gameBoard.PlaceTile(newTile, writeRow, col);
                writeRow--;
            }
            while (writeRow >= 0) {
                ITile zeroTile = new Tile("0");
                this.gameBoard.GetSpot(writeRow, col).PopTile();
                this.gameBoard.PlaceTile(zeroTile, writeRow, col);
                writeRow--;
            }
        }
    }
    


    //Match tiles, when given two tiles essentially combine the two tiles and return the tiles when its equal
    //man
    public ITile MatchTiles(ITile tile1, ITile tile2) {
        Integer tile1Value = Integer.parseInt(tile1.GetValue());
        Integer tile2Value = Integer.parseInt(tile2.GetValue());
        ITile newTile = new Tile(String.valueOf(tile1Value + tile2Value));
        return newTile;
    }
    @Override
    public boolean Action(String command) {
        switch (command.toLowerCase()) {
            case "left":
                left();
                break;
            case "right":
                right();
                break;
            case "up":
                up();
                break;
            case "down":
                down();
                break;
            default:
                return false;
        }
        
        this.isGameOver();
        this.addRandomTile();
        return true;
    }
    @Override
    public void MatchTiles() {
        // TODO Auto-generated method stub
        System.out.println("mneow");
    }

    // Get current score
    public int getScore() {
        return this.score;
    }

    // End game and update score
    public void endGame() {
        if (this.currentPlayer != null) {
            this.currentPlayer.SetScore("2048", String.valueOf(this.score));
        }
    }

    // Get current player
    public User getCurrentPlayer() {
        return this.currentPlayer;
    }

    // Set current player
    public void setCurrentPlayer(User player) {
        this.currentPlayer = player;
    }
}
