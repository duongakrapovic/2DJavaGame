package ui;

import java.awt.Rectangle;

public class PauseButton {
    protected int x, y, width, height;
    protected Rectangle bounds;
    protected boolean mouseOver, mousePressed;

    public PauseButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void setMouseOver(boolean b){ mouseOver = b; }
    public void setMousePressed(boolean b){ mousePressed = b; }
    public boolean isMouseOver(){ return mouseOver; }
    public boolean isMousePressed(){ return mousePressed; }
    public void resetBools(){ mouseOver = false; mousePressed = false; }
}
