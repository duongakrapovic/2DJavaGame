/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monster_data;

import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;

public class SlimeMonster extends Entity {
    GamePanel gp;
    public SlimeMonster(GamePanel gp, int mapIndex){
        super(gp);
        this.gp = gp;
        this.mapIndex = mapIndex;
        name = "Green Slime";
        width = gp.tileSize;
        height = gp.tileSize;
        getImage();
        
        collision = true;
        animationON = true;
        actualSpeed = 1 ;
        
        solidArea = new Rectangle();
        solidArea.x = 3 ;
        solidArea.y = 18 ;
        solidArea.width = 42 ;
        solidArea.height = 30 ;
        solidAreaDefaultX = solidArea.x ;
        solidAreaDefaultY = solidArea.y ;
        
    }
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
