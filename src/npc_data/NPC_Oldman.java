/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npc_data;

import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;

/**
 * NPC_Oldman.java
 * Represents an old man NPC in the game.
 * Moves slowly and has basic animations in all directions.
 */
public class NPC_Oldman extends Entity {
    // Reference to main GamePanel
    GamePanel gp;
    /**
     * Constructor: initializes old man NPC with size, images, speed, and collision settings.
     * @param gp Reference to the main GamePanel
     * @param mapIndex Index of the map where this NPC is placed
     */
    public NPC_Oldman(GamePanel gp, int mapIndex){
        super(gp);
        this.gp = gp;
        this.mapIndex = mapIndex;
        // NPC name
        name = "oldman" ;
        // Set size equal to one tile
        width = gp.tileSize;
        height = gp.tileSize;
        // Load images for all directions    
        getImage();    
        // Enable collision and animation
        collision = true;
        animationON = true;
        // Movement speed
        actualSpeed = 1 ;
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
        up1 = setup("/npc/oldman_up_1" , width , height) ;
        up2 = setup("/npc/oldman_up_2" , width ,  height) ;
        down1 = setup("/npc/oldman_down_1" , width, height) ;
        down2 = setup("/npc/oldman_down_2" , width, height) ;
        right1 = setup("/npc/oldman_right_1", width , height) ;
        right2 = setup("/npc/oldman_right_2" , width, height) ;
        left1 = setup("/npc/oldman_left_1" , width, height) ;
        left2 = setup("/npc/oldman_left_2" , width , height) ;
    }
}
