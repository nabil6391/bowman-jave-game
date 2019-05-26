package src;

/**
 * Main class responsible for starting the game
 *
 * @author nabil, mahi, shakil
 */
public class Bowman {

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;
    private static final String TITLE = "Bowman Game";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // create game & start it
        Game game = new Game(WINDOW_WIDTH, WINDOW_HEIGHT);
        // create window with this game
        Window window = new Window(WINDOW_WIDTH, WINDOW_HEIGHT, game, TITLE);
        // start game
        game.start();
    }
}