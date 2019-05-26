package src;

import javax.swing.*;

public class Background extends JFrame {
    Background(){
        JLabel background;
        setSize(1200,700);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        ImageIcon img = new ImageIcon("sample.jpg");
        background = new JLabel("",img,JLabel.CENTER);
        background.setBounds(0,0,1200,700);
        add(background);
        System.out.println("This is ");

        setVisible(true);

    }
    public static void main(String[] args) {
        Background bgimg = new Background();

    }
}
