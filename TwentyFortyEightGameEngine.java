import java.util.ArrayList;
import java.util.List;

public class TwentyFortyEightGameEngine extends GameEngine {
    //protected Board gameBoard; Used the TFEBoard
    public TwentyFortyEightBoard TFEGameBoard = new TwentyFortyEightBoard();
    private final Integer WIDTH = 4;
    private final Integer HEIGHT = 4;
	protected Integer gameId;
	protected Integer score;
    TwentyFortyEightGameEngine(){
        //IDK? idt it needs to initialzie anything yet
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
                    rowValues.set(i, rowValues.get(i) * 2);  // double the left tile
                    rowValues.remove(i + 1);                // remove the merged tile
                    // Skip checking the next index after a merge
                    // so you don't merge the newly formed tile again in this move
                }
            }

            // 3) Place the merged values back into the row (left aligned),
            //    filling any leftover spots with 0.
            for (int col = 0; col < WIDTH; col++) {
                int newVal = (col < rowValues.size()) ? rowValues.get(col) : 0;
                Tile newTile = new Tile();
                newTile.SetValue(String.valueOf(newVal));
                TFEGameBoard.PlaceTile(newTile, row, col);
            }
        }
    }

    public void right(){

    }

    public void up(){

    }

    public void down(){

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
        // TODO Auto-generated method stub
        System.out.println("mneow");
        return false;
    }
    @Override
    void MatchTiles() {
        // TODO Auto-generated method stub
        System.out.println("mneow");
    }
}
