/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.model;

import java.awt.*;

/**
 * @author Nabil, Mahi, Shakil
 */
public class ShrubEntity extends Entity {
    int[] xPoints;
    int[] yPoints;
    float randomGreen = (float) Math.random() % 0.8f + 0.2f;

    public ShrubEntity(float x, float y, int width, int height) {
        super(x, y, width, height);
        xPoints = new int[9];
        yPoints = new int[9];

        /**
         *               x
         *
         *
         * x-10                        x+10
         *
         *
         *      x-5  x-2    x+2   x+5
         *
         *
         *           x-2    x+2
         */
        xPoints[0] = (int) x - width / 4;
        yPoints[0] = (int) y;
        xPoints[1] = (int) x - width / 2;
        yPoints[1] = (int) y - height / 3;
        xPoints[2] = (int) x - width;
        yPoints[2] = (int) y - 2 * height / 3;
        xPoints[3] = (int) x - width / 4;
        yPoints[3] = (int) y - height / 3;
        xPoints[4] = (int) x;
        yPoints[4] = (int) y - height;
        xPoints[5] = (int) x + width / 4;
        yPoints[5] = (int) y - height / 3;
        xPoints[6] = (int) x + width;
        yPoints[6] = (int) y - 2 * height / 3;
        xPoints[7] = (int) x + width / 2;
        yPoints[7] = (int) y - height / 3;
        xPoints[8] = (int) x + width / 4;
        yPoints[8] = (int) y;
    }

    @Override
    public void tick() {
        // animation?
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(new Color(0.0f, randomGreen, 0.0f, 1.0f));
        g2d.fillPolygon(xPoints, yPoints, 9);
        g2d.setColor(Color.black);
    }
}