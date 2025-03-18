import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TwentyFortyEightGameEngine extends GameEngine {
    // The game board for 2048
    public TwentyFortyEightBoard TFEGameBoard = new TwentyFortyEightBoard();
    private final Integer WIDTH = 4;
    private final Integer HEIGHT = 4;
    protected Integer gameId;
    protected Integer score;
    protected User currentPlayer;

    // Constructor to initialize a new game with a specific player
    TwentyFortyEightGameEngine(Integer gameId, User player) {
        this.score = 0;
        this.gameId = gameId;
        this.currentPlayer = player;
    }

    // Default constructor for compatibility
    TwentyFortyEightGameEngine() {
        this.score = 0;
        this.gameId = 0;
    }

    // Moves all tiles left and merges them when possible
    public void left() {
        for (int row = 0; row < HEIGHT; row++) {
            List<Integer> rowValues = new ArrayList<>();
            // Collect non-zero tile values
            for (int col = 0; col < WIDTH; col++) {
                Tile currentTile = TFEGameBoard.currentBoard.get(row).get(col).get(0);
                int val = Integer.parseInt(currentTile.GetValue());
                if (val != 0) {
                    rowValues.add(val);
                }
            }

            // Merge adjacent tiles if they are the same
            for (int i = 0; i < rowValues.size() - 1; i++) {
                if (rowValues.get(i).equals(rowValues.get(i + 1))) {
                    int mergedValue = rowValues.get(i) * 2;
                    rowValues.set(i, mergedValue);
                    rowValues.remove(i + 1);
                    this.score += mergedValue; // Update score
                }
            }

            // Update the board with the new values
            for (int col = 0; col < WIDTH; col++) {
                int newVal = (col < rowValues.size()) ? rowValues.get(col) : 0;
                Tile newTile = new Tile();
                newTile.SetValue(String.valueOf(newVal));
                TFEGameBoard.PlaceTile(newTile, row, col);
            }
        }
    }

    // Moves all tiles right and merges them when possible
    public void right() {
        for (int row = 0; row < HEIGHT; row++) {
            List<Integer> rowValues = new ArrayList<>();
            // Collect non-zero tile values
            for (int col = 0; col < WIDTH; col++) {
                Tile currentTile = TFEGameBoard.currentBoard.get(row).get(col).get(0);
                int val = Integer.parseInt(currentTile.GetValue());
                if (val != 0) {
                    rowValues.add(val);
                }
            }
            Collections.reverse(rowValues); // Reverse for rightward movement

            // Merge adjacent tiles if they are the same
            for (int i = 0; i < rowValues.size() - 1; i++) {
                if (rowValues.get(i).equals(rowValues.get(i + 1))) {
                    int mergedValue = rowValues.get(i) * 2;
                    rowValues.set(i, mergedValue);
                    rowValues.remove(i + 1);
                    this.score += mergedValue; // Update score
                }
            }

            // Update the board with the new values
            int reversedCol = WIDTH - 1;
            for (Integer curr : rowValues) {
                Tile newTile = new Tile();
                newTile.SetValue(String.valueOf(curr));
                TFEGameBoard.PlaceTile(newTile, row, reversedCol);
                reversedCol--;
            }
            while (reversedCol >= 0) {
                Tile dummyTile = new Tile();
                dummyTile.SetValue("0");
                TFEGameBoard.PlaceTile(dummyTile, row, reversedCol);
                reversedCol--;
            }
        }
    }

    // Moves all tiles up and merges them when possible
    public void up() {
        for (int col = 0; col < WIDTH; col++) {
            List<Integer> colValues = new ArrayList<>();
            // Collect non-zero tile values
            for (int row = 0; row < HEIGHT; row++) {
                Tile currentTile = TFEGameBoard.currentBoard.get(row).get(col).get(0);
                int val = Integer.parseInt(currentTile.GetValue());
                if (val != 0) {
                    colValues.add(val);
                }
            }

            // Merge adjacent tiles if they are the same
            for (int i = 0; i < colValues.size() - 1; i++) {
                if (colValues.get(i).equals(colValues.get(i + 1))) {
                    int mergedValue = colValues.get(i) * 2;
                    colValues.set(i, mergedValue);
                    colValues.remove(i + 1);
                    this.score += mergedValue; // Update score
                }
            }

            // Update the board with the new values
            int writeRow = 0;
            for (int val : colValues) {
                Tile newTile = new Tile();
                newTile.SetValue(String.valueOf(val));
                TFEGameBoard.PlaceTile(newTile, writeRow, col);
                writeRow++;
            }
            while (writeRow < HEIGHT) {
                Tile zeroTile = new Tile();
                zeroTile.SetValue("0");
                TFEGameBoard.PlaceTile(zeroTile, writeRow, col);
                writeRow++;
            }
        }
    }

    // Moves all tiles down and merges them when possible
    public void down() {
        for (int col = 0; col < WIDTH; col++) {
            List<Integer> colValues = new ArrayList<>();
            // Collect non-zero tile values
            for (int row = 0; row < HEIGHT; row++) {
                Tile currentTile = TFEGameBoard.currentBoard.get(row).get(col).get(0);
                int val = Integer.parseInt(currentTile.GetValue());
                if (val != 0) {
                    colValues.add(val);
                }
            }
            Collections.reverse(colValues); // Reverse for downward movement

            // Merge adjacent tiles if they are the same
            for (int i = 0; i < colValues.size() - 1; i++) {
                if (colValues.get(i).equals(colValues.get(i + 1))) {
                    int mergedValue = colValues.get(i) * 2;
                    colValues.set(i, mergedValue);
                    colValues.remove(i + 1);
                    this.score += mergedValue; // Update score
                }
            }

            // Update the board with the new values
            int writeRow = HEIGHT - 1;
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

    // Matches two tiles by summing their values
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
        System.out.println("Matching tiles logic to be implemented.");
    }

    public int getScore() {
        return this.score;
    }

    public void endGame() {
        if (this.currentPlayer != null) {
            this.currentPlayer.SetScore("2048", String.valueOf(this.score));
        }
    }
}
