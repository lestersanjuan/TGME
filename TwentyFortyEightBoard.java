import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** 
 * 
 * Essentially 2048 game board will be a 4x4 Tile Game board, Where if moved left or moved right or up
 * or down will 
*/
    
public class TwentyFortyEightBoard extends Board implements IInputHandler{
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

    public void addRandomTile(){
        // TODO: can be called for whenever an action is done, should be checking if a tile
        // currently has a value or number on it. IDK the algo for it but if it does have 
        // a value just  keep calling random Indexes
    }

    public void isGameOver(){
        // TODO: called after every move, will check if there are any more moves that can be done
        // If not call isGameOver in every iteration. If it returns true then it is Game Over
    }
    
    //Algorithms for how the game should be run
    @Override
    public String GetInput() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetInput'");
    }
    @Override
    public void Left() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Left'");
    }
    @Override
    public void Right() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Right'");
    }
    @Override
    public void Up() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Up'");
    }
    @Override
    public void Down() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Down'");
    }



    
}
