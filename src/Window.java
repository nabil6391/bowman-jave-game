package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This class just creates the main window for the game
 *
 * @author a-haydar
 */
public class Window {

    public Window(int width, int height, Game game, String title) {
        //set game size to window size
        Dimension dimension = new Dimension(width, height);
        game.setPreferredSize(dimension);
        game.setMaximumSize(dimension);
        game.setMinimumSize(dimension);

        JFrame window = new JFrame(title); // create window
        window.add(game); // add game component to window
        window.pack();  // recalculate dimensions, in order for everything to appear correctly
        window.setVisible(true); // default is not visible

        // closing window means terminating the program
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // in case the above was not enough, make sure to exit
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                game.stop();
                System.exit(0);
            }

        });

        window.setResizable(false); // make window size fixed
        window.setLocationRelativeTo(null); // show window at screen center
    }

}
