import java.util.HashMap;
import java.util.Map;

// Abstract class representing a user in the game system
public abstract class User {
    protected String name;  // Name of the user
    protected Map<String, String> data; // Stores game scores with game ID as key
    protected Integer userId; // Unique identifier for the user

    // Constructor to initialize a user with a given name
    public User(String name) {
        this.name = name;
        this.data = new HashMap<>(); // Initializes an empty score data map
    }

    // Retrieves the score for a specific game using its game ID
    public String ListScore(String gameId) {
        return this.data.get(gameId);
    }

    // Updates or sets the score for a specific game
    public void SetScore(String gameId, String score) {
        this.data.put(gameId, score);
    }

    // Returns the user's name
    public String GetName() {
        return this.name;
    }
}
