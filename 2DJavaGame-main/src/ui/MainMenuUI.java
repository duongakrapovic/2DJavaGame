package ui;

import main.GamePanel;
import java.awt.*;

public class MainMenuUI extends BaseUI {
    // Tracks which menu option is currently selected
    // 0 = New Game, 1 = About, 2 = Quit
    public int commandNum = 0;

    // Flag to indicate whether the About screen is showing
    private boolean showAbout = false;

    public MainMenuUI(GamePanel gp) { super(gp); }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D g2) {
        // ===== Background =====
        g2.setColor(new Color(70, 120, 90));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // ===== Game Title =====
        g2.setFont(new Font("Georgia", Font.BOLD, 60));
        g2.setColor(Color.WHITE);
        String title = "BLUE AND JUNGLE";
        int x = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(title) / 2;
        int y = gp.tileSize * 4;
        g2.drawString(title, x, y);

        // If About screen is active → draw About info instead of menu
        if (showAbout) {
            drawAbout(g2);
            return; // Skip drawing main menu
        }

        // ===== Main Menu Options =====
        g2.setFont(new Font("Arial", Font.BOLD, 40));

        // Option 1: New Game
        String text = "NEW GAME";
        x = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(text) / 2;
        y = gp.tileSize * 9;
        g2.drawString(text, x, y);
        if (commandNum == 0) g2.drawString(">", x - gp.tileSize, y); // arrow indicator

        // Option 2: About
        text = "ABOUT";
        x = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(text) / 2;
        y = gp.tileSize * 10;
        g2.drawString(text, x, y);
        if (commandNum == 1) g2.drawString(">", x - gp.tileSize, y);

        // Option 3: Quit
        text = "QUIT";
        x = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(text) / 2;
        y = gp.tileSize * 11; // moved one line lower to avoid overlap
        g2.drawString(text, x, y);
        if (commandNum == 2) g2.drawString(">", x - gp.tileSize, y);
    }

    /**
     * Draws the "About" screen with team info and storyline
     */
    private void drawAbout(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.PLAIN, 28));
        g2.setColor(Color.WHITE);

        int y = gp.tileSize * 6;
        String[] lines = {
                "Team: Vu Hai Duong, Luu Duc Thanh Dat, Do Minh Vu",
                "Story:",
        };

        // Draw each line of the About text centered on screen
        for (String line : lines) {
            int x = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(line) / 2;
            g2.drawString(line, x, y);
            y += gp.tileSize;
        }

        // Draw hint text at the bottom
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        String backText = "Press ENTER to go back";
        int x = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(backText) / 2;
        g2.drawString(backText, x, gp.screenHeight - gp.tileSize * 2);
    }

    /**
     * Toggles About screen ON/OFF
     */
    public void toggleAbout() {
        showAbout = !showAbout;
    }

    /**
     * Returns whether About screen is active
     */
    public boolean isShowingAbout() {
        return showAbout;
    }
}
