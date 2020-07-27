/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.core;

import com.harmony.core.io.Display;

import java.awt.*;

public class Project implements Runnable {

    public static Project staticContext;

    private final String title;
    private final double updateCap;

    private Thread thread;
    private boolean isRunning = false;

    public Project(String title, int targetFps) {
        Project.staticContext = this;
        System.out.println("Harmony@" + title + " -> Created");

        this.title = title;
        this.updateCap = 1.0 / targetFps;
    }

    public synchronized void start() {
        System.out.println("Harmony@" + title + " -> Starting...");
        System.out.println("Harmony@" + title + " -> Starting New Thread...");
        thread = new Thread(this, "HarmonyProject:" + title);
        thread.start();
    }

    private void initialize() {
        System.out.println("Harmony@" + title + " -> Initializing...");

        // TODO: Update with project settings
        Display.createDisplay(title, new Dimension(1280, 720), false);

        isRunning = true;
        System.out.println("Harmony@" + title + " -> Initialized");
    }

    @Override @Deprecated
    public void run() {
        System.out.println("Harmony@" + title + " -> New Thread Started");
        initialize();

        boolean render;

        double firstTime;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime;
        double deltaTime = 0;

        while(isRunning) {
            render = false;

            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            deltaTime += passedTime;

            while(deltaTime >= updateCap) {
                deltaTime -= updateCap;
                render = true;
                this.update();
            }

            if(render) {
                this.render();
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private synchronized void update() {
    }

    private synchronized void render() {
        Display.updateDisplay(); // Perform Last
    }

    public synchronized void stop() {
        System.out.println("Harmony@" + title + " -> Stopping...");
        isRunning = false;
        Display.closeDisplay();
        System.out.println("Harmony@" + title + " -> Stopped");
    }

}
