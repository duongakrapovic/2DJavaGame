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
        combat.setTimingFrames(6, 6, 10, 18);

        // AI: lang thang, khi đến gần player thì “đổi sang đuổi”
        setController(new WanderMovement(wanderSpeed, 70));
    }

    @Override
    public void update() {
        // “bán kính kích hoạt đuổi” ~ 6 tile
        Entity player = gp.em.getPlayer();
        int dx = Math.abs(player.worldX - this.worldX);
        int dy = Math.abs(player.worldY - this.worldY);
        boolean shouldChase = (dx + dy) < gp.tileSize * 6;

        actualSpeed = shouldChase ? chaseSpeed : wanderSpeed;

        // auto tấn công nếu áp sát (hitbox thân chạm nhau)
        if (!CombatSystem.isAttacking(combat)) {
            if (intersectsPlayer()) {
                CombatSystem.startAttack(combat, this);
            }
        }

        super.update(); // giữ update mặc định (di chuyển + combat tick)
    }

    private boolean intersectsPlayer() {
        var p = gp.em.getPlayer();
        Rectangle a = new Rectangle(worldX + solidArea.x, worldY + solidArea.y,
                solidArea.width, solidArea.height);
        Rectangle b = new Rectangle(p.worldX + p.solidArea.x, p.worldY + p.solidArea.y,
                p.solidArea.width, p.solidArea.height);
        return a.intersects(b);
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
}
