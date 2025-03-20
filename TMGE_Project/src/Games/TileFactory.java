package Games;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import GameModules.ITile;
import GameModules.Tile;

public class TileFactory {
    private static final String[] COLORS = {"O", "R", "G", "B", "Y", "P"}; // Orange, Red, Green, Blue, Yellow, Purple for the colors of the tiles
    public static final Tile BlankTile = new Tile("");
    
    private Random random;

    public TileFactory() {
        random = new Random();
    }

    /**
     * Creates a single tile with a random color
     * @return a new ITile with a random color
     */
    public ITile createTile() {
        Tile tile = new Tile("");
        String randomColor = COLORS[random.nextInt(COLORS.length)];
        tile.SetValue(randomColor);
        return tile;
    }

    /** 
     * Creates a single tile with a specified color
     * @param color the color of the tile
     * @return a new ITile with the specified color
    */
    public ITile createTile(String color) {
        Tile tile = new Tile("");
        tile.SetValue(color);
        return tile;
    }

    public List<ITile> createColumn() {
        List<ITile> column = new ArrayList<>(3);

        for (int i = 0; i < 3; i++) {
            column.add(createTile());
        }
        return column;
    }
}
