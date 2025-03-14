

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
    public void left(){
        //check every row move left
        boolean foundOne = false;
        for (int row = 0; row < HEIGHT; row++){
            for (int col = 0; col < WIDTH; col++){
                Tile currTile = TFEGameBoard.currentBoard.get(row).get(col).get(0);
                if (!currTile.GetValue().equals("0")){ //Basically move the tile left till it collides
                    for (int index = 0; index < col; index++){// at col
                        Tile tempTile = TFEGameBoard.currentBoard.get(row).get(index).get(0);
                        Tile emptyTile = new Tile();
                        emptyTile.SetValue("0");
                        if (tempTile.GetValue().equals("0")){
                            TFEGameBoard.PlaceTile(currTile, row, index);
                            if (index != 0){
                                Tile dummyTile = TFEGameBoard.currentBoard.get(row).get(index - 1).get(0);
                                if ((dummyTile.GetValue().equals(currTile.GetValue()))){
                                    System.out.println("????");
                                    dummyTile.SetValue(String.valueOf(Integer.parseInt(currTile.GetValue()) * 2));
                                    TFEGameBoard.PlaceTile(dummyTile, row, index - 1);
                                    TFEGameBoard.PlaceTile(emptyTile, row, index);
                                    TFEGameBoard.PlaceTile(emptyTile, row, col);
                                    break;
                                }
                            }
                            TFEGameBoard.PlaceTile(emptyTile, row, col);
                            break;
                        }

                    }
                }
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
