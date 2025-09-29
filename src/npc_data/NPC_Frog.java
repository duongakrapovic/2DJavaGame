/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npc_data;

import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;
/**
 * NPC_Frog.java
 * Represents a frog NPC in the game.
 * Stationary enemy or interactable character with basic animation.
 */
public class NPC_Frog extends Entity{
    // Reference to main GamePanel
    GamePanel gp;
    /**
     * Constructor: initializes frog NPC with size, images, collision, and animation settings.
     * @param gp Reference to the main GamePanel
     * @param mapIndex Index of the map where this NPC is placed
     */
    public NPC_Frog(GamePanel gp,int mapIndex){
        super(gp);
        this.gp = gp;
        this.mapIndex = mapIndex;
        // NPC name
        name = "frog" ;
        // Set size equal to one tile
        width = gp.tileSize;
        height = gp.tileSize;
        // Load images for all directions
        getImage();    
        // Enable collision and animation
        collision = true;
        animationON = true;
        
        // Stationary by default
        actualSpeed = 0 ;
        
        // Define solid area for collision detection
        solidArea = new Rectangle();
        solidArea.x = 3 ;
        solidArea.y = 18 ;
        solidArea.width = 42 ;
        solidArea.height = 30 ;
        solidAreaDefaultX = solidArea.x ;
        solidAreaDefaultY = solidArea.y ;       
    }
    /**
     * Load images for all directions and animation frames.
     */
    private void getImage(){
        up1 = setup("/npc/frog1" , width , height) ;
        up2 = setup("/npc/frog2" , width ,  height) ;
        down1 = setup("/npc/frog1" , width, height) ;
        down2 = setup("/npc/frog2" , width, height) ;
        right1 = setup("/npc/frog1", width , height) ;
        right2 = setup("/npc/frog2" , width, height) ;
        left1 = setup("/npc/frog1" , width, height) ;
        left2 = setup("/npc/frog2" , width , height) ;
    }
}
