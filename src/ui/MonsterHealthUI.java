package ui;

import main.GamePanel;
import monster_data.Monster;
import java.awt.*;
import entity.Entity;


public class MonsterHealthUI extends BaseUI {

    public MonsterHealthUI(GamePanel gp) {
        super(gp);
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D g2) {

        for (Entity e : gp.em.getMonsters()) {
            if (!(e instanceof Monster monster)) continue;
            if (monster.isDead()) continue;

            int max = monster.getMaxHP();
            int cur = monster.getHP();

            // Tính vị trí hiển thị của quái trên màn hình
            int screenX = monster.worldX - gp.em.getPlayer().worldX + gp.em.getPlayer().screenX;
            int screenY = monster.worldY - gp.em.getPlayer().worldY + gp.em.getPlayer().screenY;

            // Đặt thanh máu ngay trên đầu quái (tùy chỉnh offset)
            int centerX = screenX + gp.tileSize / 2;
            int barWidth = gp.tileSize;
            int barHeight = gp.tileSize / 8;
            int barX = centerX - barWidth / 2;

            int offsetY = gp.tileSize / 2 + 6; // cách đầu quái vài pixel
            int barY = screenY - gp.tileSize - offsetY;

            // Vẽ thanh máu
            g2.setColor(Color.darkGray);
            g2.fillRect(barX, barY, barWidth, barHeight);

            int healthWidth = (int) ((cur / (double) max) * barWidth);
            g2.setColor(new Color(220, 0, 0)); // đỏ đậm cho rõ
            g2.fillRect(barX, barY, healthWidth, barHeight);

            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(1));
            g2.drawRect(barX, barY, barWidth, barHeight);
        }
    }
}
