package ui.base;

import main.GamePanel;
import main.GameState;
import java.awt.*;


public abstract class BaseUI {
    protected final GamePanel gp;

    public BaseUI(GamePanel gp) {
        this.gp = gp;
    }

    public abstract void update();


    public abstract void draw(Graphics2D g2);


    public boolean shouldRenderIn(GameState state) {
        return true;
    }

    public boolean shouldUpdate() {
        return true;
    }

    public boolean shouldDraw() {
        return true;
    }
}
