package src.model;

import java.awt.*;

/**
 * @author nabil, mahi, shakil
 */
public class WindEntity extends Entity {

    private float angle;
    float pps, xSpeedCounter;
    private float power;

    public WindEntity(float x, float y, int width, int height, float angle, float pps) {
        super(x, y, width, height);
        this.angle = angle;
        this.power = 1;
        this.pps = pps;
        this.xSpeedCounter = Math.abs(pps);
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
//                    power++;
//                    angle++;

                } else if (pps < 0) {
//                    power--;
//                    angle--;

                }
                xSpeedCounter = pps;
            }
        }
    }

    @Override
    public void render(Graphics2D g2d) {

    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }
}