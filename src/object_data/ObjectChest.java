/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object_data;

import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;

/**
 * ObjectChest.java
 * Represents a chest object in the game.
 * Can be opened or interacted with by the player (behavior handled elsewhere).
 */
public class ObjectChest extends Entity{
    
    /**
     * Constructor: initializes the chest object with size, image, and collision settings.
     * @param gp Reference to the main GamePanel
     * @param mapIndex Index of the map where this object is placed
     */
    public ObjectChest( GamePanel gp,int mapIndex){
        super(gp);
        this.mapIndex = mapIndex;
        // Object name
        name = "chest";
        // Set size equal to tile size
        width = gp.tileSize;
        height = gp.tileSize;
        // Load static image for the chest
        staticImage = setup("/object/chest" , width, height);
        // No collision (player can interact without blocking)
        collision = false;
        // Define solid area for collision detection if needed
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
