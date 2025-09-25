/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NPC;

import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;

public class NPC_Oldman extends Entity {
    GamePanel gp;
    public NPC_Oldman(GamePanel gp){
        super(gp);
        this.gp = gp;
        name = "oldman" ;
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
