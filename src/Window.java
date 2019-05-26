package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class just creates the main window for the game
 *
 * @author nabil
 */
public class Window {

    public Window(int width, int height, Game game, String title) {
        //set game size to window size
        Dimension dimension = new Dimension(width, height);
        game.setPreferredSize(dimension);
        game.setMaximumSize(dimension);
        game.setMinimumSize(dimension);


        JFrame window = new JFrame(title); // create window

        // background image add to the window jframe
//        JPanel p = new JPanel();
//        ImageIcon i = new ImageIcon("./src/sample.jpg");
//        JLabel l = new JLabel();
//        l.setIcon(i);
//        p.add(l);
//
//        window.add(p);

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