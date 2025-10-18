package ui;

import main.GamePanel;
import java.awt.*;

public class MessageUI extends BaseUI {
    private boolean show;
    private String message;
    private int counter;

    public MessageUI(GamePanel gp){ super(gp); }

    public void showMessage(String msg){
        message = msg; show = true; counter = 0;
    }

    @Override
    public void update(){
        if(show && ++counter > 120) show = false;
    }

    @Override
    public void draw(Graphics2D g2){
        if(show){
            g2.setColor(Color.white);
            g2.setFont(new Font("Arial", Font.PLAIN, 25));
            g2.drawString(message, gp.tileSize, gp.tileSize*5);
        }
    }
    public void showTouchMessage(String text, Object obj, GamePanel gp) {
        message = text;
        show = true;
        counter = 0;
    }
    public void hideTouchMessage() {
        show = false;
    }

}
