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
    public List<List<List<Tile>>> getBoard() {
        for (int i = 0; i < HEIGHT; i++) { // col
            for (int j = 0; j < WIDTH; j++) { // row
                System.out.print(this.currentBoard.get(i).get(j).get(0).GetValue() + " ");
            }
            System.out.println(" ");
        }
        return this.currentBoard;
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

    public void isGameOver() {
        // TODO: called after every move, will check if there are any more moves that
        // can be done
        // If not call isGameOver in every iteration. If it returns true then it is Game
        // Over
    }

}
