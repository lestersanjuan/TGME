import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        System.out.println("meow");

        TwentyFortyEightGameEngine man = new TwentyFortyEightGameEngine();
        man.TFEGameBoard.addRandomTile();

        Scanner reader = new Scanner(System.in);

        while (true) {
            man.TFEGameBoard.getBoard();

            System.out.print("up, down, left, right, quit");
            String command = reader.next();
            if (command.equals("up")) {
                man.up();
            } else if (command.equals("down")) {
                man.down();
            } else if (command.equals("left")) {
                man.left();
            } else if (command.equals("right")) {
                man.right();
            } else if (command.equals("quit")) {
                System.out.println("goodbye.");
                break;
            } else {
                System.out.println("what the hell");
                continue;
            }

            man.TFEGameBoard.addRandomTile();
        }

        reader.close();
    }
}
