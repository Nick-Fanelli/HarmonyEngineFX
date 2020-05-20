package com.harmony.engine.core;

import com.harmony.engine.core.io.Display;
import com.harmony.engine.core.io.Input;
import com.harmony.engine.core.parameters.Parameters;
import com.harmony.engine.core.state.StateManager;

import java.awt.*;

/**
 * The type Harmony is used to create the Project.
 * To get stated simply call the start method.
 */
public class Harmony implements Runnable {

    private Thread coreThread;
    private Parameters parameters;
    private Display display;
    private StateManager stateManager;
    private Input input;

    private Graphics2D g;

    private final double updateCap;

    private int currentFps = 0;

    private boolean isRunning = false;

    /**
     * Instantiates a new Harmony Type which is used to control the entire engine.
     *
     * @param parameters the parameters
     */
    public Harmony(Parameters parameters) {
        System.out.println("Harmony -> Created a new Harmony instance at: " + this.toString().substring(31));
        this.parameters = parameters;
        this.updateCap = 1.0 / parameters.targetFps;
    }

    private void initialize() {
        isRunning = true;

        display = new Display(parameters.displayParameters);
        g = display.getGraphics2D();

        input = new Input(this);

        stateManager = new StateManager();
    }

    /**
     * The run method is used to start the engine and create the window with the specified parameters.
     */
    @Override
    public void run() {

        if(coreThread == null) {
            coreThread = new Thread(this, "Harmony:CoreThread");
            coreThread.start();
            return;
        }

        System.out.println("Harmony -> Started the Core Thread");

        initialize();

        boolean shouldRender;

        double firstTime;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime;
        double deltaTime = 0;

        double frameTime = 0;
        int frames = 0;
        int fps = 0;

        while(isRunning) {

            shouldRender = false;

            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            deltaTime += passedTime;
            frameTime += passedTime;

            while(deltaTime >= updateCap) {
                deltaTime -= updateCap;
                shouldRender = true;
                update();

                if(frameTime >= 1) {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                    this.currentFps = fps;
                }
            }

            if(shouldRender) {
                render();

                frames++;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        cleanUp();
    }

    private synchronized void update() {
        stateManager.update();
        input.update();
    }

    private synchronized void render() {
        // Clear the screen
        g.setColor(Display.getBackgroundColor());
        g.fillRect(0, 0, Display.width, Display.height);

        stateManager.render(g);

        display.update();
    }

    /**
     * The stop method is used to stop the engine and will automatically close the game loop and threads.
     */
    public synchronized void stop() { if(isRunning) isRunning = false; }

    private void cleanUp() {

    }


    /**
     * Is running boolean will return if the engine is running or not.
     *
     * @return the status of the engine
     */
    public boolean isRunning() { return isRunning; }

    /**
     * Gets the current fps.
     *
     * @return the current fps
     */
    public int getCurrentFps() { return currentFps; }

    /**
     * Gets display.
     *
     * @return the display
     */
    public Display getDisplay() { return display; }
}
