package monster_data;

import ai.movement.WanderMovement;
import main.GamePanel;

import java.awt.Rectangle;

public class SlimeMonster extends Monster {
    private final int wanderSpeed = 1;

    public SlimeMonster(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;

        name = "Green Slime";
        width = gp.tileSize; height = gp.tileSize;
        getImage();

        collision = true; animationON = true;
        actualSpeed = wanderSpeed;

        solidArea = new Rectangle(3, 18, 42, 30);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // Stats sinh tồn
        setStats(10, 2, 0);

        // ===== Combat cấu hình (đòn cắn/húc) =====
        this.attackDamage = 2;          // tùy game feel
        this.attackKnockback = 6;
        this.attackTriggerRadius = 28;  // gần hơn chút để natural
        // Kích thước hitbox & timing (có thể khác default của Monster)
        this.combat.setAttackBoxSize(28, 28);
        this.combat.setTimingFrames(6, 6, 10, 20);

        // Di chuyển mặc định: đi lang thang
        setController(new WanderMovement(/*speed*/wanderSpeed, /*changeEveryFrames*/120));
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
        atkUp1 = up1;    atkUp2 = up2;
        atkDown1 = down1; atkDown2 = down2;
        atkLeft1 = left1; atkLeft2 = left2;
        atkRight1 = right1; atkRight2 = right2;

    }
}
