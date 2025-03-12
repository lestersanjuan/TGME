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
        Random random = new Random();
        Integer randomHeight = random.nextInt(1,2) * 2; //Multiply by 2
        Integer randomWidth = random.nextInt(1,2) * 2;
        Integer randomStart = random.nextInt(1,2) * 2;
        
        Tile newTile = new Tile();
        newTile.setValue()
        //Place First Tile
        this.PlaceTile(newTile, randomHeight, randomWidth);

        
    }

    
}
