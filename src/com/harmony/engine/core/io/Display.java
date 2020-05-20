package com.harmony.engine.core.io;

import com.harmony.engine.core.parameters.DisplayParameters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * The type Display is used to setup the display the engine will draw to.
 */
public class Display {

    private static Color backgroundColor;

    private DisplayParameters parameters;

    private String title;

    /**
     * The constant width.
     */
    public static int width,
    /**
     * The Height.
     */
    height;

    private JFrame frame;
    private Canvas canvas;
    private BufferedImage image;
    private BufferStrategy bufferStrategy;
    private Graphics g;
    private Graphics2D g2d;

    /**
     * Instantiates a new Display.
     *
     * @param parameters the parameters
     */
    public Display(DisplayParameters parameters) {
        this.parameters = parameters;

        this.title = parameters.title;
        Display.backgroundColor = parameters.backgroundColor;

        Display.width = parameters.startingWidth;
        Display.height = parameters.startingHeight;

        image = new BufferedImage(Display.width, Display.height, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D) image.getGraphics();

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(Display.width, Display.height));

        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setFocusable(true);
        frame.setVisible(true);

        frame.requestFocus();
        canvas.requestFocus();

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        g = bufferStrategy.getDrawGraphics();

        handleFrameResize();
    }

    private void handleFrameResize() {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if(frame.getWidth() <= 0 || frame.getHeight() <= 0) return;

                canvas.createBufferStrategy(2);
                bufferStrategy = canvas.getBufferStrategy();
                g = bufferStrategy.getDrawGraphics();
            }
        });
    }

    /**
     * Not for client use!!!
     */
    public void update() {
        g.drawImage(image, 0, 0, frame.getWidth(), frame.getHeight(), null);
        bufferStrategy.show();
    }

    /**
     * Gets graphics 2D.
     *
     * @return the graphics 2D
     */
    public Graphics2D getGraphics2D() { return g2d; }

    /**
     * Sets background color of the display.
     *
     * @param color the color
     */
    public static void setBackgroundColor(Color color) { Display.backgroundColor = color; }

    /**
     * Gets the background color of the display.
     *
     * @return the background color
     */
    public static Color getBackgroundColor() { return backgroundColor; }

    /**
     * Gets canvas.
     *
     * @return the canvas
     */
    public Canvas getCanvas() { return canvas; }
}
