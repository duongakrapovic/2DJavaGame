package ui.health;   // hoặc ui.effects tuỳ bạn

import main.GamePanel;
import main.GameState;
import player_manager.Player;
import ui.base.BaseUI;

import java.awt.*;

public class PlayerStatusUI extends BaseUI {

    public PlayerStatusUI(GamePanel gp) {
        super(gp);
    }

    @Override
    public void update() {
        // hiện tại chưa cần update gì, nhưng để sẵn
    }

    @Override
    public void draw(Graphics2D g2) {
        if (gp == null || gp.em == null) return;
        Player p = gp.em.getPlayer();
        if (p == null) return;

        int level     = p.getLevel();
        int exp       = p.getExp();
        int expToNext = p.getExpToNext();

        // ==== Vị trí panel (NGAY DƯỚI thanh máu) ====
        int panelX = gp.tileSize / 2;
        int panelY = gp.tileSize / 2 + gp.tileSize + 4; // thấp hơn thanh máu một chút
        int panelW = gp.tileSize * 5;
        int panelH = gp.tileSize;

        Color oldColor = g2.getColor();
        Font oldFont   = g2.getFont();

        // Nền mờ
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 10, 10);

        // Viền
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 10, 10);

        // ==== Text bên trong panel ====
        int textPaddingX = 10;
        int textPaddingY = 20;

        int textX = panelX + textPaddingX;
        int textY = panelY + textPaddingY;

        // Level
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Lv " + level, textX, textY);

        // EXP text nhỏ hơn, ngay dưới
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("EXP " + exp + "/" + expToNext, textX, textY + 14);

        // ==== Thanh EXP nhỏ nằm cuối panel ====
        int barMarginX = 10;
        int barMarginY = 6;

        int barX = panelX + barMarginX;
        int barW = panelW - barMarginX * 2;
        int barH = 5;
        int barY = panelY + panelH - barH - barMarginY;

        double ratio = expToNext > 0 ? (double) exp / expToNext : 0;
        ratio = Math.max(0, Math.min(1, ratio));

        // nền thanh
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(barX, barY, barW, barH);

        // phần đã có exp
        g2.setColor(new Color(0, 200, 255));
        g2.fillRect(barX, barY, (int) (barW * ratio), barH);

        // viền thanh
        g2.setColor(Color.WHITE);
        g2.drawRect(barX, barY, barW, barH);

        g2.setColor(oldColor);
        g2.setFont(oldFont);
    }


    @Override
    public boolean shouldRenderIn(GameState state) {
        // Hiện level trong lúc chơi và lúc pause (nếu muốn)
        return state == GameState.PLAY || state == GameState.PAUSE;
    }

    @Override
    public boolean shouldUpdate() {
        return true;
    }

    @Override
    public boolean shouldDraw() {
        return true;
    }
}
