import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager() {
            @Override
            void AddUser(String name, Integer id, List<String> data) {
                User user = new User(name) {
                    @Override
                    public String toString() {
                        return "player " + name + " score: " + this.ListScore("2048");
                    }
                };
                user.userId = id;
                this.users.put(id, user);
            }
        };

        // Add some players
        gameManager.AddUser("player 1", 1, new ArrayList<>());
        gameManager.AddUser("player 2", 2, new ArrayList<>());

        Scanner reader = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n1. Start new");
            System.out.println("2. scores");
            System.out.println("3. quit");
            
            String choice = reader.next();
            switch (choice) {
                case "1":
                    System.out.print("Enter player ID (1 or 2): ");
                    int playerId = reader.nextInt();
                    User currentPlayer = gameManager.GetUser(playerId);
                    
                    if (currentPlayer != null) {
                        TwentyFortyEightGameEngine game = new TwentyFortyEightGameEngine();
                        game.gameId = gameManager.games.size() + 1;
                        gameManager.games.put(game.gameId, game);
                        gameManager.leaderboard.put(game.gameId, new HashMap<>());
                        
                        System.out.println("\nGame started for " + currentPlayer.GetName());
                        playGame(game, reader);
                        
                        // Update score after game ends
                        gameManager.SetNewScore(game.gameId, playerId, game.getScore());
                        currentPlayer.SetScore("2048", String.valueOf(game.getScore()));
                        System.out.println("Final score: " + game.getScore());
                    } else {
                        System.out.println("Invalid player ID");
                    }
                    break;
                    
                case "2":
                    System.out.println("\nPlayer Scores:");
                    for (User user : gameManager.users.values()) {
                        System.out.println(user.toString());
                    }
                    break;
                    
                case "3":
                    running = false;
                    break;
                    
                default:
                    System.out.println("Invalid option");
            }
        }

        reader.close();
    }

    private static void playGame(TwentyFortyEightGameEngine game, Scanner reader) {
        game.TFEGameBoard.addRandomTile();
        
        while (true) {
            game.TFEGameBoard.getBoard();
            System.out.println("Current score: " + game.getScore());
            System.out.print("Enter move (up/down/left/right/quit): ");
            
            String command = reader.next().toLowerCase();
            if (command.equals("quit")) {
                break;
            }
            
            if (!game.Action(command)) {
                System.out.println("Invalid move!");
                continue;
            }
        }
    }
}
