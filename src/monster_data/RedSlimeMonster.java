package monster_data;

import ai.movement.WanderMovement;
import combat.CombatSystem;
import main.GamePanel;

import java.awt.Rectangle;

public class RedSlimeMonster extends Monster {

    private final int wanderSpeed = 2;

    public RedSlimeMonster(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;

        name = "RedSlime";
        width = gp.tileSize;
        height = gp.tileSize;
        hasAttackAnim = false;

        getImage();

        collision = true;
        animationON = true;
        actualSpeed = wanderSpeed;

        // slime thân bè ⇒ hitbox thấp
        solidArea = new Rectangle(10, 18, width-20, height-22);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // stats nhỉnh hơn slime thường
        setStats(12, 3, 1);       // hp, def, exp
        attackDamage = 4;
        attackKnockback = 5;

        // combat: quệt ngắn
        combat.setAttackBoxSize(30, 26);
        combat.setTimingFrames(20, 6, 16, 40);

        setController(new WanderMovement(1, 240));
    }

    private void getImage() {
        up1    = setup("/monster/redslime_down_1", width, height);
        up2    = setup("/monster/redslime_down_2", width, height);
        down1  = setup("/monster/redslime_down_1", width, height);
        down2  = setup("/monster/redslime_down_2", width, height);
        left1  = setup("/monster/redslime_down_1", width, height);
        left2  = setup("/monster/redslime_down_2", width, height);
        right1 = setup("/monster/redslime_down_1", width, height);
        right2 = setup("/monster/redslime_down_2", width, height);

        // không dùng attack-anim riêng
        atkUp1=up1; atkUp2=up2; atkDown1=down1; atkDown2=down2;
        atkLeft1=left1; atkLeft2=left2; atkRight1=right1; atkRight2=right2;
    }

}
