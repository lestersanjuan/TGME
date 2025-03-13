

public class TwentyFortyEightGameEngine extends GameEngine {
    //protected Board gameBoard; Used the TFEBoard
    protected TwentyFortyEightBoard TFEGameBoard = new TwentyFortyEightBoard();
    
	protected Integer gameId;
	protected Integer score;
    TwentyFortyEightGameEngine(){
        //IDK? idt it needs to initialzie anything yet
    }
    //List<List<List<Tile>>>                                     v WILL ALWAYS BE 0 FOR THIS CASE
    //TFEFGameBoard to access TFEGameBoard.get(col).get(row).get(0 )
    private void left(){
        
    }

    private void right(){

    }

    private void up(){

    }

    private void down(){

    }

    @Override
    boolean Action(String command) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Action'");
    }

    @Override
    //Match tiles, when given two tiles essentially combine the two tiles and return the tiles when its equal
    //man
    void MatchTiles() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'MatchTiles'");
    }
    
}
