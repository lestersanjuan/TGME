import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * Essentially 2048 game board will be a 4x4 Tile Game board, Where if moved
 * left or moved right or up
 * or down will
 */

public class TwentyFortyEightBoard extends Board {
    private final Integer WIDTH = 4; // Game board does not change sizes
    private final Integer HEIGHT = 4;

    /**
     * To Begin the Game
     */

    public TwentyFortyEightBoard() {
        super(4, 4); // Call the parent constructor to set up width and height

        // Initialize currentBoard as a 2D list of Tile objects
        this.currentBoard = new ArrayList<>();
        for (int i = 0; i < HEIGHT; i++) {
            List<List<Tile>> row = new ArrayList<>();
            for (int j = 0; j < WIDTH; j++) {
                List<Tile> tileCell = new ArrayList<>();
                Tile newTile = new Tile();
                newTile.SetValue("0");
                tileCell.add(newTile);
                row.add(tileCell);
            }
            this.currentBoard.add(row);
        }
        addRandomTile();
    }

    // Prints out current board
    public String getBoard() {
        String printingString = "";
        for (int i = 0; i < HEIGHT; i++) { // col
            for (int j = 0; j < WIDTH; j++) { // row
                printingString = printingString + this.currentBoard.get(i).get(j).get(0).GetValue() + " ";
            }
            printingString += "\n";
        }
        System.out.println(printingString);
        return printingString;
    }

    public void addRandomTile() {
        Integer TWELVE_TRIES = 0;
        while (TWELVE_TRIES <= 12) {
            TWELVE_TRIES++;
            Random random = new Random();
            int randomHeight = random.nextInt(HEIGHT);
            int randomWidth = random.nextInt(WIDTH);
            int randomStart = (random.nextInt(2) + 1) * 2; // 2 or 4

            Tile currentTile = this.currentBoard.get(randomHeight).get(randomWidth).get(0);

            if (currentTile.GetValue().equals("0")) { // ✅ Only place on empty spaces
                Tile newTile = new Tile();
                newTile.SetValue(Integer.toString(randomStart));
                this.PlaceTile(newTile, randomHeight, randomWidth);
                break;
            }
        }
    }

    public boolean isGameOver() {
        int size = 4;
    
        // 1) Check for any empty (0) tile
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Tile tile = this.currentBoard.get(row).get(col).get(0);
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
                Tile currentTile = this.currentBoard.get(row).get(col).get(0);
                int currentValue = Integer.parseInt(currentTile.GetValue());
    
                // Check right neighbor if exists
                if (col + 1 < size) {
                    Tile rightTile = this.currentBoard.get(row).get(col + 1).get(0);
                    int rightValue = Integer.parseInt(rightTile.GetValue());
                    if (currentValue == rightValue) {
                        return false;  // Merge possible with right neighbor
                    }
                }
    
                // Check down neighbor if exists
                if (row + 1 < size) {
                    Tile downTile = this.currentBoard.get(row + 1).get(col).get(0);
                    int downValue = Integer.parseInt(downTile.GetValue());
                    if (currentValue == downValue) {
                        return false;  // Merge possible with down neighbor
                    }
                }
            }
        }
    
        // If we reach here, no empty spaces and no merges => game over
        return true;
    }
    

}
