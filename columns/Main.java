package columns;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ColumnsGameEngine2pINP game = new ColumnsGameEngine2pINP();
        
        System.out.println("Welcome to Columns!");
        System.out.println("Select game mode:");
        System.out.println("1. Single Player");
        System.out.println("2. Two Players");
        
        int choice = 0;
        while (choice != 1 && choice != 2) {
            System.out.print("Enter your choice (1 or 2): ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter 1 or 2.");
            }
        }
        game.initializeGame(choice);
        
        System.out.println("\nPieces will fall one block at a time.");
        if (choice == 2) {
            System.out.println("Player 1 Controls: 'a' - left, 'd' - right, 's' - rotate, Enter - down");
            System.out.println("Player 2 Controls: 'j' - left, 'l' - right, 'k' - rotate, Enter - down");
        } else {
            System.out.println("Controls: 'a'=left, 'd'=right, 's'=rotate, 'Enter'=down");
        }
        System.out.println("Press Enter to start the game (press Ctrl+C to quit)...");
        scanner.nextLine();
        
        
        // Main game loop
        while (game.isGameRunning()) {
            System.out.print(game.getCurrentPrompt());
            String input = scanner.nextLine().trim().toLowerCase();
            boolean success = game.Action(input);
            
            if (!success) {
                System.out.println("Press Enter to exit...");
                scanner.nextLine();
                break;
            }
        }
        
        scanner.close();
    }
}