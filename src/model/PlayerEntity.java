/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.model;

import src.Direction;
import src.Targetable;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * @author nabil
 */
public class PlayerEntity extends Entity implements Targetable {

    private int health;
    private float angle, power;
    private String name;
    private boolean active;
    private ArrowEntity arrow;
    private Direction direction;

    public PlayerEntity(float x, float y, int width, int height, String name) {
        super(x, y, width, 170); // ignoring width and height since we are hardcoding
        this.health = 100;
        this.power = 0;
        this.angle = 0;
        this.name = name;
        this.active = false;
        this.direction = Direction.RIGHT;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        if (angle >= 1 && angle <= 90) {
            this.angle = angle;
        } else if (angle < 1) {
            this.angle = 1;
        } else {
            this.angle = 90;
        }
        arrow.setAngle(angle);
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        if (power >= 2 && power <= 100) {
            this.power = power;
        } else if (power < 2) {
            this.power = 2;
        } else {
            this.power = 100;
        }
        arrow.setInitialVelocity(power);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrowEntity getArrow() {
        return arrow;
    }

    public void setArrow(ArrowEntity arrow) {
        this.arrow = arrow;
        this.arrow.setDirection(direction);
        arrow.setX(x + width + arrow.getWidth());
        arrow.setY(y + height / 3);
        arrow.setRotationFixedPoint(x + 15, y + 40);
    }

    public void shoot() {
        this.arrow.setShot(true);
        this.arrow = null;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics2D g2d) {
        Stroke originalStroke = g2d.getStroke();
        Stroke boldStroke = new BasicStroke(4);

        g2d.setStroke(boldStroke);
        // head
        g2d.drawOval((int) x, (int) y, 30, 30);
        // body
        g2d.drawOval((int) x, (int) y + 35, 30, 70);
        // legs
        if (direction == Direction.RIGHT) {
            g2d.drawLine((int) x + 5, (int) y + 100, (int) x + 10, (int) y + 130);
            g2d.drawLine((int) x + 10, (int) y + 130, (int) x + 5, (int) y + 155);
            g2d.drawLine((int) x + 25, (int) y + 100, (int) x + 30, (int) y + 130);
            g2d.drawLine((int) x + 30, (int) y + 130, (int) x + 25, (int) y + 155);
            // feet
            g2d.fillRoundRect((int) x, (int) y + 155, 15, 7, 3, 3);
            g2d.fillRoundRect((int) x + 20, (int) y + 155, 15, 7, 3, 3);
        } else {
            g2d.drawLine((int) x + 5, (int) y + 100, (int) x, (int) y + 130);
            g2d.drawLine((int) x, (int) y + 130, (int) x + 5, (int) y + 155);
            g2d.drawLine((int) x + 25, (int) y + 100, (int) x + 20, (int) y + 130);
            g2d.drawLine((int) x + 20, (int) y + 130, (int) x + 25, (int) y + 155);
            // feet
            g2d.fillRoundRect((int) x - 5, (int) y + 155, 15, 7, 3, 3);
            g2d.fillRoundRect((int) x + 15, (int) y + 155, 15, 7, 3, 3);
        }
        AffineTransform originalTransform = g2d.getTransform();
        // bow
        if (direction == Direction.RIGHT) {
            g2d.rotate(Math.toRadians(-angle), x + 15, y + 40);
            g2d.drawArc((int) x + 40, (int) y + 5, 30, 80, -90, 180);
        } else {
            g2d.rotate(Math.toRadians(angle), x + 15, y + 40);
            g2d.drawArc((int) x - 40, (int) y + 5, 30, 80, 90, 180);
        }
        if (arrow != null) {
            //arrow.setTransform(g2d.getTransform());
            //g2d.getTransform().translate(x+15, y+40);
            //g2d.getTransform().translate(-(x + 15), -(y + 40));
            //Point2D tip = g2d.getTransform().transform(new Point((int) (x + 55 - power / 3), (int) y + 45), null);
            //g2d.getTransform().translate(x + 15, y + 40);
            //g2d.getTransform().translate(-(x+15), -(y+40));
            //arrow.setX((float) tip.getX() - 300);//this is wrong
            //arrow.setY((float) tip.getY() + 600);//an so is this
            //System.out.println(tip.toString());
        }
        // hands
        if (direction == Direction.RIGHT) {
            if (arrow != null) {
                arrow.setX(x + 55 + arrow.getWidth() - power / 3);
                arrow.setY(y + 45);

            }
            g2d.drawLine((int) x + 15, (int) y + 40, (int) x + 70, (int) y + 45);
            g2d.drawLine((int) (x + 55 - power / 3), (int) y + 45, (int) (x + 35 - power / 3), (int) y + 45);
            g2d.drawLine((int) (x + 35 - power / 3), (int) y + 45, (int) x + 15, (int) y + 40);
            g2d.setStroke(originalStroke);
            g2d.drawLine((int) (x + 55 - power / 3), (int) y + 45, (int) x + 55, (int) y + 5);
            g2d.drawLine((int) (x + 55 - power / 3), (int) y + 45, (int) x + 55, (int) y + 85);
        } else {
            if (arrow != null) {
                // arrow.setX(x - 25 - arrow.getWidth() + power / 3);
                arrow.setX(x - 25 - arrow.getWidth() + power / 3);
                arrow.setY(y + 45);
            }
            g2d.drawLine((int) x + 15, (int) y + 40, (int) x - 40, (int) y + 45);
            g2d.drawLine((int) (x - 25 + power / 3), (int) y + 45, (int) (x - 5 + power / 3), (int) y + 45);
            g2d.drawLine((int) (x - 5 + power / 3), (int) y + 45, (int) x + 15, (int) y + 40);
            g2d.setStroke(originalStroke);
            g2d.drawLine((int) (x - 25 + power / 3), (int) y + 45, (int) x - 25, (int) y + 5);
            g2d.drawLine((int) (x - 25 + power / 3), (int) y + 45, (int) x - 25, (int) y + 85);
        }
        g2d.setTransform(originalTransform);
        //draw bounding boxes
        //Color c = g2d.getColor();
        //g2d.setColor(Color.PINK);
        //g2d.drawRect((int)x, (int)y, 30, 30); //head
        //g2d.drawRect((int)x, (int)y+31, 30, 69); //body
        //g2d.drawRect((int)x, (int)y+101, 30, 65); //legs
        //g2d.setColor(c);
        //g2d.drawRoundRect((int) x, (int) y, width, height, 10, 10);
    }

    @Override
    public boolean hitTarget(int x, int y) {
        // check head no need to be precise
        if (x >= this.x && x <= this.x + 30
                && y >= this.y && y <= this.y + 30) {
            // hit to the head = -50
            health -= 50;

            System.out.println(name + " -50");
            return true;
        } else if (x >= this.x && x <= this.x + 30
                && y >= this.y + 31 && y <= this.y + 100) {
            // check body
            // hit to the body = -25
            health -= 25;
            System.out.println(name + " -25");
            return true;

        } else if (x >= this.x && x <= this.x + 30
                && y >= this.y + 101 && y <= this.y + 165) {
            // check legs
            // hit to the legs = -15
            health -= 15;
            System.out.println(name + " -15");
            return true;
        }
        return false;
    }

}
