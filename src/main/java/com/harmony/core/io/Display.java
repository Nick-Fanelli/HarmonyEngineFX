/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.core.io;

import com.harmony.core.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Display {

    private static JFrame frame;
    private static BufferedImage image;
    private static Canvas canvas;
    private static BufferStrategy bufferStrategy;
    private static Graphics2D g;

    private static String title;
    private static Dimension dimension;

    public static void createDisplay(String title, Dimension dimension, boolean isResizable) {
        System.out.println("Harmony@" + title + " -> Creating Display...");

        Display.title = title;
        Display.dimension = dimension;

        image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
        canvas = new Canvas();
        canvas.setPreferredSize(dimension);

        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(isResizable);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        g = (Graphics2D) bufferStrategy.getDrawGraphics();

        frame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                Display.handleClose();
            }
        });

        frame.setVisible(true);

        // TODO: Handle potential frame resize

        System.out.println("Harmony@" + title + " -> Display Created...");
    }

    private static void handleClose() {
        Project.staticContext.stop();
    }

    public static void updateDisplay() {
        try {
            g.drawImage(image, 0, 0, dimension.width, dimension.height, null);
            bufferStrategy.show();
        } catch (Exception ignored) {}
    }

    public static void closeDisplay() {
        System.out.println("Harmony@" + title + " -> Closing Display...");
        frame.setVisible(false);
        frame.dispose();
        System.out.println("Harmony@" + title + " -> Display Closed");
    }

}
