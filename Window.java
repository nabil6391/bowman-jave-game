package bowman;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * This class just creates the main window for the game
 * 
 * @author a-haydar
 */
public class Window {

    public Window(int w, int h, Game game, String title) {
        //set game size to window size
        game.setPreferredSize(new Dimension(w, h));
        game.setMaximumSize(new Dimension(w, h));
        game.setMinimumSize(new Dimension(w, h));
        
        JFrame window = new JFrame(title); // create window
        window.add(game); // add game component to window
        window.pack();  // recalculate dimentions, in order for everything to 
                        // appear correctly
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
