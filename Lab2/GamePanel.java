package Lab2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {
    private static final int WIDTH = 360;
    private static final int HEIGHT = 640;
    private static final int FPS = 60;
    private static final int PIPE_SPEED = 3;
    private static final int PIPE_GAP = 150;
    private static final int PIPE_SPAWN_MS = 1300;

    private final BufferedImage backgroundImage;
    private final BufferedImage birdImage;
    private final BufferedImage topPipeImage;
    private final BufferedImage bottomPipeImage;
    private final Font scoreFont = new Font("Arial", Font.BOLD, 28);
    private final Font hintFont = new Font("Arial", Font.PLAIN, 20);
    private final Random random = new Random();
    private final List<Pipe> pipes = new ArrayList<>();

    private Bird bird;
    private boolean gameOver;
    private int score;
    private long lastPipeSpawnTime;
    private Timer gameTimer;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);

        backgroundImage = loadImage("flappybirdbg.png");
        birdImage = loadImage("flappybird.png");
        topPipeImage = loadImage("toppipe.png");
        bottomPipeImage = loadImage("bottompipe.png");

        initInput();
        resetGame();
    }

    public void startGameLoop() {
        int delay = 1000 / FPS;
        gameTimer = new Timer(delay, e -> {
            updateGame();
            repaint();
        });
        gameTimer.start();
    }

    private void initInput() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (!gameOver && (keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_ENTER)) {
                    bird.flap();
                } else if (gameOver && keyCode == KeyEvent.VK_R) {
                    resetGame();
                }
            }
        });
    }

    private void resetGame() {
        bird = new Bird(80, HEIGHT / 2, birdImage);
        pipes.clear();
        score = 0;
        gameOver = false;
        lastPipeSpawnTime = System.currentTimeMillis();
        requestFocusInWindow();
    }

    private void updateGame() {
        if (gameOver) {
            return;
        }

        long now = System.currentTimeMillis();
        if (now - lastPipeSpawnTime >= PIPE_SPAWN_MS) {
            pipes.add(createPipe());
            lastPipeSpawnTime = now;
        }

        bird.update();

        Iterator<Pipe> iterator = pipes.iterator();
        while (iterator.hasNext()) {
            Pipe pipe = iterator.next();
            pipe.update(PIPE_SPEED);

            if (!pipe.isPassed() && pipe.getX() < bird.getX()) {
                pipe.setPassed(true);
                score++;
            }

            if (pipe.isOffScreen()) {
                iterator.remove();
            }
        }

        for (Pipe pipe : pipes) {
            if (bird.getBounds().intersects(pipe.getTopBounds())
                    || bird.getBounds().intersects(pipe.getBottomBounds())) {
                gameOver = true;
                return;
            }
        }

        if (bird.getTop() <= 0 || bird.getBottom() >= HEIGHT) {
            gameOver = true;
        }
    }

    private Pipe createPipe() {
        int minGapY = 170;
        int maxGapY = HEIGHT - 170;
        int gapY = random.nextInt(maxGapY - minGapY + 1) + minGapY;
        return new Pipe(WIDTH + 40, gapY, PIPE_GAP, topPipeImage, bottomPipeImage);
    }

    private BufferedImage loadImage(String fileName) {
        String[] candidatePaths = {
            "Lab2/data/" + fileName,
            "data/" + fileName,
            "Lab2/" + fileName,
            fileName
        };

        for (String path : candidatePaths) {
            File file = new File(path);
            if (file.exists()) {
                try {
                    return ImageIO.read(file);
                } catch (IOException ignored) {
                    // Try next candidate path.
                }
            }
        }

        throw new IllegalStateException("Khong tim thay file anh: " + fileName);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, null);

        for (Pipe pipe : pipes) {
            pipe.draw(g2d);
        }

        bird.draw(g2d);

        g2d.setFont(scoreFont);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Score: " + score, 12, 35);

        if (gameOver) {
            g2d.setColor(new Color(255, 60, 60));
            g2d.drawString("GAME OVER", WIDTH / 2 - 95, HEIGHT / 2 - 20);

            g2d.setFont(hintFont);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Nhan R de choi lai", WIDTH / 2 - 75, HEIGHT / 2 + 15);
        }
    }
}
