/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object_data;

import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;
/**
 * ObjectDoor.java
 * Represents a door object in the game.
 * Blocks the player until unlocked (collision enabled).
 */
public class ObjectDoor extends Entity{
    /**
     * Constructor: initializes the door object with size, image, and collision area.
     * @param gp Reference to the main GamePanel
     * @param mapIndex Index of the map where this object is placed
     */
    public ObjectDoor( GamePanel gp,int mapIndex){
        super(gp);
        this.gp = gp;
        this.mapIndex = mapIndex;
        
        // Object name
        name = "door";
        
        // Set size equal to one tile
        width = gp.tileSize;
        height = gp.tileSize;
        
        // Load static image for the door
        staticImage = setup("/object/door" , width, height);
        // Enable collision (player cannot pass through)
        collision = true;
        
        // Define solid area for collision detection
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    } 
}
