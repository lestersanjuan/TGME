import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TwentyFortyEightGameEngine extends GameEngine {
    private final Integer WIDTH = 4; // Game board does not change sizes
    private final Integer HEIGHT = 4;

    TwentyFortyEightGameEngine() {
        System.out.println("test");
        Random random = new Random();
        Integer randomHeight = random.nextInt(4);
        Integer randomWidth = random.nextInt(4);
        Integer randomStart = random.nextInt(1, 2) * 2; // multiply by 2 to keep it a multiple of 2
        this.currentBoard = new ArrayList<>(HEIGHT);
        for (int i = 0; i < HEIGHT; i++) {
            List<Tile> current = new ArrayList<Tile>(WIDTH);
            for (int j = 0; j < WIDTH; j++) {
                Tile newTile = new Tile();
                newTile.SetValue("0");
                current.add(newTile);
            }
            currentBoard.add(current);
        }
        System.out.println("????/");
        Tile newTile = new Tile();
        newTile.SetValue(randomStart.toString());
        // Place First Tile
        this.PlaceTile(newTile, randomHeight, randomWidth);
        // Check If there is tile
    }

    @Override
    public boolean Action(String command) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Action'");
    }

    @Override
    public void MatchTiles() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'MatchTiles'");
    }

}
