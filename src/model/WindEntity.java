package src.model;

import java.awt.*;

/**
 * @author nabil, mahi, shakil
 */
public class WindEntity extends Entity {

    private float angle;
    int[] xPoints;
    int[] yPoints;
    float pps, xSpeedCounter;
    Color color;
    float randomBlue;

    public WindEntity(float x, float y, int width, int height, float angle, float pps) {
        super(x, y, width, height);
        this.angle = angle;
        xPoints = new int[13];
        yPoints = new int[13];
        this.pps = pps;
        this.xSpeedCounter = Math.abs(pps);
        randomBlue = (float) Math.random() % 0.6f + 0.3f;
        this.color = new Color(randomBlue, randomBlue, 1.0f, 1f);
    }

    public float getAngle() {
        return angle;
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

    }
}