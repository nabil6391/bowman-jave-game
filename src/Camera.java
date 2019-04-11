package src;

import src.model.Entity;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * @author a-haydar
 */
public class Camera implements MouseListener, MouseMotionListener {

    float x, y;
    int width, height;
    int ch, cw, bh, bw, margin; // camera controls
    Game game;
    // mouse
    boolean dragged = false;
    boolean pressed = false;
    int pressX, pastPressX;
    int pressY;

    public Camera(Game game, float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.game = game;
        ch = 14;
        cw = 10;
        bh = 6;
        bw = game.getGameWidth() / 10;
        margin = 10;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void tick(Entity centered) {
        if (centered == null) {
            //x++;
        } else {
            x = Math.min(centered.getX() - width / 2, game.getGameWidth());
            y = Math.min(centered.getY() - height / 2, game.getGameHeight() - height);
        }
        //check mouse
        if (pressed || dragged) {
            x = (pressX - (width - bw - margin)) * 10;
        }
    }

    public void renderBarControl(Graphics2D g2d) {
        // draw x control
        g2d.drawRect(width - bw - margin - 50, height - bh - margin, bw, bh);
        g2d.fillRect((int) (width - bw - margin + x / 10), height - ch - margin + bh / 2 + 1, cw, ch);
    }

    public void centerOn(Entity e) {
        setX(e.getX() - width / 2 + e.getWidth() / 2);
        setY(e.getY() - height / 2 + e.getHeight() / 2);
    }

    private boolean onControl(int x, int y) {
        return (x >= width - bw - margin + x / 10 && x <= width - bw - margin + x / 10 + cw)
                && (y >= height - ch - margin + bh / 2 + 1 && y <= height - margin + bh / 2 + 1);
    }

    private boolean onBar(int x, int y) {
        return (x >= width - bw - margin - 50 && x <= width - margin)
                && (y >= height - bh - margin && y <= height - margin);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (onBar(e.getX(), e.getY())) {
            pressed = true;
            pressX = e.getX();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressed = false;
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
        if (onBar(e.getX(), e.getY())) {
            dragged = true;
            pressX = e.getX();
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
