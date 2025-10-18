/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package player_manager;

import entity.Entity;
import main.GamePanel;

public class PlayerSpriteManager {
    private final GamePanel gp;
    
    public PlayerSpriteManager(GamePanel gp) {
        this.gp = gp;
    }
    
    /** Load player sprite images for all directions */
    public void loadSprites(Entity player){
        
        player.up1 = player.setup("/player/boy_up_1", gp.tileSize, gp.tileSize);
        player.up2 = player.setup("/player/boy_up_2", gp.tileSize, gp.tileSize);
        player.down1 = player.setup("/player/boy_down_1", gp.tileSize, gp.tileSize);
        player.down2 = player.setup("/player/boy_down_2", gp.tileSize, gp.tileSize);
        player.left1 = player.setup("/player/boy_left_1", gp.tileSize, gp.tileSize);
        player.left2 = player.setup("/player/boy_left_2", gp.tileSize, gp.tileSize);
        player.right1 = player.setup("/player/boy_right_1", gp.tileSize, gp.tileSize);
        player.right2 = player.setup("/player/boy_right_2", gp.tileSize, gp.tileSize);

        player.atkUp1    = player.setup("/player/boy_attack_up_1", gp.tileSize, gp.tileSize * 2);
        player.atkUp2    = player.setup("/player/boy_attack_up_2", gp.tileSize, gp.tileSize * 2);
        player.atkDown1  = player.setup("/player/boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
        player.atkDown2  = player.setup("/player/boy_attack_down_2", gp.tileSize, gp.tileSize * 2);
        player.atkLeft1  = player.setup("/player/boy_attack_left_1", gp.tileSize * 2, gp.tileSize );
        player.atkLeft2  = player.setup("/player/boy_attack_left_2", gp.tileSize * 2, gp.tileSize );
        player.atkRight1 = player.setup("/player/boy_attack_right_1", gp.tileSize * 2, gp.tileSize );
        player.atkRight2 =player.setup("/player/boy_attack_right_2", gp.tileSize * 2, gp.tileSize );
    } 
}
