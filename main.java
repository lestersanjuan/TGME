import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        // Create a game manager to handle user data and game logic
        GameManager gameManager = new GameManager() {
            // Override the AddUser method to add new players to the game
            @Override
            void AddUser(String name, Integer id, List<String> data) {
                // Create a new User object with the given name
                User user = new User(name) {
                    // Override the toString method to display the player's name and score
                    @Override
                    public String toString() {
                        return "player " + name + " score: " + this.ListScore("2048");
                    }
                };
                user.userId = id;  // Set the user ID
                this.users.put(id, user);  // Add the user to the users map in GameManager
            }
        };

        // Add some players to the game
        gameManager.AddUser("player 1", 1, new ArrayList<>());
        gameManager.AddUser("player 2", 2, new ArrayList<>());

        Scanner reader = new Scanner(System.in);  // Create a scanner to read user input
        boolean running = true;  // Flag to control the main game loop

        while (running) {
            // Display the menu options
            System.out.println("\n1. Start new");
            System.out.println("2. scores");
            System.out.println("3. quit");
            
            // Read user choice
            String choice = reader.next();
            switch (choice) {
                case "1":
                    // Start a new game
                    System.out.print("Enter player ID (1 or 2): ");
                    int playerId = reader.nextInt();  // Get the player ID
                    User currentPlayer = gameManager.GetUser(playerId);  // Get the player object
                    
                    if (currentPlayer != null) {
                        // Create a new game and assign it a game ID
                        TwentyFortyEightGameEngine game = new TwentyFortyEightGameEngine();
                        game.gameId = gameManager.games.size() + 1;  // Set the game ID based on the number of games
                        gameManager.games.put(game.gameId, game);  // Add the game to the games map
                        gameManager.leaderboard.put(game.gameId, new HashMap<>());  // Initialize the leaderboard for the game
                        
                        System.out.println("\nGame started for " + currentPlayer.GetName());
                        // Call the playGame method to play the game
                        playGame(game, reader);
                        
                        // Update the score after the game ends
                        gameManager.SetNewScore(game.gameId, playerId, game.getScore());
                        currentPlayer.SetScore("2048", String.valueOf(game.getScore()));  // Set the player's score for "2048"
                        System.out.println("Final score: " + game.getScore());
                    } else {
                        // If the player ID is invalid, print an error message
                        System.out.println("Invalid player ID");
                    }
                    break;
                    
                case "2":
                    // Display the scores of all players
                    System.out.println("\nPlayer Scores:");
                    for (User user : gameManager.users.values()) {
                        System.out.println(user.toString());  // Print each user's score
                    }
                    break;
                    
                case "3":
                    // Quit the game
                    running = false;
                    break;
                    
                default:
                    // If the input is invalid, display an error message
                    System.out.println("Invalid option");
            }
        }

        reader.close();  // Close the scanner
    }

    // Method to simulate playing the game
    private static void playGame(TwentyFortyEightGameEngine game, Scanner reader) {
        game.TFEGameBoard.addRandomTile();  // Add a random tile to the game board at the start
        
        while (true) {
            String oldBoard = game.TFEGameBoard.getBoard();  // Get the current game board state
            // Check if the game is over
            if (game.TFEGameBoard.isGameOver()){
                System.out.println("u suck");  // Game over message
                break;
            }
            System.out.println("Current score: " + game.getScore());  // Display the current score
            System.out.print("Enter move (up/down/left/right/quit): ");
            
            String command = reader.next().toLowerCase();  // Read the player's move
            if (command.equals("quit")) {
                break;  // Exit the game if the player chooses to quit
            }
            
            // If the move is invalid, prompt the player to try again
            if (!game.Action(command)) {
                System.out.println("Invalid move!");
                continue;
            }
            // If the board has changed, add a new random tile to the board
            if (!oldBoard.equals(game.TFEGameBoard.getBoard())){
                game.TFEGameBoard.addRandomTile();
            }
            else {
                // If the move did not change the board, display an error message
                System.out.println("Can Not Do Move");
            }
        }
    }
}

