import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TwentyFortyEightGameEngine extends GameEngine {
    //protected Board gameBoard; Used the TFEBoard
    public TwentyFortyEightBoard TFEGameBoard = new TwentyFortyEightBoard();
    private final Integer WIDTH = 4;
    private final Integer HEIGHT = 4;
	protected Integer gameId;
	protected Integer score;
    protected User currentPlayer;

    TwentyFortyEightGameEngine(Integer gameId, User player){
        this.score = 0;
        this.gameId = gameId;
        this.currentPlayer = player;
    }

    // Default constructor for backward compatibility
    TwentyFortyEightGameEngine(){
        this.score = 0;
        this.gameId = 0;
    }

    //List<List<List<Tile>>>                                     v WILL ALWAYS BE 0 FOR THIS CASE
    //TFEFGameBoard to access TFEGameBoard.get(col).get(row).get(0)
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
                Tile currentTile = TFEGameBoard.currentBoard.get(row).get(col).get(0);
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
                Tile newTile = new Tile();
                newTile.SetValue(String.valueOf(newVal));
                TFEGameBoard.PlaceTile(newTile, row, col);
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
                Tile currentTile = TFEGameBoard.currentBoard.get(row).get(col).get(0);
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
                Tile newTile = new Tile();
                newTile.SetValue(String.valueOf(curr));
                TFEGameBoard.PlaceTile(newTile, row, reversedCol);
                reversedCol--;
            }
            while (reversedCol >= 0){
                Tile dummyTile = new Tile();
                dummyTile.SetValue("0");
                TFEGameBoard.PlaceTile(dummyTile, row, reversedCol);
                reversedCol--;
            }
        }
    }

    public void up() {
        // For each column
        for (int col = 0; col < WIDTH; col++) {
            List<Integer> colValues = new ArrayList<>();
            for (int row = 0; row < HEIGHT; row++) {
                Tile currentTile = TFEGameBoard.currentBoard.get(row).get(col).get(0);
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
                Tile newTile = new Tile();
                newTile.SetValue(String.valueOf(val));
                TFEGameBoard.PlaceTile(newTile, writeRow, col);
                writeRow++;
            }
            // Fill the rest with 0
            while (writeRow < HEIGHT) {
                Tile zeroTile = new Tile();
                zeroTile.SetValue("0");
                TFEGameBoard.PlaceTile(zeroTile, writeRow, col);
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
                Tile currentTile = TFEGameBoard.currentBoard.get(row).get(col).get(0);
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
                Tile newTile = new Tile();
                newTile.SetValue(String.valueOf(val));
                TFEGameBoard.PlaceTile(newTile, writeRow, col);
                writeRow--;
            }
            while (writeRow >= 0) {
                Tile zeroTile = new Tile();
                zeroTile.SetValue("0");
                TFEGameBoard.PlaceTile(zeroTile, writeRow, col);
                writeRow--;
            }
        }
    }
    


    //Match tiles, when given two tiles essentially combine the two tiles and return the tiles when its equal
    //man
    public Tile MatchTiles(Tile tile1, Tile tile2) {
        Tile newTile = new Tile();
        Integer tile1Value = Integer.parseInt(tile1.GetValue());
        Integer tile2Value = Integer.parseInt(tile2.GetValue());
        newTile.SetValue(String.valueOf(tile1Value + tile2Value));
        return newTile;
    }
    @Override
    boolean Action(String command) {
        switch (command.toLowerCase()) {
            case "left":
                left();
                return true;
            case "right":
                right();
                return true;
            case "up":
                up();
                return true;
            case "down":
                down();
                return true;
            default:
                return false;
        }
    }
    @Override
    void MatchTiles() {
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
