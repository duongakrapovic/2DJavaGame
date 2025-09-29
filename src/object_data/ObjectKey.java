/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object_data;

import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;
/**
 * ObjectKey.java
 * Represents a key item in the game.
 * Can be collected by the player to unlock doors or chests (behavior handled elsewhere).
 */
public class ObjectKey extends Entity{
    /**
     * Constructor: initializes the key object with size, image, and collision area.
     * @param gp Reference to the main GamePanel
     * @param mapIndex Index of the map where this object is placed
     */
    public ObjectKey( GamePanel gp,int mapIndex){
        super(gp);
        this.gp = gp;
        this.mapIndex = mapIndex;
        // Object name
        name = "key";
        // Set size relative to tile size   
        width = gp.tileSize * 3 / 5;
        height = gp.tileSize * 3 / 5;
        // Load static image for the key
        staticImage = setup("/object/key" , width, height);
        // No collision (player can pick up)
        collision = false;
        
        // Define solid area with small offset for collision detection
        solidArea = new Rectangle(-gp.tileSize/4, -gp.tileSize/4,
                        width + gp.tileSize/2, height + gp.tileSize/2);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
