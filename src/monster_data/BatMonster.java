package monster_data;

import ai.movement.AggroSwitchMovement;
import ai.movement.ChaseMovement;
import ai.movement.WanderMovement;
import combat.CombatSystem;
import entity.Entity;
import main.GamePanel;
import player_manager.Player;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BatMonster extends Monster {

    private final int wanderSpeed = 2;
    private final int chaseSpeed  = 3;

    public BatMonster(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;

        name = "Bat";
        width = gp.tileSize;
        height = gp.tileSize;
        hasAttackAnim = false;

        getImage();

        collision = true;
        animationON = true;
        actualSpeed = wanderSpeed;

        // thân nhỏ, khó trúng
        solidArea = new Rectangle(12, 12, width-24, height-20);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // stats
        setStats(20, 5, 3);
        setExpReward(5);
        attackDamage = 3;
        attackKnockback = 6;

        // combat ngắn – lao vào quẹt
        combat.setAttackBoxSize(28, 24);
        combat.setTimingFrames(3, 18, 42, 30);

        // movement AI
        var wander = new WanderMovement(2, 240);
        Supplier<Player> playerSup = () -> (gp.em != null ? gp.em.getPlayer() : null);
        var chase  = new ChaseMovement(gp, playerSup, 2, gp.tileSize);

        Predicate<Entity> aggroCond = me -> {
            Player p = playerSup.get();
            if (p == null || p.isDead()) return false;
            long dx = (long)p.worldX - me.worldX;
            long dy = (long)p.worldY - me.worldY;
            long dist2 = dx*dx + dy*dy;
            long r = 1L * gp.tileSize * 6;
            return dist2 < r*r;
        };

        setController(new AggroSwitchMovement(wander, chase, aggroCond));
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

        // dùng chung cho attack
        atkUp1=up1; atkUp2=up2; atkDown1=down1; atkDown2=down2;
        atkLeft1=left1; atkLeft2=left2; atkRight1=right1; atkRight2=right2;
    }
    @Override
    public void update() {
        if (combat != null && CombatSystem.isAttackActive(combat)) {
            actualSpeed = 3;
        } else {
            actualSpeed = wanderSpeed;
        }
        super.update();
    }
}
