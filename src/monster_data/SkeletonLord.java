package monster_data;

import ai.movement.WanderMovement;
import combat.CombatSystem;
import main.GamePanel;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class SkeletonLord extends Monster {

    private static final String SPRITE_DIR = "/monster/";

    private static final int BASE_SPEED = 2 , ENRAGE_SPEED = 3;
    private static final int ENRAGE_THRESHOLD_PCT = 50;
    private boolean enraged = false;


    private BufferedImage p2Up1, p2Up2, p2Down1, p2Down2, p2Left1, p2Left2, p2Right1, p2Right2;
    private BufferedImage p2AtkUp1, p2AtkUp2, p2AtkDown1, p2AtkDown2, p2AtkLeft1, p2AtkLeft2, p2AtkRight1, p2AtkRight2;

    public SkeletonLord(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;

        name = "Skeleton Lord";
        width = gp.tileSize * 2;
        height = gp.tileSize * 2;
        hasAttackAnim = true;

        collision = true;
        animationON = true;

        // speed
        actualSpeed  = BASE_SPEED;
        defaultSpeed = BASE_SPEED;

        // hitbox
        solidArea = new Rectangle(28, 32, 36, 60);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // stats
        setStats(220 , 15, 10);
        attackKnockback = 8;
        setExpReward(100);

        // combat reach + timing P1
        atkW = gp.tileSize * 2;
        atkH = gp.tileSize * 2;
        combat.setAttackBoxSize(atkW, atkH);
        combat.setTimingFrames(3, 18, 39, 30);

        // trigger radius & face lock
        attackTriggerRadius = Math.max(gp.tileSize * 6, 48);
        faceLockThreshold   = 6;

        direction = "down";

        loadPhaseSprites();
        // easy movement
        setController(new WanderMovement(/*speed*/2, /*changeEveryFrames*/120));
    }

    private void loadPhaseSprites() {
        // ----- Phase 1  -----
        up1    = setup(SPRITE_DIR + "skeletonlord_up_1",    width, height);
        up2    = setup(SPRITE_DIR + "skeletonlord_up_2",    width, height);
        down1  = setup(SPRITE_DIR + "skeletonlord_down_1",  width, height);
        down2  = setup(SPRITE_DIR + "skeletonlord_down_2",  width, height);
        left1  = setup(SPRITE_DIR + "skeletonlord_left_1",  width, height);
        left2  = setup(SPRITE_DIR + "skeletonlord_left_2",  width, height);
        right1 = setup(SPRITE_DIR + "skeletonlord_right_1", width, height);
        right2 = setup(SPRITE_DIR + "skeletonlord_right_2", width, height);

        atkUp1    = setup(SPRITE_DIR + "skeletonlord_attack_up_1",    width, height * 2);
        atkUp2    = setup(SPRITE_DIR + "skeletonlord_attack_up_2",    width, height* 2);
        atkDown1  = setup(SPRITE_DIR + "skeletonlord_attack_down_1",  width, height* 2);
        atkDown2  = setup(SPRITE_DIR + "skeletonlord_attack_down_2",  width, height* 2);
        atkLeft1  = setup(SPRITE_DIR + "skeletonlord_attack_left_1",  width* 2, height);
        atkLeft2  = setup(SPRITE_DIR + "skeletonlord_attack_left_2",  width* 2, height);
        atkRight1 = setup(SPRITE_DIR + "skeletonlord_attack_right_1", width* 2, height);
        atkRight2 = setup(SPRITE_DIR + "skeletonlord_attack_right_2", width* 2, height);

        // ----- Phase 2 (enrage) -----
        p2Up1    = setup(SPRITE_DIR + "skeletonlord_phase2_up_1",    width, height);
        p2Up2    = setup(SPRITE_DIR + "skeletonlord_phase2_up_2",    width, height);
        p2Down1  = setup(SPRITE_DIR + "skeletonlord_phase2_down_1",  width, height);
        p2Down2  = setup(SPRITE_DIR + "skeletonlord_phase2_down_2",  width, height);
        p2Left1  = setup(SPRITE_DIR + "skeletonlord_phase2_left_1",  width, height);
        p2Left2  = setup(SPRITE_DIR + "skeletonlord_phase2_left_2",  width, height);
        p2Right1 = setup(SPRITE_DIR + "skeletonlord_phase2_right_1", width, height);
        p2Right2 = setup(SPRITE_DIR + "skeletonlord_phase2_right_2", width, height);

        p2AtkUp1    = setup(SPRITE_DIR + "skeletonlord_phase2_attack_up_1",    width, height* 2);
        p2AtkUp2    = setup(SPRITE_DIR + "skeletonlord_phase2_attack_up_2",    width, height* 2);
        p2AtkDown1  = setup(SPRITE_DIR + "skeletonlord_phase2_attack_down_1",  width, height* 2);
        p2AtkDown2  = setup(SPRITE_DIR + "skeletonlord_phase2_attack_down_2",  width, height* 2);
        p2AtkLeft1  = setup(SPRITE_DIR + "skeletonlord_phase2_attack_left_1",  width* 2, height);
        p2AtkLeft2  = setup(SPRITE_DIR + "skeletonlord_phase2_attack_left_2",  width* 2, height);
        p2AtkRight1 = setup(SPRITE_DIR + "skeletonlord_phase2_attack_right_1", width* 2, height);
        p2AtkRight2 = setup(SPRITE_DIR + "skeletonlord_phase2_attack_right_2", width* 2, height);
    }

    private void switchToPhase2Sprites() {
        up1 = p2Up1;   up2 = p2Up2;
        down1 = p2Down1; down2 = p2Down2;
        left1 = p2Left1; left2 = p2Left2;
        right1 = p2Right1; right2 = p2Right2;

        atkUp1 = p2AtkUp1;   atkUp2 = p2AtkUp2;
        atkDown1 = p2AtkDown1; atkDown2 = p2AtkDown2;
        atkLeft1 = p2AtkLeft1; atkLeft2 = p2AtkLeft2;
        atkRight1 = p2AtkRight1; atkRight2 = p2AtkRight2;
    }

    @Override
    public void update() {
        updatePhase();

        // ACTIVE: “lao” theo đòn
        if (combat != null && CombatSystem.isAttackActive(combat)) {
            actualSpeed = Math.max(actualSpeed, enraged ? ENRAGE_SPEED + 1 : BASE_SPEED + 1);
        } else {
            actualSpeed = enraged ? ENRAGE_SPEED : BASE_SPEED;
        }

        super.update();
    }

    private void updatePhase() {
        int hpPct = (int) Math.round(getHP() * 100.0 / Math.max(1, getMaxHP()));
        if (!enraged && hpPct <= ENRAGE_THRESHOLD_PCT) {
            enraged = true;

            defaultSpeed = ENRAGE_SPEED;
            actualSpeed  = ENRAGE_SPEED;
            atkW = gp.tileSize * 3;
            atkH = gp.tileSize * 2;
            combat.setAttackBoxSize(atkW, atkH);
            combat.setTimingFrames(6, 8, 10, 36);
            switchToPhase2Sprites();
        }
    }
    @Override
    public void onDeath() {
        // 1) Gọi logic chung của Monster: cộng EXP + random HealthPosion
        super.onDeath();

        // 2) Boss rơi thêm kiếm
        if (gp != null && gp.om != null) {
            gp.om.spawnSword(this.mapIndex, this.worldX, this.worldY);
        }
    }

}
