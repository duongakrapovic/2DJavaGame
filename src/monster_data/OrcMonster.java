package monster_data;

import ai.movement.AggroSwitchMovement;
import ai.movement.ChaseMovement;
import ai.movement.WanderMovement;
import entity.Entity;
import main.GamePanel;
import player_manager.Player;

import java.awt.Rectangle;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class OrcMonster extends Monster {

    private final int wanderSpeed = 1; // chậm hơn goblin, mạnh hơn slime


    public OrcMonster(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;

        name = "Orc";
        width = gp.tileSize;
        height = gp.tileSize;
        this.hasAttackAnim = true;

        getImage(); // load all frames

        collision = true;
        animationON = true;
        actualSpeed = wanderSpeed;

        solidArea = new Rectangle(6, 20, 36, 28);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // ===== Base stats =====
        setStats(40, 8, 5); // hp, defense, exp drop (tùy bạn)
        this.attackKnockback = 8;
        setExpReward(5);
        // ===== Combat config =====
        this.combat.setAttackBoxSize(40, 32);
        this.combat.setTimingFrames(12, 10, 16, 82);

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
        // --- Movement frames ---
        up1    = setup("/monster/orc_up_1", width, height);
        up2    = setup("/monster/orc_up_2", width, height);
        down1  = setup("/monster/orc_down_1", width, height);
        down2  = setup("/monster/orc_down_2", width, height);
        left1  = setup("/monster/orc_left_1", width, height);
        left2  = setup("/monster/orc_left_2", width, height);
        right1 = setup("/monster/orc_right_1", width, height);
        right2 = setup("/monster/orc_right_2", width, height);

        // --- Attack frames ---
        atkUp1    = setup("/monster/orc_attack_up_1", width, height);
        atkUp2    = setup("/monster/orc_attack_up_2", width, height);
        atkDown1  = setup("/monster/orc_attack_down_1", width, height);
        atkDown2  = setup("/monster/orc_attack_down_2", width, height);
        atkLeft1  = setup("/monster/orc_attack_left_1", width, height);
        atkLeft2  = setup("/monster/orc_attack_left_2", width, height);
        atkRight1 = setup("/monster/orc_attack_right_1", width, height);
        atkRight2 = setup("/monster/orc_attack_right_2", width, height);

        atkUp1    = gp.uTool.scaleImage(atkUp1,    width, height * 2);
        atkUp2    = gp.uTool.scaleImage(atkUp2,    width , height * 2);
        atkDown1  = gp.uTool.scaleImage(atkDown1,  width , height * 2);
        atkDown2  = gp.uTool.scaleImage(atkDown2,  width , height * 2);
        atkLeft1  = gp.uTool.scaleImage(atkLeft1,  width * 2, height);
        atkLeft2  = gp.uTool.scaleImage(atkLeft2,  width * 2, height);
        atkRight1 = gp.uTool.scaleImage(atkRight1, width * 2, height);
        atkRight2 = gp.uTool.scaleImage(atkRight2, width * 2, height);

    }
}
