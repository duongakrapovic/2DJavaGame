/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// this stores variables that will be use in player , monster and npc class.
package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
 
public class Entity {
    
    // default setting 
    public int worldX, worldY;
    public int width, height;
    public final int screenX;
    public final int screenY;

    // set for animaion
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage staticImage;// use for enity without animation
    public String direction = "down";
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int actionLockCounter = 0;
    
    // set collision auto false
    public Rectangle solidArea;
    public boolean collisionXOn = false;
    public boolean collisionYOn = false;
    public boolean collisionOn = false;
    public boolean collision  = false;
    
    // set for colision area of entity
    public int solidAreaDefaultX, solidAreaDefaultY;
    
    //enity state
    public String name;
    public int defaultSpeed, actualSpeed, buffSpeed;
    public boolean animationON = false;
    
    private int HP = 0;
    public int getHP() {return HP;}
    public void setHP(int HP) {this.HP = HP;}
    // system
    protected GamePanel gp;
    protected final EntityMovement emo;
    protected final EntitySpriteManager esm;
    protected final EntityDraw ed;
    public int mapIndex = 0;
    // constructor
    public Entity(GamePanel gp) {
        this.gp = gp;
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);
        
        emo = new EntityMovement(gp);
        esm = new EntitySpriteManager();
        ed = new EntityDraw(gp);
        
    }
    // 
    public void update(){
        emo.setAction(this);// change the direction first then we have delta move 
        emo.move(this);// move the entity follow the direction anf check collision
        esm.updateSprite(this);// change animation
    }
    public void draw(Graphics2D g2){
        ed.draw(g2, this);
    }
    public BufferedImage setup(String path, int w, int h){
        return esm.setup(path, w, h);
    }   
}
