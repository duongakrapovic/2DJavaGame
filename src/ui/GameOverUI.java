package ui;

import main.GamePanel;
import main.GameState;
import java.awt.*;

/** Simple black screen with big centered "GAME OVER" text. */
public class GameOverUI extends BaseUI {

    public GameOverUI(GamePanel gp) {
        super(gp);
    }

    @Override
    public void update() {
        // No animation for now
    }

    @Override
    public void draw(Graphics2D g2) {
        if (gp.gsm.getState() != GameState.GAME_OVER) return;

        // Fill black background
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Big bold font scaled from tile size
        int fontSize = gp.tileSize * 3; // adjust if you want bigger/smaller
        Font font = new Font("Arial", Font.BOLD, fontSize);
        g2.setFont(font);

        String text = "GAME OVER";
        // White text with a soft shadow for readability
        FontMetrics fm = g2.getFontMetrics();
        int textW = fm.stringWidth(text);
        int textH = fm.getAscent();

        int x = (gp.screenWidth  - textW) / 2;
        int y = (gp.screenHeight + textH) / 2;

        // Shadow
        g2.setColor(new Color(0, 0, 0, 180));
        g2.drawString(text, x + 4, y + 4);

        // Main text
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
    }
    @Override
    public boolean shouldRenderIn(GameState state) {
        return state == GameState.GAME_OVER;
    }
}
