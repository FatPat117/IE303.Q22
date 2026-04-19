package Lab3;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public final class ProductCatalogApp extends JFrame {

    private static final Color BG_TOP = new Color(0xF5, 0xF7, 0xFA);
    private static final Color BG_BOTTOM = new Color(0xE8, 0xEC, 0xF1);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color ACCENT = new Color(0x1E, 0x88, 0xE5);
    private static final Color ACCENT_DARK = new Color(0x15, 0x65, 0xC0);
    private static final Color TEXT_MAIN = new Color(0x1A, 0x1D, 0x21);
    private static final Color TEXT_MUTED = new Color(0x6B, 0x72, 0x7D);
    private static final Color PRICE_COLOR = new Color(0x0D, 0x47, 0xA1);
    private static final int CARD_RADIUS = 14;

    private final List<Product> products;
    private final FadingImagePanel mainImagePanel;
    private final JLabel mainTitle;
    private final JLabel mainPrice;
    private final JLabel mainBrand;
    private final JLabel mainSubtext;
    private final List<CatalogProductCard> cardPanels = new ArrayList<>();

    private int selectedIndex = 0;

    public ProductCatalogApp() {
        super("Sneaker store — Product catalog");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new GradientBackgroundPanel();
        root.setLayout(new BorderLayout());

        products = Collections.unmodifiableList(Arrays.asList(
                new Product("4DFWD PULSE SHOES", "Adidas", 160,
                        "This product is excluded from all promotional discounts and offers.", "img1.png"),
                new Product("FORUM MID SHOES", "Adidas", 100,
                        "This product is excluded from all promotional discounts and offers.", "img2.png"),
                new Product("SUPERNOVA SHOES", "Adidas", 150, "NMD City Stock 2", "img3.png"),
                new Product("NMD CITY STOCK 2", "Adidas", 160, "NMD City Stock 2", "img4.png"),
                new Product("NMD CITY STOCK 2", "Adidas", 120, "NMD City Stock 2", "img5.png"),
                new Product("4DFWD PULSE SHOES", "Adidas", 160,
                        "This product is excluded from all promotional discounts and offers.", "img6.png"),
                new Product("4DFWD PULSE SHOES", "Adidas", 160,
                        "This product is excluded from all promotional discounts and offers.", "img1.png"),
                new Product("FORUM MID SHOES", "Adidas", 100,
                        "This product is excluded from all promotional discounts and offers.", "img2.png")));

        mainImagePanel = new FadingImagePanel();
        mainImagePanel.setPreferredSize(new Dimension(440, 340));
        mainImagePanel.setOpaque(false);

        Font ui = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
        mainTitle = new JLabel();
        mainTitle.setFont(ui.deriveFont(Font.BOLD, 26f));
        mainTitle.setForeground(TEXT_MAIN);
        mainPrice = new JLabel();
        mainPrice.setFont(ui.deriveFont(Font.BOLD, 22f));
        mainPrice.setForeground(PRICE_COLOR);
        mainBrand = new JLabel();
        mainBrand.setFont(ui.deriveFont(Font.BOLD, 13f));
        mainBrand.setForeground(ACCENT_DARK);
        mainSubtext = new JLabel("<html><body style='width:300px'></body></html>");
        mainSubtext.setFont(ui.deriveFont(13f));
        mainSubtext.setForeground(TEXT_MUTED);
        mainSubtext.setVerticalAlignment(SwingConstants.TOP);

        JPanel heroShell = new JPanel(new BorderLayout());
        heroShell.setOpaque(false);
        heroShell.setBorder(new EmptyBorder(0, 0, 4, 0));
        RoundedImageStage imageStage = new RoundedImageStage(mainImagePanel);
        imageStage.setPreferredSize(new Dimension(452, 356));
        heroShell.add(imageStage, BorderLayout.CENTER);

        JLabel featured = new JLabel("FEATURED");
        featured.setFont(ui.deriveFont(Font.BOLD, 11f));
        featured.setForeground(ACCENT);
        featured.setBorder(new EmptyBorder(0, 4, 8, 0));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.setBorder(new EmptyBorder(20, 24, 20, 12));
        left.add(featured);
        left.add(heroShell);
        left.add(Box.createVerticalStrut(16));
        left.add(mainTitle);
        left.add(Box.createVerticalStrut(8));
        left.add(mainPrice);
        left.add(Box.createVerticalStrut(6));
        left.add(mainBrand);
        left.add(Box.createVerticalStrut(10));
        left.add(mainSubtext);

        JPanel grid = new JPanel(new GridLayout(2, 4, 16, 16));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(8, 4, 16, 20));

        for (int i = 0; i < products.size(); i++) {
            final int idx = i;
            CatalogProductCard card = buildCard(products.get(i), idx == selectedIndex);
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectProduct(idx);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    card.setHover(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    card.setHover(false);
                }
            });
            cardPanels.add(card);
            grid.add(card);
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        JPanel rightWrap = new JPanel(new BorderLayout());
        rightWrap.setOpaque(false);
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(12, 8, 4, 20));
        JLabel dots = new JLabel(
                "<html><span style='color:#FF5F57'>●</span> <span style='color:#FEBC2E'>●</span> <span style='color:#28C840'>●</span></html>");
        topBar.add(dots, BorderLayout.EAST);
        JLabel browse = new JLabel("Browse");
        browse.setFont(ui.deriveFont(Font.BOLD, 13f));
        browse.setForeground(TEXT_MUTED);
        topBar.add(browse, BorderLayout.WEST);
        rightWrap.add(topBar, BorderLayout.NORTH);
        rightWrap.add(scroll, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, rightWrap);
        split.setResizeWeight(0.42);
        split.setDividerSize(1);
        split.setContinuousLayout(true);
        split.setBorder(BorderFactory.createEmptyBorder());
        split.setOpaque(false);
        split.setBackground(new Color(0, 0, 0, 0));
        root.add(split, BorderLayout.CENTER);
        setContentPane(root);

        pack();
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);

        applyProductToMain(0, false);
    }

    private CatalogProductCard buildCard(Product p, boolean selected) {
        CatalogProductCard card = new CatalogProductCard(selected);
        card.setLayout(new BorderLayout(4, 2));

        Font ui = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

        JLabel t = new JLabel(p.getShortTitle(18));
        t.setFont(ui.deriveFont(Font.BOLD, 12.5f));
        t.setForeground(TEXT_MAIN);
        t.setBorder(new EmptyBorder(10, 12, 0, 12));

        String sub = p.getSubtext();
        if (sub.length() > 28) {
            sub = sub.substring(0, 27) + "…";
        }
        JLabel s = new JLabel(sub);
        s.setFont(ui.deriveFont(10.5f));
        s.setForeground(TEXT_MUTED);
        s.setBorder(new EmptyBorder(2, 12, 4, 12));

        JLabel img = new JLabel();
        img.setHorizontalAlignment(SwingConstants.CENTER);
        BufferedImage bi = loadImage(p.getImageFileName());
        if (bi != null) {
            img.setIcon(new ImageIcon(scale(bi, 118, 96)));
        }
        img.setBorder(new EmptyBorder(2, 10, 2, 10));

        JLabel brand = new JLabel(p.getBrand());
        brand.setFont(ui.deriveFont(Font.BOLD, 11f));
        brand.setForeground(ACCENT_DARK);
        brand.setBorder(new EmptyBorder(0, 12, 2, 12));

        JLabel price = new JLabel(String.format("$%.2f", p.getPriceUsd()));
        price.setFont(ui.deriveFont(Font.BOLD, 13f));
        price.setForeground(PRICE_COLOR);
        price.setBorder(new EmptyBorder(0, 12, 12, 12));

        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        north.setOpaque(false);
        north.add(t);
        north.add(s);

        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.setOpaque(false);
        south.add(brand);
        south.add(price);

        card.add(north, BorderLayout.NORTH);
        card.add(img, BorderLayout.CENTER);
        card.add(south, BorderLayout.SOUTH);
        return card;
    }

    private void selectProduct(int index) {
        if (index == selectedIndex) {
            return;
        }
        selectedIndex = index;
        for (int i = 0; i < cardPanels.size(); i++) {
            cardPanels.get(i).setCardSelected(i == selectedIndex);
        }
        applyProductToMain(index, true);
    }

    private void applyProductToMain(int index, boolean animate) {
        Product p = products.get(index);
        mainTitle.setText(p.getTitle());
        mainPrice.setText(String.format("$%.2f", p.getPriceUsd()));
        mainBrand.setText(p.getBrand());
        mainSubtext.setText(
                "<html><body style='width:320px;line-height:1.45'>" + escapeHtml(p.getSubtext()) + "</body></html>");

        BufferedImage bi = loadImage(p.getImageFileName());
        if (bi != null) {
            BufferedImage scaled = scale(bi, 400, 300);
            if (animate) {
                mainImagePanel.crossfadeTo(scaled);
            } else {
                mainImagePanel.setImageImmediate(scaled);
            }
        }
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static BufferedImage scale(BufferedImage src, int maxW, int maxH) {
        double sx = maxW / (double) src.getWidth();
        double sy = maxH / (double) src.getHeight();
        double scale = Math.min(sx, sy);
        int w = (int) Math.round(src.getWidth() * scale);
        int h = (int) Math.round(src.getHeight() * scale);
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = out.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();
        return out;
    }

    private BufferedImage loadImage(String fileName) {
        File f = resolveDataFile(fileName);
        if (f == null || !f.isFile()) {
            return null;
        }
        try {
            return javax.imageio.ImageIO.read(f);
        } catch (Exception e) {
            return null;
        }
    }

    /** Nền cửa sổ dạng gradient nhẹ. */
    private static final class GradientBackgroundPanel extends JPanel {
        GradientBackgroundPanel() {
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            GradientPaint gp = new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOTTOM);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    /** Khung bo góc phía sau ảnh sản phẩm chính. */
    private static final class RoundedImageStage extends JPanel {
        RoundedImageStage(JComponent inner) {
            super(new BorderLayout());
            setOpaque(false);
            inner.setOpaque(false);
            add(inner, BorderLayout.CENTER);
            setBorder(new EmptyBorder(10, 10, 10, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            g2.setColor(new Color(0, 0, 0, 16));
            g2.fill(new RoundRectangle2D.Float(2f, 4f, w - 4f, h - 4f, 22, 22));
            g2.setColor(new Color(0xFA, 0xFB, 0xFC));
            g2.fill(new RoundRectangle2D.Float(0, 0, w - 2f, h - 2f, 22, 22));
            g2.setColor(new Color(0xD8, 0xDE, 0xE6));
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 2.5f, h - 2.5f, 22, 22));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Thẻ sản phẩm: bo góc, đổ bóng nhẹ, viền khi chọn / hover. */
    private static final class CatalogProductCard extends JPanel {
        private boolean selected;
        private boolean hover;

        CatalogProductCard(boolean selected) {
            this.selected = selected;
            setOpaque(false);
        }

        void setCardSelected(boolean s) {
            if (selected != s) {
                selected = s;
                repaint();
            }
        }

        void setHover(boolean h) {
            if (hover != h) {
                hover = h;
                repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            float inset = 2f;
            float rw = w - inset * 2;
            float rh = h - inset * 2;
            float x = inset;
            float y = inset;

            g2.setColor(new Color(0, 0, 0, 20));
            g2.fill(new RoundRectangle2D.Float(x + 2, y + 3, rw - 2, rh - 2, CARD_RADIUS, CARD_RADIUS));
            g2.setColor(new Color(0, 0, 0, 12));
            g2.fill(new RoundRectangle2D.Float(x + 1, y + 2, rw - 1, rh - 1, CARD_RADIUS, CARD_RADIUS));

            g2.setColor(CARD_BG);
            g2.fill(new RoundRectangle2D.Float(x, y, rw, rh, CARD_RADIUS, CARD_RADIUS));

            Color border = selected ? ACCENT : (hover ? new Color(0xB0, 0xB8, 0xC4) : new Color(0xD8, 0xDE, 0xE6));
            float stroke = selected ? 2.2f : 1f;
            g2.setStroke(new BasicStroke(stroke));
            g2.setColor(border);
            g2.draw(new RoundRectangle2D.Float(x + 0.5f, y + 0.5f, rw - 1f, rh - 1f, CARD_RADIUS, CARD_RADIUS));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static File resolveDataFile(String name) {
        String cwd = System.getProperty("user.dir");
        java.io.File[] candidates = new java.io.File[] {
                Paths.get(cwd, "Lab3", "data", name).toFile(),
                Paths.get(cwd, "data", name).toFile(),
                Paths.get(cwd, name).toFile()
        };
        for (File f : candidates) {
            if (f.isFile()) {
                return f;
            }
        }
        return null;
    }

    /** Ảnh lớn bên trái với hiệu ứng crossfade. */
    private static final class FadingImagePanel extends JPanel {
        private BufferedImage fromImg;
        private BufferedImage toImg;
        private float blend; // 0 = chỉ from, 1 = chỉ to
        private Timer fadeTimer;

        FadingImagePanel() {
        }

        void setImageImmediate(BufferedImage img) {
            if (fadeTimer != null) {
                fadeTimer.stop();
                fadeTimer = null;
            }
            this.fromImg = img;
            this.toImg = null;
            this.blend = 1f;
            repaint();
        }

        void crossfadeTo(BufferedImage newImg) {
            if (fadeTimer != null) {
                fadeTimer.stop();
                fadeTimer = null;
            }
            if (fromImg == null) {
                setImageImmediate(newImg);
                return;
            }
            this.toImg = newImg;
            this.blend = 0f;
            fadeTimer = new Timer(16, null);
            fadeTimer.addActionListener(e -> {
                blend += 0.08f;
                if (blend >= 1f) {
                    blend = 1f;
                    fromImg = toImg;
                    toImg = null;
                    fadeTimer.stop();
                    fadeTimer = null;
                }
                repaint();
            });
            fadeTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (fromImg == null && toImg == null) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            int cw = getWidth();
            int ch = getHeight();

            if (toImg == null) {
                drawCentered(g2, fromImg, cw, ch, 1f);
            } else {
                drawCentered(g2, fromImg, cw, ch, 1f - blend);
                drawCentered(g2, toImg, cw, ch, blend);
            }
            g2.dispose();
        }

        private static void drawCentered(Graphics2D g2, BufferedImage img, int cw, int ch, float alpha) {
            if (img == null || alpha <= 0.001f) {
                return;
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            int x = (cw - img.getWidth()) / 2;
            int y = (ch - img.getHeight()) / 2;
            g2.drawImage(img, x, y, null);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // keep default
        }
        SwingUtilities.invokeLater(() -> new ProductCatalogApp().setVisible(true));
    }
}
