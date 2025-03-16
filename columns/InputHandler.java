package columns;

public interface InputHandler {
    String getNextMove(int playernum);
    void waitForEnter();
    int getIntChoice(int min, int max);
    void close();
}
