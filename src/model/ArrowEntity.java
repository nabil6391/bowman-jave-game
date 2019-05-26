/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.model;

import src.ArrowStateListener;
import src.Direction;
import src.Targetable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author nabil, mahi, shakil
 */
public class ArrowEntity extends Entity {

    private final int yLand;
    int[] xPoints;
    int[] yPoints;
    //Draw blood Splash ********************************************
    BufferedImage img;
    float sequenceOfPicture_splash = 0;//sequence of image for the blood splash
    //Draw blood Dropping *********************************************
    BufferedImage img2;
    float locationOfDrop = 1;//sequence of image for the blood dropping
    int flip = 1;
    private float initialVelocity;
    private float angle;
    private float angleInRadians;
    private float vx;
    private float vy;
    private float flightTime;
    private boolean stopped; // finished flight
    private boolean shot;
    private boolean hit;
    private Direction direction;
    private ArrayList<ArrowStateListener> arrowStateListeners;
    private ArrayList<Targetable> targets;
    private int rotationPointX, rotationPointY;
    private Graphics2D g2d;

    public ArrowEntity(float x, float y, float initialVelocity, float angle, int yLand, ArrayList<Targetable> targets, boolean isArrow) {
        super(x, y, 80, 1, isArrow); // fixed arrow width and height
        this.initialVelocity = initialVelocity; // m/s
        this.angle = 0;
        this.flightTime = 0;
        this.yLand = yLand;
        this.stopped = false;
        this.shot = false;
        this.hit = false;
        xPoints = new int[10];
        yPoints = new int[10];
        this.direction = Direction.RIGHT;
        arrowStateListeners = new ArrayList<>();
        this.targets = targets;
        rotationPointX = rotationPointY = 0;
        setup();


    }

