/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object_data;

import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;
/**
 * ObjectPortal.java
 * Represents a portal object in the game.
 * Animated object with two frames that can transport the player (behavior handled elsewhere).
 */
public class ObjectPortal extends Entity{
    
    /**
     * Constructor: initializes the portal object with size, images, animation, and collision area.
     * @param gp Reference to the main GamePanel
     * @param mapIndex Index of the map where this object is placed
     */
    public ObjectPortal( GamePanel gp,int mapIndex){
        super(gp);
        this.mapIndex = mapIndex;
        // Object name
        name = "portal";
        // Set size larger than a single tile
        width = gp.tileSize * 3/2 ;
        height = gp.tileSize * 3/2;
        // Load two animation frames
        down1 = setup("/object/portal1" , width, height);
        down2 = setup("/object/portal2" , width, height);
        // No collision, player can pass through
        collision = false;
        animationON = true;  // Enable animation
        
        // Define solid area for collision detection if needed
        solidArea = new Rectangle(-gp.tileSize/25, -gp.tileSize/25, width, height);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
    /**
     * Update the animation frame of the portal.
     * Switches between two frames every 30 updates.
     */
    @Override
    public void update() {
        spriteCounter++;
        if (spriteCounter > 30) { 
            if (spriteNum == 1) {
                spriteNum = 2;
            } 
            else {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
}
