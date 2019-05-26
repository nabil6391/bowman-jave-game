package src;

import src.model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
//import sun.java2d.pipe.DrawImage;

/**
 * Game class will be the main class that contain the game and everything
 * related to it. This will run in its own thread.
 *
 * @author a-haydar
 */
public class Game extends Canvas implements Runnable, MouseListener, MouseMotionListener, ArrowStateListener {

    int imgNumber = 0;
    BufferedImage img;
    private int gameWidth;
    private int gameHeight;
    private int windowWidth;
    private int windowHeight;
    private int groundYPosition; // ground position
    private PlayerEntity p1, p2;


    private ArrowEntity currentArrow;
    private Stack<GameState> state;
    private boolean running = false;
    private Thread gameThread;
    // needed for drawing graphics
    private BufferStrategy bufferedGraphics;
    private Graphics2D g;
    // Camera
    private Camera cam;
    // game entities
    private ArrayList<Entity> entities;
    private ArrayList<Targetable> targets;
    // game speed statistics
    private int frames = 0;
    private int lastFrames = 0;
    private long framesTime = 0;
    private int secondsSoFar = 0;
    private int mouseX;
    private int mouseY;
    private boolean gameover = false;

    public Game(int width, int height) {
        gameWidth = 5000;
        gameHeight = 2 * height;
        windowHeight = height;
        windowWidth = width;
        groundYPosition = gameHeight - 80;
        state = new Stack<>();
        state.push(GameState.STARTUP);
    }

    /**
     * Get the value of gameWidth
     *
     * @return the value of gameWidth
     */
    public int getGameWidth() {
        return gameWidth;
    }

    /**
     * Get the value of gameHeight
     *
     * @return the value of gameHeight
     */
    public int getGameHeight() {
        return gameHeight;
    }

    /**
     * The start() method is used to start our game thread if it has not been
     * started before. A new thread is created. The method is synchronized to
     * avoid potential race conditions
     */
    public synchronized void start() {
        if (!running) {
            gameThread = new Thread(this);
            gameThread.start();
            running = true;
        }
    }

