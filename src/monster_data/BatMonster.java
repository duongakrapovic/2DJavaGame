package monster_data;

import ai.movement.WanderMovement;
import combat.CombatSystem;
import entity.Entity;
import main.GamePanel;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class BatMonster extends Monster {

    private final int wanderSpeed = 2;   // nhanh hơn orc
    private final int chaseSpeed  = 3;   // bám đuổi khi gần

    public BatMonster(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;

        name = "Bat";
        width = gp.tileSize;
        height = gp.tileSize;
        hasAttackAnim = false;           // dùng frame tileSize, không offset vẽ

        getImage();

        collision = true;
        animationON = true;
        actualSpeed = wanderSpeed;

        // thân nhỏ, khó trúng
        solidArea = new Rectangle(12, 12, width-24, height-20);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // stats
        setStats(10, 0, 2);              // hp, def, exp
        attackDamage = 3;
        attackKnockback = 6;

        // combat ngắn – lao vào quẹt
        combat.setAttackBoxSize(28, 24);
        combat.setTimingFrames(12, 6, 16, 86);

        // AI: lang thang, khi đến gần player thì “đổi sang đuổi”
        setController(new WanderMovement(wanderSpeed, 70));
    }
    private void getImage() {
        // 2 khung đập cánh (tileSize)
        up1    = setup("/monster/bat_down_1", width, height);
        up2    = setup("/monster/bat_down_2", width, height);
        down1  = setup("/monster/bat_down_1", width, height);
        down2  = setup("/monster/bat_down_2", width, height);
        left1  = setup("/monster/bat_down_1", width, height);
        left2  = setup("/monster/bat_down_2", width, height);
        right1 = setup("/monster/bat_down_1", width, height);
        right2 = setup("/monster/bat_down_2", width, height);

        // dùng chung cho attack (không cần phình khung)
        atkUp1=up1; atkUp2=up2; atkDown1=down1; atkDown2=down2;
        atkLeft1=left1; atkLeft2=left2; atkRight1=right1; atkRight2=right2;
    }
    @Override
    public void update() {
        // Optional: “nhảy quệt” – trong ACTIVE tăng speed chút
        if (combat != null && CombatSystem.isAttackActive(combat)) {
            actualSpeed = 3;
        } else {
            actualSpeed = wanderSpeed;
        }
        super.update();
    }
}
