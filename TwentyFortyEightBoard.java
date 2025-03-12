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
        Integer randomHeight = random.nextInt(1,4);
        Integer randomWidth = random.nextInt(1,4);
        Integer randomStart = random.nextInt(1,2) * 2; //multiply by 2 to keep it a multiple of 2

        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++){ //populate currentBoard with all new Tiles
                Tile newTile = new Tile();
                this.PlaceTile(newTile, i, j);
            }
        }
        Tile newTile = new Tile();
        newTile.SetValue(randomStart.toString());
        //Place First Tile
        this.PlaceTile(newTile, randomHeight, randomWidth);
        //Check If there is tile
    }

    
}
