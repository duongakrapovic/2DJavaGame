package monster_data;

import ai.movement.*;
import main.GamePanel;

import java.awt.Rectangle;

public class SlimeMonster extends Monster {
    private final int wanderSpeed = 1;   // px/frame
    private final int chaseSpeed  = 2;   // px/frame
    private final int stopRadius  = 12;  // px (dừng khi đủ gần)
    private final int aggroRadius = 120; // px (phát hiện/đổi sang chase)

    public SlimeMonster(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;

        name = "Green Slime";
        width = gp.tileSize;
        height = gp.tileSize;
        getImage();

        collision = true;
        animationON = true;
        actualSpeed = wanderSpeed;

        solidArea = new Rectangle(3, 18, 42, 30);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setStats(10, 2, 0);

        // Controllers
        setController(new WanderMovement(/*speed*/1, /*changeEveryFrames*/120));
    }
    private void getImage(){
        up1    = setup("/monster/greenslime_down_1" , width , height);
        up2    = setup("/monster/greenslime_down_2" , width , height);
        down1  = setup("/monster/greenslime_down_1" , width , height);
        down2  = setup("/monster/greenslime_down_2" , width , height);
        right1 = setup("/monster/greenslime_down_1",  width , height);
        right2 = setup("/monster/greenslime_down_2" , width , height);
        left1  = setup("/monster/greenslime_down_1" , width , height);
        left2  = setup("/monster/greenslime_down_2" , width , height);
    }
}
