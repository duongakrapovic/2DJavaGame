/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monster_data;

import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;

/**
 * SlimeMonster.java
 * Represents a green slime monster in the game.
 * Simple moving enemy with basic animation and collision.
 */
public class SlimeMonster extends Monster {
    // Reference to main GamePanel
    GamePanel gp;
    /**
     * Constructor: initializes slime monster with size, images, speed, and collision settings.
     * @param gp Reference to the main GamePanel
     * @param mapIndex Index of the map where this monster is placed
     */
    public SlimeMonster(GamePanel gp, int mapIndex){ 
        super(gp);
        this.gp = gp;
        this.mapIndex = mapIndex;
        
        // Monster name
        name = "Green Slime";
        // Set size equal to one tile
        width = gp.tileSize;
        height = gp.tileSize;
        // Load images for all directions and animation frames
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

        setStats(10, 2, 0);
    }
    /**
     * Load images for all directions and animation frames.
     */
    private void getImage(){
        up1 = setup("/monster/greenslime_down_1" , width , height) ;
        up2 = setup("/monster/greenslime_down_2" , width ,  height) ;
        down1 = setup("/monster/greenslime_down_1" , width, height) ;
        down2 = setup("/monster/greenslime_down_2" , width, height) ;
        right1 = setup("/monster/greenslime_down_1", width , height) ;
        right2 = setup("/monster/greenslime_down_2" , width, height) ;
        left1 = setup("/monster/greenslime_down_1" , width, height) ;
        left2 = setup("/monster/greenslime_down_2" , width , height) ;
    }
}
