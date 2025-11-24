package ui.health;

import main.GamePanel;
import main.GameState;
import monster_data.Monster;
import java.awt.*;
import entity.Entity;
import ui.base.BaseUI;

public class MonsterHealthUI extends BaseUI {

    public MonsterHealthUI(GamePanel gp) {
        super(gp);
    }
    @Override
    public void update() {}
    @Override
    public void draw(Graphics2D g2) {
        // Duyệt toàn bộ quái hiện tại
        for (Entity e : gp.em.getMonsters()) {
            if (!(e instanceof Monster monster)) continue;
            if (monster.isDead()) continue;

            // Lấy chỉ số máu
            int max = monster.getMaxHP();
            int cur = monster.getHP();

            // Tính vị trí của quái trên màn hình (từ world → screen)
            int screenX = monster.worldX - gp.em.getPlayer().worldX + gp.em.getPlayer().screenX;
            int screenY = monster.worldY - gp.em.getPlayer().worldY + gp.em.getPlayer().screenY;

            // Tính vị trí thanh máu nằm ngay trên đầu quái
            int centerX = screenX + gp.tileSize / 2;
            int barWidth = gp.tileSize;
            int barHeight = gp.tileSize / 8;
            int barX = centerX - barWidth / 2;

            int offsetY = gp.tileSize / 2 + 6; // khoảng cách phía trên đầu quái
            int barY = screenY - gp.tileSize - offsetY;

            // Nền thanh máu
            g2.setColor(Color.darkGray);
            g2.fillRect(barX, barY, barWidth, barHeight);

            // Phần máu còn lại
            int healthWidth = (int) ((cur / (double) max) * barWidth);
            g2.setColor(new Color(220, 0, 0)); // đỏ đậm cho dễ nhìn
            g2.fillRect(barX, barY, healthWidth, barHeight);

            // Viền thanh máu
            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(1));
            g2.drawRect(barX, barY, barWidth, barHeight);
        }
    }

    @Override
    public boolean shouldRenderIn(GameState state) {
        // Chỉ hiển thị trong state PLAY
        return state == GameState.PLAY;
    }

    @Override
    public boolean shouldUpdate() {
        // Không cần update riêng, lấy dữ liệu trực tiếp theo frame
        return false;
    }
}
