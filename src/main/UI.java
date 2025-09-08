/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import object.ObjectKey;
import object.SuperObject;
import main.GamePanel;

public class UI {
    GamePanel gp;
    Font arial , arial_40B;
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

    public UI(GamePanel gp){
        this.gp = gp;
        
        arial = new Font("Arial", Font.PLAIN , 25);
        arial_40B = new Font("Arial", Font.BOLD , 40);
        // set here to out of loop and on screen before main things
        // if set in draw , it get called 60 times -> slow the game
        ObjectKey key = new ObjectKey(gp);
        keyImage = key.image; 
    }
    
    public void showMessage(String text){
        message = text;
        messageOn = true;
    }
    
    public void showTouchMessage(String text, SuperObject obj , GamePanel gp){
        touchMessage = text;
        touchMessageOn = true;
        
        // print posision will be higher than object a little as 10 pixel
        messPosX = obj.worldX - gp.player.worldX + gp.player.screenX;
        messPosY = obj.worldY - gp.player.worldY + gp.player.screenY - 10;
    }
    
    public void hideTouchMessage(){ // delete mess if non touch 
        touchMessageOn = false;
    }
    
    public void draw(Graphics2D g2){
        
        if(gameFinished == true){
            String text;
            int textLength;
            int x , y;
            
            g2.setFont(arial);
            g2.setColor(Color.white);
                       
            text = "You found the treasure";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();            
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 - (gp.tileSize * 2);
            g2.drawString(text , x, y);
            
            g2.setFont(arial_40B);
            g2.setColor(Color.yellow);
            text = "Congratuations";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();            
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 + (gp.tileSize * 2);
            g2.drawString(text , x, y);
            
            gp.gameThread = null;  
        }
        else{
            g2.setFont(arial);
            g2.setColor(Color.white);
            
            // draw the key and number of keys
            g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, 2*gp.tileSize/3, 2*gp.tileSize/3, null);
                  // image , pos x and y , size x and y , null
            g2.drawString("x " + gp.player.hasKey , gp.tileSize + 30 ,50);
            // normal in java , y = top line of tile, however
            // in drawString , y = bot line of tile,
        
            // MESSAGE
            if(messageOn == true){
                g2.setFont(arial);
                g2.drawString(message , gp.tileSize/2, gp.tileSize * 5);
            
                messCounter++;
                if(messCounter > 120){
                    messCounter = 0;
                    messageOn = false;
                }
            }
            if(touchMessageOn == true){
                int textLength;
                g2.setFont(arial);
                g2.setColor(Color.white);
           
                textLength = (int)g2.getFontMetrics().getStringBounds(touchMessage, g2).getWidth();            
                messPosX = messPosX - textLength/2 + 20;
                g2.drawString(touchMessage , messPosX, messPosY);
            }
        } 
    }
}
// g2.setFont(g2.getFont().deriveFont(30F)); use for manual