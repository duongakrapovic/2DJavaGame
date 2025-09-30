package ui;

import main.GamePanel;
import java.awt.*;

public abstract class BaseUI {
    protected GamePanel gp;
    public BaseUI(GamePanel gp){ this.gp = gp; }

    public abstract void update();
    public abstract void draw(Graphics2D g2);
}
