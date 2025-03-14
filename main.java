public class main {
    public static void main(String[] args) {
        System.out.println("meow");

        TwentyFortyEightGameEngine man = new TwentyFortyEightGameEngine();
        man.TFEGameBoard.addRandomTile();
        man.TFEGameBoard.addRandomTile();
        man.TFEGameBoard.addRandomTile();
        man.TFEGameBoard.addRandomTile();
        man.TFEGameBoard.addRandomTile();
        man.TFEGameBoard.addRandomTile();
        man.TFEGameBoard.addRandomTile();
        man.TFEGameBoard.addRandomTile();

        System.out.println(man.TFEGameBoard.getBoard());
        man.left();
        System.out.println(man.TFEGameBoard.getBoard());
    }
}