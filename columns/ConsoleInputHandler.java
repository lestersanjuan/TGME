package columns;

import java.util.Scanner;

public class ConsoleInputHandler implements InputHandler {
    private Scanner scanner;
    
    public ConsoleInputHandler() {
        this.scanner = new Scanner(System.in);
    }
    
    @Override
    public String getNextMove(int playerNum) {
        if (playerNum == 1) {
            System.out.println("Player 1 Move (a=left, d=right, s=rotate, enter=down): ");
        } else {
            System.out.println("Player 2 Move (j=left, l=right, k=rotate, enter=down): ");
        }
        return scanner.nextLine().trim().toLowerCase();
    }
    
    @Override
    public void waitForEnter() {
        scanner.nextLine();
    }
    
    @Override
    public int getIntChoice(int min, int max) {
        int choice = -1;
        while (choice < min || choice > max) {
            System.out.print("Enter your choice (" + min + "-" + max + "): ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice < min || choice > max) {
                    System.out.println("Please enter a number between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
        return choice;
    }
    
    @Override
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}