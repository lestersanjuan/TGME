import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TwentyFortyEightBoard extends Board {
    private final Integer WIDTH = 4;
    private final Integer HEIGHT = 4;

    public TwentyFortyEightBoard() {
        super(4, 4); // Initialize board size

        this.currentBoard = new ArrayList<>();
        for (int i = 0; i < HEIGHT; i++) {
            List<List<Tile>> row = new ArrayList<>();
            for (int j = 0; j < WIDTH; j++) {
                List<Tile> tileCell = new ArrayList<>();
                Tile newTile = new Tile();
                newTile.SetValue("0"); // Empty tile
                tileCell.add(newTile);
                row.add(tileCell);
            }
            this.currentBoard.add(row);
        }
        addRandomTile(); // Start with a random tile
    }

    public String getBoard() {
        String printingString = "";
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                printingString += this.currentBoard.get(i).get(j).get(0).GetValue() + " ";
            }
            printingString += "\n";
        }
        System.out.println(printingString);
        return printingString;
    }

    public void addRandomTile() {
        int tries = 0;
        while (tries < 12) {
            tries++;
            Random random = new Random();
            int row = random.nextInt(HEIGHT);
            int col = random.nextInt(WIDTH);
            int value = (random.nextInt(2) + 1) * 2; // 2 or 4

            if (this.currentBoard.get(row).get(col).get(0).GetValue().equals("0")) {
                Tile newTile = new Tile();
                newTile.SetValue(Integer.toString(value));
                this.PlaceTile(newTile, row, col);
                break;
            }
        }
    }

    public boolean isGameOver() {
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                int value = Integer.parseInt(this.currentBoard.get(row).get(col).get(0).GetValue());
                if (value == 0) return false; // Empty space found

                // Check right
                if (col + 1 < WIDTH) {
                    int rightValue = Integer.parseInt(this.currentBoard.get(row).get(col + 1).get(0).GetValue());
                    if (value == rightValue) return false; // Merge possible
                }

                // Check down
                if (row + 1 < HEIGHT) {
                    int downValue = Integer.parseInt(this.currentBoard.get(row + 1).get(col).get(0).GetValue());
                    if (value == downValue) return false; // Merge possible
                }
            }
        }
        return true; // No moves left
    }
}
