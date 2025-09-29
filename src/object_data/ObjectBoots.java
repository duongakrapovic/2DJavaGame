/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object_data;

import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;

/**
 * ObjectBoots.java
 * Represents a collectible boots item in the game.
 * Increases player movement speed when picked up (behavior handled elsewhere).
 */
public class ObjectBoots extends Entity {
    /**
     * Constructor: initializes the boots object with size, image, and collision settings.
     * @param gp Reference to the main GamePanel
     * @param mapIndex Index of the map where this object is placed
     */
    public ObjectBoots(GamePanel gp,int mapIndex){
        super(gp);
        this.gp = gp;
        this.mapIndex = mapIndex;
        // Object name
        name = "boots";
        // Set size relative to tile size
        width = gp.tileSize * 3 / 5;
        height = gp.tileSize * 3 / 5;
        // Load static image for the boots
        staticImage = setup("/object/boots" , width, height);
        // No collision (player can pick up)
        collision = false;
        
        // Define solid area for collision detection if needed
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}