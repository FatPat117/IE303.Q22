package Lab2;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Bird {
    private static final double GRAVITY = 0.35;
    private static final double FLAP_STRENGTH = -7.0;

    private final BufferedImage image;
    private final int x;
    private double y;
    private double velocityY;

    public Bird(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.velocityY = 0.0;
    }

    public void flap() {
        velocityY = FLAP_STRENGTH;
    }

    public void update() {
        velocityY += GRAVITY;
        y += velocityY;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(image, x, (int) y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, (int) y, image.getWidth(), image.getHeight());
    }

    public int getX() {
        return x;
    }

    public int getTop() {
        return (int) y;
    }

    public int getBottom() {
        return (int) y + image.getHeight();
    }
}
