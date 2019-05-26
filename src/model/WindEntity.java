package src.model;

import java.awt.*;

/**
 * @author nabil, mahi, shakil
 */
public class WindEntity extends Entity {

    private float angle;
    private float power;
    double probability;
    public WindEntity(float x, float y, int width, int height, float angle) {
        super(x, y, width, height);
        this.angle = angle;
        this.power = 1;

        probability = Math.random() * 1000;
    }

    public float getAngle() {
        return angle;
    }


    @Override
    public void tick() {
        if (probability < 1) {
            power = (float) (Math.random() * 6);
            angle = (float) (-Math.random() * 90);
        }
        probability = Math.random() * 1000;
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