package ui.screens.gameover;

import main.GamePanel;
import main.GameState;
import ui.base.BaseUI;

import java.awt.*;

public class GameOverUI extends BaseUI {
    private int commandNum = 0;

    public GameOverUI(GamePanel gp) {
        super(gp);
    }

    public void moveUp() {
        commandNum = (commandNum == 0) ? 1 : 0;
    }

    public void moveDown() {
        commandNum = (commandNum == 1) ? 0 : 1;
    }

    public void select() {
        if (commandNum == 0) {
            // Restart
            gp.restartGame();
        } else if (commandNum == 1) {
            // Quit
            gp.gsm.setState(GameState.START);
        }
    }

    @Override
    public void update() {
        // No animation for now
    }

    @Override
    public void draw(Graphics2D g2) {
        if (gp.gsm.getState() != GameState.GAME_OVER) return;

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;

        // === GAME OVER title ===
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110F));
        text = "Game Over";

        // Shadow
        g2.setColor(Color.black);
        x = getXforCenteredText(g2, text);
        y = gp.tileSize * 4;
        g2.drawString(text, x, y);

        // Main text
        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);

        // === Restart ===
        g2.setFont(g2.getFont().deriveFont(50F));
        text = "Restart";
        x = getXforCenteredText(g2, text);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - 40, y);
        }
        // === Quit ===
        text = "Quit";
        x = getXforCenteredText(g2, text);
        y += 55;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x - 40, y);
        }
    }
    public int getXforCenteredText(Graphics2D g2, String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

    @Override
    public boolean shouldRenderIn(GameState state) {
        return state == GameState.GAME_OVER;
    }
}