    /**
     * The stop() method is used to stop our game thread if it has been started
     * before. The method is synchronized to avoid potential race conditions
     */
    public synchronized void stop() {
        if (running) {
            try {
                running = false;
                gameThread.join();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
     * This is the method executed when the thread is started (using start()).
     * run() contains the game loop which basically works as follows:
     * <p>
     * while game is running calculate or update game logic or objects draw game
     * <p>
     * Since computing power is different from one computer to another, we need
     * to ensure that game speed is limited so that it is the same on different
     * machines. This is done simply as follows:
     * <p>
     * - choose number of updates/renders (called frame/tick) you want per
     * second. - calculate how many nano seconds are required for each
     * update/renderBarControl. - measure time continously to keep how much time
     * has passed, and once enough time for a frame/tick has passed, do the
     * update/tick
     */
    @Override
    public void run() {
        // first init things
        init();

        //game loop calculations
        int fps = 60; // frames/ticks/updates we want per second
        double nanoSecPerFrame = 1000000000 / fps; // nanosec to give a frame
        double elapsedTimePercent = 0;
        long currentTime;
        long lastTime = System.nanoTime();

        //game loop
        while (running) {
            currentTime = System.nanoTime();
            // calculate elapsed time percent and add it to previous elapsed time
            elapsedTimePercent += (currentTime - lastTime) / nanoSecPerFrame;
            // time calculation for statistics
            framesTime += (currentTime - lastTime);
            // check if the time elapsed until now is enough for a frame/tick
            if (elapsedTimePercent >= 1) {
                tick();
                // we finished a frame, so remove its time to restart calculation
                // otherwise, elapsedTimePercent will always be > 1
                elapsedTimePercent--;
                //for statistics
                frames++;
            }

            try {
                render();
            } catch (IOException e) {
                e.printStackTrace();
            }


            // set lastTime for next calculation
            lastTime = currentTime;

            //TODO find a better way to reduce CPU load
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // not running anymore, stop thread
        //stop();
    }

    /**
     * the tick() method is sometimes called update(). This is where you do the
     * game calculations like moving objects.
     */
    private void tick() {
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).tick();
        }
        switch (state.peek()) {
            case STARTUP:
                cam.tick(null);
                break;
            case P1ACTIVE:
                cam.tick(p1);
                break;
            case P2ACTIVE:
                cam.tick(p2);
                break;
            case ARROWAWAY:
                cam.tick(currentArrow);
                break;
            case GAMEOVER:
                renderGameoverMessage(g);
                break;
            default:
                break;
        }

    }

    public void drawSkeletonAnimation() {

        imgNumber %= 8; //whatever happened it would be from 0-7
        try {
            img = ImageIO.read(new File("./skeleton" + imgNumber + ".gif"));
            imgNumber++;
            g.drawImage(img, (int) cam.getX() + cam.getWidth() / 2, (int) cam.getY() + cam.getHeight() / 2, null);
            g.setColor(Color.green);
            g.fillRect((int) cam.getX() * 2, (int) cam.getY() / 2, 3000, 3000);
            System.out.println("image drawn sucessfully at " + cam.getX() + " " + cam.getY());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    /**
     * the renderBarControl() method is where you do all your drawing to the
     * screen.
     */
    private void render() throws IOException {
        // get accelerated graphics
        bufferedGraphics = getBufferStrategy();
        if (bufferedGraphics == null) {
            createBufferStrategy(3);
            return; // exit to avoid graphic problems
        }
        g = (Graphics2D) bufferedGraphics.getDrawGraphics();

        // ---------------start drawing things----------------------
        g.translate(-cam.getX(), -cam.getY()); // follow cam

        // clear screen
        g.clearRect((int) cam.getX(), (int) cam.getY(), cam.getWidth(), cam.getHeight());

        //draw white Background

//        g.setColor(new Color(1f, 1f, 1f, 1f));
//        g.fillRect((int) cam.getX(), (int) cam.getY(), cam.getWidth(), cam.getHeight());

        g.setColor(Color.black);
        // draw land
        g.drawLine((int) cam.getX(), groundYPosition, (int) (cam.getX() + cam.getWidth()), groundYPosition);

        // renderBarControl entities
        for (int i = 0; i < entities.size(); i++) {
            Entity getEntity = entities.get(i);
            if (visible(getEntity)) {
                getEntity.render(g);
            }
        }

        switch (state.peek()) {
            case STARTUP:
                renderStartupMessage(g);
                break;
            case GAMEOVER:
                renderGameoverMessage(g);
                break;
            default:
                break;
        }

        g.translate(cam.getX(), cam.getY()); // restore after cam

        // renderBarControl information and controls
        renderHUD(g);
        cam.renderBarControl(g);

        // ---------------finished drawing things-------------------

        bufferedGraphics.show();
//        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    private void drawLand() {

    }

    private void renderStartupMessage(Graphics2D g2d) {
        Color c = g2d.getColor();
        g2d.setColor(new Color(0.2f, 0.2f, 0.2f, 0.5f));
        g2d.fillRect((int) cam.getX(), (int) cam.getY(), cam.getWidth(), cam.getHeight());
        g2d.setBackground(Color.white);
        g2d.setColor(c);
        g2d.drawString("Assalamu Alaikum. Click any key  to start Game", cam.getX() + cam.getWidth() / 2 - 80, cam.getY() + cam.getHeight() / 2);

//        JFrame background = new JFrame(); // create window
//        // background image add to the window jframe
//        JPanel p = new JPanel();
//        ImageIcon i = new ImageIcon("D:/personal/programming/java/Bowman/src/sample.jpg");
//        JLabel l = new JLabel();
//        l.setIcon(i);
//        p.add(l);
//        background.add(p);
//        background.setVisible(true);
    }

    private void renderGameoverMessage(Graphics2D g2d) {
        Color c = g2d.getColor();
        g2d.setColor(new Color(0.2f, 0.2f, 0.2f, 0.5f));
        g2d.fillRect((int) cam.getX(), (int) cam.getY(), cam.getWidth(), cam.getHeight());
        g2d.setColor(c);
        g2d.drawString("Game Over", cam.getX() + cam.getWidth() / 2 - 80, cam.getY() + cam.getHeight() / 2);
    }

    private void init() {
        entities = new ArrayList<>();



        initShrubs();
        initClouds();
        initCamera();
        initPlayers();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    private void initCamera() {
        // Camera, started at bottom of game
        cam = new Camera(this, 0, gameHeight - windowHeight, windowWidth, windowHeight);
        addMouseListener(cam);
        addMouseMotionListener(cam);
    }

    private void initShrubs() {
        //shrubs; 20; random
        float xPos;
        int sw, sh;
        for (int i = 0; i < 60; i++) {
            xPos = (float) (Math.random() * (gameWidth - 400)) + 200;
            sw = (int) (Math.random() * (10)) + 5;
            sh = (int) (Math.random() * (40)) + 20;
            entities.add(new ShrubEntity(xPos, (float) groundYPosition, sw, sh));
        }
    }

    private void initClouds() {
        //Clouds; 20; random
        float xPos, yPos;
        int cw, ch;
        for (int i = 0; i < 20; i++) {
            xPos = (float) (Math.random() * (gameWidth - 400));
            yPos = (float) (Math.random() * (gameHeight / 2)) + 200;
            //cameraWidth = (int) (Math.random()*(160)) + 40;
            cw = (int) (Math.random() * (60)) + 15;
            //ch = (int) (Math.random()*(80)) + 20;
            ch = 2 * cw / 3;
            entities.add(new CloudEntity(xPos, yPos, cw, ch, (float) (Math.random()) - 0.5f));
        }
    }

    private void initPlayers() {
        p1 = new PlayerEntity(200, groundYPosition - 160, 60, 160, "Player 1");
        p2 = new PlayerEntity((float) (Math.random() * (gameWidth - 1500) + 1000), groundYPosition - 160, 60, 160, "Player 2");
        p2.setDirection(Direction.LEFT);
        entities.add(p1);
        entities.add(p2);
        targets = new ArrayList<>();
        targets.add(p1);
        targets.add(p2);
    }

    /**
     * Display the current frames per second to the screen.
     *
     * @param g2d Graphics2D model used for drawing.
     */
    private void renderHUD(Graphics2D g2d) {
        //players
        int padding = 15;
        renderPlayerStats(g2d, p1, 200, 10, padding, padding);
        int x2 = windowWidth - 200 - padding;
        renderPlayerStats(g2d, p2, 200, 10, x2, padding);

        // fps
        g2d.drawString("FPS: " + lastFrames + "      [" + secondsSoFar + "] seconds   <" + (cam.getX() + cam.getWidth() / 2) + " | " + (cam.getY() + cam.getHeight() / 2) + ">", 5, windowHeight - 5);
        if (framesTime >= 1000000000) { // if a second has passed
            lastFrames = frames;
            frames = 0;
            framesTime = 0;
            secondsSoFar++;
        }
    }

    /**
     * Display player stats.
     *
     * @param g2d Graphics2D model used for drawing.
     * @param bw  bar width
     * @param bh  bar height
     * @param x   bar x coord.
     * @param y   bar y coord.
     * @param p   the player we display information about
     */
    private void renderPlayerStats(Graphics2D g2d, PlayerEntity p, int bw, int bh, int x, int y) {
        g2d.drawString(p.getName(), x, y);
        renderBar(g2d, x, y + 10, bw, bh, bw * p.getHealth() / 100);
        g2d.drawString("Power :" + p.getPower(), x, y + 35);
        g2d.drawString("Angle :" + p.getAngle(), x, y + 50);
    }

    /**
     * Draws a progress bar with its value.
     *
     * @param x     bar x coordinate
     * @param y     bar y coordinate
     * @param w     bar width
     * @param h     bar height
     * @param value how full is the bar
     */
    private void renderBar(Graphics2D g2d, int x, int y, int w, int h, int value) {
        g2d.drawRoundRect(x, y, w, h, 5, 5);
        g2d.setColor(new Color(0.86f, 0.08f, 0.23f));//makes the life bar bloody color
        g2d.fillRoundRect(x, y, value, h, 5, 5);
        g2d.setColor(Color.black);
    }

    private boolean visible(Entity entity) {
        // instead of checking if they are overlapping, check if they are not
        // if (entityRight < camLeft || entityLeft > camRight) return false
        if (entity.getX() + entity.getWidth() < cam.getX() || entity.getX() - entity.getWidth() > cam.getX() + cam.getWidth()) {
            return false;
        }
        // if (entityBottom < camTop || entityTop > camBottom) return false or return true if not
        return !(entity.getY() < cam.getY() || entity.getY() - entity.getHeight() > cam.getY() + cam.getHeight());
    }

    @Override
    public void mouseClicked(MouseEvent e) {//the first click on the screen (player1 should start)
        System.out.println(e.getX() + " " + e.getY());
        switch (state.peek()) {
            case STARTUP:
                prepareArrow(p1);
                state.push(GameState.P1ACTIVE);
                break;
            default:
                break;
        }

    }

    private void prepareArrow(PlayerEntity player) {
        makeThePreviouseArrowGreen();
        currentArrow = new ArrowEntity(400, gameHeight - 60, player.getAngle(), -45, groundYPosition, targets, true);
        currentArrow.addStateListener(this);
        entities.add(currentArrow);
        player.setArrow(currentArrow);
        cam.tick(player);


    }

    public void makeThePreviouseArrowGreen() {//make the last arrow GREEN and make the previouse one BLACK
        Entity lastEntity, beforeLastEntity;
        if (entities.size() > 2) {
            lastEntity = entities.get(entities.size() - 2);
            beforeLastEntity = entities.get(entities.size() - 3);
            if (lastEntity.isArrow) {
                if (lastEntity.color != lastEntity.RED) {//if this arrow didnt hit the target then change its color
                    lastEntity.color = lastEntity.GREEN;
                }
                if (beforeLastEntity.color != beforeLastEntity.RED)//make the prevoiuse one to its original color if it didnt hit target
                    beforeLastEntity.color = beforeLastEntity.BLACK;

            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (state.peek()) {
            case P1ACTIVE:
            case P2ACTIVE:
                mouseX = e.getX();
                mouseY = e.getY();
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //if the mouse would be under the land dont do anything
        if (e.getY() > windowHeight - 50)//if the mouseReleased under the line of the land don't do anything
        {
            return;
        }
        switch (state.peek()) {
            case P1ACTIVE:
                state.push(GameState.ARROWAWAY);
                p1.shoot();
                break;
            case P2ACTIVE:
                state.push(GameState.ARROWAWAY);
                p2.shoot();
            default:
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.getY() < windowHeight - 50) { // outside control bar
            if (state.peek() == GameState.P1ACTIVE || state.peek() == GameState.P2ACTIVE) {
                PlayerEntity curr = p1;
                int modifier = -1;
                if (state.peek() == GameState.P2ACTIVE) {
                    curr = p2;
                    modifier = 1;
                }
                if (mouseX - e.getX() > 0) {
                    curr.setPower(curr.getPower() - modifier * 2f);
                } else if (mouseX - e.getX() < 0) {
                    curr.setPower(curr.getPower() + modifier * 2f);
                }
                if (mouseY - e.getY() > 0) { // maybe use the difference instead of 1
                    curr.setAngle(curr.getAngle() + 1.5f);
                } else if (mouseY - e.getY() < 0) {
                    curr.setAngle(curr.getAngle() - 1.5f);
                }
                mouseX = e.getX();
                mouseY = e.getY();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void arrowHitTarget(Targetable target) {
        //should be arrow away, so pop
//        if (state.peek() == GameState.ARROWAWAY) {
//            state.pop();
//        }
        PlayerEntity p = (PlayerEntity) target;
        if (p.getHealth() <= 0) {
            state.push(GameState.GAMEOVER);
            System.out.println(p.getName() + " is out!");
        }
//else {
//            // wait a bit
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            if (p == p1) {
//                prepareArrow(p2);
//                state.pop();
//                state.push(GameState.P2ACTIVE);
//            } else {
//                prepareArrow(p1);
//                state.pop();
//                state.push(GameState.P1ACTIVE);
//            }
//        }
    }

    @Override
    public void arrowHitLand(int xPosition) {
        //should be arrow away, so pop
        if (state.peek() == GameState.ARROWAWAY) {
            state.pop();
        }
        // wait a bit
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        switch (state.peek()) {
            case P1ACTIVE:
                prepareArrow(p2);
                state.pop();
                state.push(GameState.P2ACTIVE);
                break;
            case P2ACTIVE:
                prepareArrow(p1);
                state.pop();
                state.push(GameState.P1ACTIVE);
                break;
            default:
                //System.out.println(state.toString());
                break;
        }
    }

    private enum GameState {
        STARTUP, P1ACTIVE, P2ACTIVE, ARROWAWAY, GAMEOVER
    }

}
