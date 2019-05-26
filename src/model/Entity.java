/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.model;

import java.awt.*;

/**
 * Root class of all entities with common functionality.
 *
 * @author nabil, mahi, shakil
 */
public abstract class Entity {

    public final int BLACK = 1, RED = 2, GREEN = 3;
    public boolean isArrow = false;
    public int color;
    protected float x;
    protected float y;
    protected int width;
    protected int height;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        color = BLACK;
    }

    public Entity(float x, float y, int width, int height, boolean isArrow) {
        this(x, y, width, height);
        this.isArrow = isArrow;
    }

    public abstract void tick();

    public abstract void render(Graphics2D g2d);

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
}