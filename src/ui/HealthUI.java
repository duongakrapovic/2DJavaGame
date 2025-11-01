package ui;

import main.GamePanel;
import main.GameState;
import java.awt.*;

public class HealthUI extends BaseUI {
    public HealthUI(GamePanel gp){ super(gp); }

    @Override
    public void update(){}

    @Override
    public void draw(Graphics2D g2){
        int max = gp.em.getPlayer().getMaxHP();
        int cur = gp.em.getPlayer().getHP();

        int barWidth = gp.tileSize * 4;
        int barHeight = gp.tileSize / 2;
        int x = gp.tileSize / 2, y = gp.tileSize / 2;

        g2.setColor(Color.gray);
        g2.fillRect(x, y, barWidth, barHeight);

        int healthWidth = (int)((cur/(double)max) * barWidth);
        g2.setColor(Color.red);
        g2.fillRect(x, y, healthWidth, barHeight);

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, y, barWidth, barHeight);
    }
    @Override
    public boolean shouldRenderIn(GameState state) {
        return state == GameState.PLAY;
    }
    @Override
    public boolean shouldUpdate() {
        return false;
    }
}
