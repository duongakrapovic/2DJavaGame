/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import entity.Entity;
import object_data.WorldObject;  // ⬅️ quan trọng: dùng WorldObject cho object

public class UI {
    private GamePanel gp;
    private Graphics2D g2;
    Font arial;

    BufferedImage keyImage;

    // normal mess
    public boolean messageOn = false;
    public String message = "";
    int messCounter = 0;

    // touching object mess
    public boolean touchMessageOn = false;
    public String touchMessage = "";
    public int messPosX, messPosY;

    // check if game end
    public boolean gameFinished = false;
    // check request
    public int commandNum = 0;

    // fading effect
    private float alpha = 0f;
    private boolean fading = false;
    private int phase = 0; // 1=fade out, 2=fade in
    private Runnable onFadeComplete;

    public UI(GamePanel gp){
        this.gp = gp;
        arial = new Font("Arial", Font.PLAIN , 25);

        // Lấy keyImage từ danh sách WorldObject của map hiện tại
        if (gp != null && gp.em != null) {
            for (WorldObject obj : gp.em.getWorldObjects(gp.currentMap)) {
                if (obj != null && "key".equals(obj.name)) {
                    keyImage = obj.staticImage;
                    break;
                }
            }
        }
    }

    // print the message in the middle of the screen
    public int getXforCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }

    // draw a rectangle chat box , use for pause screen and chat with npc
    public void drawSubWindow(int x, int y, int width, int height){
        Color c = new Color(0,0,0,150); // r g b alpha
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255); // white;
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5)); // outline thickness
        g2.drawRoundRect(x, y, width, height, 25, 25);
    }

    public void showMessage(String text){
        message = text;
        messageOn = true;
        messCounter = 0;
    }

    /** Hiện touch message với WorldObject (chuẩn) */
    public void showTouchMessage(String text, WorldObject obj, GamePanel gp){
        if (obj == null || gp == null || gp.em == null) return;
        Entity player = gp.em.getPlayer();
        if (player == null) return;

        touchMessage = text;
        touchMessageOn = true;

        // world -> screen
        int sx = obj.worldX - player.worldX + player.screenX;
        int sy = obj.worldY - player.worldY + player.screenY;

        // đặt ngay trên hitbox (nếu có), nhô lên 16px
        if (obj.solidArea != null) {
            sx += obj.solidArea.x;
            sy += obj.solidArea.y - 16;
        } else {
            sy -= 16;
        }

        messPosX = sx;
        messPosY = sy;
    }

    /** (Tuỳ chọn) Overload cũ cho Entity — nếu bạn vẫn gọi ở đâu đó */
    public void showTouchMessage(String text, Entity obj , GamePanel gp){
        if (obj == null || gp == null || gp.em == null) return;
        Entity player = gp.em.getPlayer();
        if (player == null) return;

        touchMessage = text;
        touchMessageOn = true;

        int sx = obj.worldX - player.worldX + player.screenX;
        int sy = obj.worldY - player.worldY + player.screenY - 16;
        messPosX = sx;
        messPosY = sy;
    }

    public void hideTouchMessage(){ // delete mess if non touch
        touchMessageOn = false;
        touchMessage = "";
    }

    public void drawUI(Graphics2D g2){
        this.g2 = g2;

        g2.setFont(arial);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);

        if(gp.gsm.getState() == GameState.PLAY){
            drawMessage();
        }
        if(gp.gsm.getState() == GameState.START){
            drawGameStartScene();
        }
        if(gp.gsm.getState() == GameState.PAUSE){
            drawGamePauseScene();
        }
        drawFade(g2);
    }

    public void drawMessage(){
        if(gp.em.getPlayer().hasKey > 0){
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
            g2.setColor(Color.white);

            // draw the key and number of keys
            g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, 2*gp.tileSize/3, 2*gp.tileSize/3, null);
            g2.drawString("x " + gp.em.getPlayer().hasKey , gp.tileSize + 30 ,50);
        }

        // MESSAGE
        if(messageOn){
            g2.setFont(arial);
            g2.drawString(message , gp.tileSize/2, gp.tileSize * 5);

            messCounter++;
            if(messCounter > 120){
                messCounter = 0;
                messageOn = false;
            }
        }

        if(touchMessageOn){
            g2.setFont(arial);
            g2.setColor(Color.white);

            int textLength = (int)g2.getFontMetrics().getStringBounds(touchMessage, g2).getWidth();

            // KHÔNG thay đổi messPosX mỗi frame — tính drawX tạm để tránh “trôi”
            int drawX = messPosX - textLength/2;
            int drawY = messPosY;

            // viền đen mỏng cho dễ đọc
            g2.setColor(Color.black);
            g2.drawString(touchMessage , drawX + 1, drawY + 1);
            g2.setColor(Color.white);
            g2.drawString(touchMessage , drawX, drawY);
        }
    }

    public void drawGameStartScene(){
        g2.setColor(new Color(70 , 120 , 90));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80F));
        String text = "BLUE AND JUNGLE";
        int x = getXforCenteredText(text) + 16;
        int y = gp.tileSize * 3;

        g2.setColor(Color.gray);
        g2.drawString(text, x+5, y+5);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        x = gp.screenWidth / 2 - gp.tileSize;
        y = gp.screenHeight / 2 - 2 * gp.tileSize;
        g2.drawImage(gp.em.getPlayer().down1, x, y , gp.tileSize*2 , gp.tileSize *2 , null);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));

        text = "NEW GAME";
        x = getXforCenteredText(text);
        y = gp.tileSize * 9;
        g2.drawString(text, x, y);
        if(commandNum == 0){
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y = gp.tileSize * 10;
        g2.drawString(text, x, y);
        if(commandNum == 1){
            g2.drawString(">", x - gp.tileSize, y);
        }
    }

    public void drawGamePauseScene(){
        int x = gp.screenWidth / 2 - 5 * gp.tileSize;
        int y = gp.screenHeight / 2 - 2 * gp.tileSize;
        int width = gp.tileSize * 10;
        int height = gp.tileSize * 6;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80F));
        g2.setColor(Color.white);

        String text = "RESUME";
        x = getXforCenteredText(text);
        y = gp.screenHeight / 2;
        g2.drawString(text, x, y);
        if(commandNum == 0){
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y = gp.tileSize * 10;
        g2.drawString(text, x, y);
        if(commandNum == 1){
            g2.drawString(">", x - gp.tileSize, y);
        }
    }

    public void startFade(Runnable onComplete){
        fading = true;
        phase = 1; // bắt đầu fade out
        alpha = 0f;
        this.onFadeComplete = onComplete;
    }

    private void drawFade(Graphics2D g2){
        if(!fading) return;
        Color c = new Color(0,0,0,(int)(alpha*255));
        g2.setColor(c);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        if(phase == 1){
            alpha += 0.05f;
            if(alpha >= 1f){
                alpha = 1f;
                if(onFadeComplete != null){
                    onFadeComplete.run(); // đổi map ở đây
                    onFadeComplete = null;
                }
                phase = 2; // sang fade in
            }
        }
        else if(phase == 2){
            alpha -= 0.05f;
            if(alpha <= 0f){
                alpha = 0f;
                fading = false; // kết thúc
            }
        }
    }
}
