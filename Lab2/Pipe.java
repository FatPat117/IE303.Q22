package Lab2;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Pipe {
    private final BufferedImage topImage;
    private final BufferedImage bottomImage;
    private final int gapY;
    private final int gapSize;
    private int x;
    private boolean passed;

    public Pipe(int x, int gapY, int gapSize, BufferedImage topImage, BufferedImage bottomImage) {
        this.x = x;
        this.gapY = gapY;
        this.gapSize = gapSize;
        this.topImage = topImage;
        this.bottomImage = bottomImage;
        this.passed = false;
    }

    public void update(int speed) {
        x -= speed;
    }

    public void draw(Graphics2D g2d) {
        int topY = gapY - gapSize / 2 - topImage.getHeight();
        int bottomY = gapY + gapSize / 2;
        g2d.drawImage(topImage, x, topY, null);
        g2d.drawImage(bottomImage, x, bottomY, null);
    }

    public Rectangle getTopBounds() {
        int topY = gapY - gapSize / 2 - topImage.getHeight();
        return new Rectangle(x, topY, topImage.getWidth(), topImage.getHeight());
    }

    public Rectangle getBottomBounds() {
        int bottomY = gapY + gapSize / 2;
        return new Rectangle(x, bottomY, bottomImage.getWidth(), bottomImage.getHeight());
    }

    public boolean isOffScreen() {
        return x + topImage.getWidth() < 0;
    }

    public int getX() {
        return x;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
