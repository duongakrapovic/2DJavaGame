package npc_data;

import ai.movement.WanderMovement;
import entity.Entity;
import main.GamePanel;

import java.awt.Rectangle;

public class NPC_Oldman extends Entity {

    public NPC_Oldman(GamePanel gp, int mapIndex){
        super(gp);
        this.mapIndex = mapIndex;
        name = "oldman";

        width = gp.tileSize; height = gp.tileSize;
        getImage();

        collision = true; animationON = true;
        actualSpeed = 1;

        solidArea = new Rectangle(3, 18, 42, 30);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // đi lang thang, đổi hướng mỗi ~2s
        setController(new WanderMovement(/*speed*/1, /*changeEveryFrames*/120));
    }

    private void getImage(){
        up1    = setup("/npc/oldman_up_1" , width , height);
        up2    = setup("/npc/oldman_up_2" , width , height);
        down1  = setup("/npc/oldman_down_1" , width, height);
        down2  = setup("/npc/oldman_down_2" , width, height);
        right1 = setup("/npc/oldman_right_1", width , height);
        right2 = setup("/npc/oldman_right_2" , width, height);
        left1  = setup("/npc/oldman_left_1" , width, height);
        left2  = setup("/npc/oldman_left_2" , width , height);
    }
}
