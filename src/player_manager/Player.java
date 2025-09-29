/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package player_manager;

import entity.Entity;
import interact_manager.Interact;
import input_manager.InputController;
import main.GamePanel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

/**
 * Player.java
 * Represents the main player character in the game.
 * Handles input, movement, collision, animation, and interactions with objects, NPCs, and monsters.
 */
public class Player extends Entity{
    
    public int hasKey = 0; // Number of keys currently held
    int speedTimer = 0; // Countdown timer for speed buff;
    
    public Interact iR;// Interaction manager
    public final InputController input;// Input handler for movement
    private final PlayerSpriteManager psm;
    private final PlayerMovement pm;
    private final PlayerAnimation pa;
    /**
     * Constructor: initializes player with input, position, collision box, and images.
     * @param gp Reference to the GamePanel
     * @param input Input controller for handling player input
     */
    public Player(GamePanel gp , InputController input ){
        super(gp);
        this.iR = new Interact(gp, this, input);
        this.input = input;
        
        // Initialize collision box
        solidArea = new Rectangle(11, 16, 25,25);
        solidAreaDefaultX =  solidArea.x;
        solidAreaDefaultY =  solidArea.y;
        
        setDefaultValues();
        
        // Init managers
        psm = new PlayerSpriteManager(gp);
        psm.loadSprites(this);
        pm = new PlayerMovement(this, gp);
        pa = new PlayerAnimation(this);
    }
    /** Set default values for position, speed, direction, and animation */
    public void setDefaultValues(){
        // central of the chunk 
        worldX = gp.tileSize * gp.chunkSize / 2;
        worldY = gp.tileSize * gp.chunkSize / 2;
        defaultSpeed = 15;
        buffSpeed = 4;
        actualSpeed = defaultSpeed;
        direction = "down";
        animationON = true;
        //System.out.println("complete setdefault");
    }
    
    /** Update player's state, movement, collision, and interactions */
    @Override
    public void update(){
        int[] delta = pm.calculateMovement();
        pm.move(delta[0], delta[1]);
        pa.update(pm.isMoving());

        // Speed buff timer
        if(speedTimer > 0) speedTimer--;
        if(speedTimer == 0) actualSpeed = defaultSpeed;
    }
    
    /** Draw the player on the screen with proper sprite */
    @Override
    public void draw(Graphics2D g2){
//      g2.setColor(Color.white);// color to draw objects      
//      g2.fillRect(x , y, gp.tileSize , gp.tileSize);

        BufferedImage image = null;
        switch(direction){
        case "up": image = spriteNum==1 ? up1 : up2; break;
        case "down": image = spriteNum==1 ? down1 : down2; break;
        case "left": image = spriteNum==1 ? left1 : left2; break;
        case "right": image = spriteNum==1 ? right1 : right2; break;
    }
        g2.drawImage(image, screenX, screenY, null);
        g2.setColor(Color.red);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }  
}