    public void addStateListener(ArrowStateListener asl) {
        arrowStateListeners.add(asl);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private void setup() {
        this.angleInRadians = (float) Math.toRadians(angle);
        this.vx = (float) (initialVelocity * Math.cos(angleInRadians)); // m/s
        this.vx = this.vx * 100 / 180; // px/ticks
        this.vy = (float) (initialVelocity * Math.sin(angleInRadians));
        //System.out.println("x and y "+x+" "+y);

    }

    public float getInitialVelocity() {
        return initialVelocity;
    }

    public void setInitialVelocity(float initialVelocity) {
        this.initialVelocity = initialVelocity * 22 / 100; // full power = 22
        setup();
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = -angle;
        setup();
    }

    public boolean isShot() {
        return shot;
    }

    public void setShot(boolean shot) {
        this.shot = shot;
    }

    @Override
    public void tick() {
        if (!stopped && shot) {
            // time
            flightTime++;
            // speeds: vx constant (ignoring .. stuff), and vy =
            vy = (float) (initialVelocity * Math.sin(angleInRadians) + 9.8 * flightTime / 180); // in m/s
            vy = vy * 100 / 180; // in px/ticks

            // check for land (commented lines allow arrow to stop
            // more accurately on land)
            if ((y + vy) >= yLand - 10) {
                stopped = true;
                y = yLand - 10;
                float ratio = (y + vy - yLand) / vy;
                x = x + ratio * vx;
                fireArrowHitLand(x);
            } else {
                if (direction == Direction.LEFT) {
                    x -= vx;
                } else {
                    x += vx;
                }
                y += vy;
                if (!hit) {
                    for (int i = 0; i < targets.size(); i++) {
                        if (targets.get(i).hitTarget((int) x, (int) y + 15)) {
                            fireHitTarget(targets.get(i));
                            hit = true;
                            color = RED;
                            stopped = true;
                            fireArrowHitLand(x);
                            //makes the arrow go deeper inside the player
                            if (direction == Direction.RIGHT)
                                x += 10;
                            if (direction == Direction.LEFT)
                                x -= 15;
                            //make it more realasim
                            y += 8;


                        }
                    }
                }


            }
        }
    }

    public void drawBloodSplash(Graphics2D g2d) {

        try {
            img = ImageIO.read(new File("src/asset/tmp-" + (int) sequenceOfPicture_splash + ".gif"));

            if (direction == Direction.RIGHT)
                g2d.drawImage(img, (int) x, (int) y - 50, 70, 70, null);
            if (direction == Direction.LEFT)
                g2d.drawImage(img, (int) x, (int) y - 50, -70, 70, null);

            if (sequenceOfPicture_splash <= 8)
                sequenceOfPicture_splash += 0.1;//this is determine how many times it takes to increase a real number "1 ... 2....3..."

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void drawBloodDropping(Graphics2D g2d) {
        try {
            img2 = ImageIO.read(new File("src/asset/BloodDrop.gif"));
            g2d.drawImage(img2, (int) x, (int) y + 20 + (int) locationOfDrop, 5, 5, null);

            if (locationOfDrop >= 600)
                locationOfDrop = 1;

            locationOfDrop += locationOfDrop * 9.8 / 240;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void setRotationFixedPoint(float x, float y) {
        if (rotationPointX == 0 && rotationPointY == 0)//if the rotation point doesnt have value
        {//make fixed value
            if (direction == Direction.RIGHT) {
                rotationPointX = (int) x;
            } else
                rotationPointX = (int) x;

            rotationPointY = (int) y;
        }

    }

    @Override
    public void render(Graphics2D g2d) {
        /**
         * | 4|2|px 
         *  ___  _ 2px
         *  \__\_____________y
         *  /__/ _ 2px 
         * x-w       x-w/2   x
         *
         */
        AffineTransform originalTransform = g2d.getTransform();


        if (direction == Direction.LEFT)
            flip = -1;
        else
            flip = 1;


        xPoints[0] = (int) x - flip * width / 2;
        yPoints[0] = (int) y;
        xPoints[1] = (int) x - flip * width + flip * 6;
        yPoints[1] = (int) y;
        xPoints[2] = (int) x - flip * width + flip * 4;
        yPoints[2] = (int) y - 2;
        xPoints[3] = (int) x - flip * width;
        yPoints[3] = (int) y - 2;
        xPoints[4] = (int) x - flip * width + flip * 2;
        yPoints[4] = (int) y;
        xPoints[5] = (int) x - flip * width + flip * 6;
        yPoints[5] = (int) y;
        xPoints[6] = (int) x - flip * width + flip * 4;
        yPoints[6] = (int) y + 2;
        xPoints[7] = (int) x - flip * width;
        yPoints[7] = (int) y + 2;
        xPoints[8] = (int) x - flip * width + flip * 2;
        yPoints[8] = (int) y;
        xPoints[9] = (int) x;
        yPoints[9] = (int) y;


        if (direction == Direction.RIGHT) {
            if (!shot) {//if it is at the intial point
                g2d.rotate(Math.atan2(vy, vx), rotationPointX, rotationPointY);
                //System.out.println("rotationX "+rotationPointX+" rotationY "+rotationPointY);
            } else {
                g2d.rotate(Math.atan2(vy, vx), x - width, y);
                // System.out.println(rotationPointY);
            }
        } else {//the other direction
            if (!shot) {//if it is at the intial point

                g2d.rotate(Math.atan2(-vy, vx), rotationPointX, rotationPointY);
                // System.out.println("rotationX "+rotationPointX+" rotationY "+rotationPointY);
                // System.out.println("the x value is "+x);
            } else {
                g2d.rotate(Math.atan2(-vy, vx), x + width, y);

            }
        }

        switch (color)//determine which color to choose
        {
            case GREEN:
                g2d.setColor(Color.GREEN);
                break;
            case RED:
                g2d.setColor(Color.RED);
                break;
            default:
                g2d.setColor(Color.BLACK);
        }

        g2d.drawPolygon(xPoints, yPoints, 10);
        g2d.setColor(Color.black);//the rest of the picture make it BLACK


        g2d.setTransform(originalTransform);

        if (hit)//draw blood animation
        {
            drawBloodSplash(g2d);
            drawBloodDropping(g2d);
        }


    }

    private void fireArrowHitLand(float x) {
        for (int i = 0; i < arrowStateListeners.size(); i++) {
            arrowStateListeners.get(i).arrowHitLand((int) x);
        }
    }

    private void fireHitTarget(Targetable t) {
        for (int i = 0; i < arrowStateListeners.size(); i++) {
            arrowStateListeners.get(i).arrowHitTarget(t);
        }
    }

}
