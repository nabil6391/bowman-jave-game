package src.model;

import java.awt.*;

/**
 * @author a-haydar
 */
public class CloudEntity extends Entity {

    int[] xPoints;
    int[] yPoints;
    float pps, xSpeedCounter;
    Color color;
    float randomBlue;

    public CloudEntity(float x, float y, int width, int height, float pps) {
        super(x, y, width, height);
        xPoints = new int[13];
        yPoints = new int[13];
        this.pps = pps;
        this.xSpeedCounter = Math.abs(pps);
        randomBlue = (float) Math.random() % 0.6f + 0.3f;
        this.color = new Color(randomBlue, randomBlue, 1.0f, 1f);
    }

    @Override
    public void tick() {
        if (pps != 0) {
            xSpeedCounter += Math.abs(pps);
            if (xSpeedCounter >= 1) {
                if (pps > 0) {
                    x++;
                } else if (pps < 0) {
                    x--;
                }
                xSpeedCounter = pps;
            }
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        /**    x-5w/8       
         *     _________                y-h
         *  __/         \_______        y-3h/4
         * /                    \__     y-2h/4
         * \                         \  y-h/4
         *  \___________x____________/x+w  y
         * x-w                   x+4w/8
         *  x-7w/8                 x+6w/8
         *    x-6w/8     x+w/8       x+7w/8
         */
        xPoints[0] = (int) x - width;
        yPoints[0] = (int) y;
        xPoints[1] = (int) x + 7 * width / 8;
        yPoints[1] = (int) y;
        xPoints[2] = (int) x + width;
        yPoints[2] = (int) y - height / 4;
        xPoints[3] = (int) x + 6 * width / 8;
        yPoints[3] = (int) y - 2 * height / 4;
        xPoints[4] = (int) x + 4 * width / 8;
        yPoints[4] = (int) y - 2 * height / 4;
        xPoints[5] = (int) x + 3 * width / 8;
        yPoints[5] = (int) y - 3 * height / 4;
        xPoints[6] = (int) x + width / 8;
        yPoints[6] = (int) y - 3 * height / 4;
        xPoints[7] = (int) x;
        yPoints[7] = (int) y - height;
        xPoints[8] = (int) x - 5 * width / 8;
        yPoints[8] = (int) y - height;
        xPoints[9] = (int) x - 6 * width / 8;
        yPoints[9] = (int) y - 3 * height / 4;
        xPoints[10] = (int) x - 7 * width / 8;
        yPoints[10] = (int) y - 3 * height / 4;
        xPoints[11] = (int) x - width;
        yPoints[11] = (int) y - 2 * height / 4;
        xPoints[12] = (int) x - 7 * width / 8;
        yPoints[12] = (int) y;
        Color temp = g2d.getColor();
        g2d.setColor(color);
        g2d.fillPolygon(xPoints, yPoints, 13);
        g2d.setColor(temp);
    }
}