import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** 
 * 
 * Essentially 2048 game board will be a 4x4 Tile Game board, Where if moved left or moved right or up
 * or down will 
*/
    
public class TwentyFortyEightBoard extends Board{
    private final Integer WIDTH = 4; // Game board does not change sizes
    private final Integer HEIGHT = 4;


    /**
     * To Begin the Game
     */

    public TwentyFortyEightBoard(){
        System.out.println("test");
        Random random = new Random();
        Integer randomHeight = random.nextInt(1,4);
        Integer randomWidth = random.nextInt(1,4);
        Integer randomStart = random.nextInt(1,2) * 2; //multiply by 2 to keep it a multiple of 2
        this.currentBoard = new ArrayList<>(HEIGHT);
        for (int i = 0; i < HEIGHT; i++){
            List<Tile> current = new ArrayList<Tile>(WIDTH);
            for (int j = 0; j < WIDTH; j++){
                Tile newTile = new Tile();
                newTile.SetValue("0");
                current.add(newTile);
            }
            currentBoard.add(current);
        }
        System.out.println("????/");
        Tile newTile = new Tile();
        newTile.SetValue(randomStart.toString());
        //Place First Tile
        this.PlaceTile(newTile, randomHeight, randomWidth);
        //Check If there is tile
    }
    //Prints out current board
    public List<List<Tile>> getBoard(){
        for (int i = 0; i < HEIGHT; i++){
            for (int j = 0; j < WIDTH; j++){
                System.out.print(this.currentBoard.get(i).get(j).GetValue() + " ");
            }
            System.out.println(" ");
        }
        return this.currentBoard;
    }

    
}
