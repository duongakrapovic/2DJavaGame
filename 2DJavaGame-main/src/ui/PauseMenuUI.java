package ui;

import main.GamePanel;
import javax.swing.*;
import java.awt.*;

public class PauseMenuUI extends BaseUI {
    public int commandNum = 0; // 0 = Resume, 1 = Home
    private Image iconPlay, iconHome;

    public PauseMenuUI(GamePanel gp) {
        super(gp);

        // Load icons
        iconPlay = new ImageIcon(getClass().getResource("/ui/play.png")).getImage();
        iconHome = new ImageIcon(getClass().getResource("/ui/home.png")).getImage();
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D g2) {
        // ===== Overlay mờ =====
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // ===== Khung chính =====
        int boxW = gp.tileSize * 8;
        int boxH = gp.tileSize * 6;
        int boxX = gp.screenWidth / 2 - boxW / 2;
        int boxY = gp.screenHeight / 2 - boxH / 2;

        g2.setColor(new Color(40, 40, 50, 220)); // nền khung
        g2.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);

        g2.setColor(new Color(200, 200, 200)); // viền sáng
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(boxX, boxY, boxW, boxH, 20, 20);

        // ===== Banner đỏ tiêu đề =====
        String title = "PAUSE";
        g2.setFont(new Font("Georgia", Font.BOLD, 40));
        int titleW = g2.getFontMetrics().stringWidth(title);
        int bannerW = titleW + gp.tileSize * 3;
        int bannerH = gp.tileSize * 2;
        int bannerX = gp.screenWidth / 2 - bannerW / 2;
        int bannerY = boxY - bannerH / 2;

        g2.setColor(new Color(180, 30, 30));
        g2.fillRoundRect(bannerX, bannerY, bannerW, bannerH, 15, 15);

        g2.setColor(Color.WHITE);
        int titleX = gp.screenWidth / 2 - titleW / 2;
        int titleY = bannerY + bannerH / 2 + g2.getFontMetrics().getAscent() / 2 - 5;
        g2.drawString(title, titleX, titleY);

        // ===== 2 nút icon =====
        int btnW = gp.tileSize * 2;
        int btnH = gp.tileSize * 2;
        int gap = gp.tileSize * 2;

        int btnY = boxY + boxH - btnH - gp.tileSize;
        int btnX1 = gp.screenWidth / 2 - btnW - gap / 2;
        int btnX2 = gp.screenWidth / 2 + gap / 2;

        drawButton(g2, iconPlay, btnX1, btnY, btnW, btnH, commandNum == 0);
        drawButton(g2, iconHome, btnX2, btnY, btnW, btnH, commandNum == 1);
    }

    private void drawButton(Graphics2D g2, Image icon, int x, int y, int w, int h, boolean selected) {
        // nền nút
        g2.setColor(new Color(100, 80, 50));
        g2.fillRoundRect(x, y, w, h, 10, 10);

        g2.setColor(new Color(180, 150, 90));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, w, h, 10, 10);

        // highlight
        if (selected) {
            g2.setColor(new Color(255, 215, 0));
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(x - 3, y - 3, w + 6, h + 6, 12, 12);
        }

        // icon
        int iconW = w * 2 / 3;
        int iconH = h * 2 / 3;
        int iconX = x + (w - iconW) / 2;
        int iconY = y + (h - iconH) / 2;
        g2.drawImage(icon, x + w/6, y + h/6, w*2/3, h*2/3, null);
    }
}
