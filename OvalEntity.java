/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowman;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;

/**
 *
 * @author a-haydar
 */
public class OvalEntity extends Entity implements MouseListener, MouseMotionListener, KeyListener {

    private double angle = 0;
    private int pressX;
    private int pressY;
    private boolean dragged = false;

    public OvalEntity(float x, float y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void tick() {
        // lets rotate 1 times in a second
        // since we have ~60 ticks per second, we need to increase angle
        // by 360/60 each second for a full rotation in 1 second 
        // which is 6 degrees for each tick
        // actually, lets make it slower by rotating once every 2 seconds
        // that is, 3 degrees/tick
        angle += 3;
        angle %= 360; // to stay between 0 - 359
    }

    @Override
    public void render(Graphics2D g2d) {
        // save old transform so that we can go back
        AffineTransform original = g2d.getTransform();
        // rotate about center
        g2d.rotate(Math.toRadians(angle), x + width / 2, y + height / 2);
        // draw oval
        g2d.drawOval((int) x, (int) y, width, height);
        // return to old transformation
        g2d.setTransform(original);
        
        if (dragged) {
            g2d.drawString("["+x+","+y+"]", 0, 15);
        } else {
            g2d.drawString("Click and drag mouse up/down to change height or right/left to change width", 0, 15);
        }
    }

    public void setWidth(int width) {
        int w = this.width;
        if (width >= 200 && width <= 600) {
            this.width = width;
        } else if (width < 200) {
            this.width = 200;
        } else {
            this.width = 600;
        }
        this.x = this.x + (w-this.width)/2;
    }

    public void setHeight(int height) {
        int h = this.height;
        if (height >= 100 && height <= 300) {
            this.height = height;
        } else if (height < 100) {
            this.height = 100;
        } else {
            this.height = 300;
        }
        this.y = this.y + (h-this.height)/2;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressX = e.getX();
        pressY = e.getY();
        System.out.println("Pressed!");
        //this.x = pressX - width/2;
        //this.y = pressY - height/2;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragged = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        dragged = true;
        setWidth(width + (pressX - e.getX()));
        setHeight(height + (pressY - e.getY()));
        pressX = e.getX();
        pressY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
     
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
                setHeight(height + 5);
                break;
            case KeyEvent.VK_DOWN:
                setHeight(height - 5);
                break;
            case KeyEvent.VK_LEFT:
                setWidth(width - 5);
                break;
            case KeyEvent.VK_RIGHT:
                setWidth(width + 5);
                break;
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

}
