package player_manager;

import combat.CombatSystem;
import entity.Entity;
import interact_manager.Interact;
import input_manager.InputController;
import main.GamePanel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    public int hasKey = 0;      // số chìa đang giữ
    int speedTimer = 0;         // đếm ngược buff tốc

    // managers
    public Interact iR;
    public final InputController input;
    private final PlayerSpriteManager psm;
    private final PlayerMovement pm;
    private final PlayerAnimation pa;

    // combat input
    private int attackBtnLock = 0; // chống spam phím

    public Player(GamePanel gp, InputController input) {
        super(gp);
        this.iR = new Interact(gp, this, input);
        this.input = input;

        // Collision box mặc định
        solidArea = new Rectangle(11, 16, 25, 25);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();

        // Managers
        psm = new PlayerSpriteManager(gp);
        psm.loadSprites(this);
        pm = new PlayerMovement(this, gp);
        pa = new PlayerAnimation(this);

        // ---- Combat config (dùng CombatComponent mới) ----
        combat.setAttackBoxSize(36, 36);           // kích thước hitbox
        combat.setTimingFrames(4, 6, 8, 10);       // windup, active, recover, cooldown
        setStats(10, 3, 1);                        // maxHp, atk, def
    }

    /** Set default values for position, speed, direction, and animation */
    public void setDefaultValues() {
        // trung tâm chunk
        worldX = gp.tileSize * gp.chunkSize / 2;
        worldY = gp.tileSize * gp.chunkSize / 2;
        defaultSpeed = 15;
        buffSpeed = 4;
        actualSpeed = defaultSpeed;
        direction = "down";
        animationON = true;
    }

    /** Update player's state, movement, collision, and interactions */
    @Override
    public void update() {
        int[] delta = pm.calculateMovement();
        pm.move(delta[0], delta[1]);

        // cập nhật animation dựa trên trạng thái combat mới
        pa.update(
                pm.isMoving(),
                CombatSystem.isAttacking(combat),
                CombatSystem.getPhase(combat)
        );

        // Speed buff timer
        if (speedTimer > 0) speedTimer--;
        if (speedTimer == 0) actualSpeed = defaultSpeed;

        // combat
        handleAttackInput();
        // ⬇️ GỘP: phase tấn công + i-frame + knockback
        CombatSystem.tick(this);
    }


    /** Draw the player on the screen with proper sprite */
    @Override
    public void draw(Graphics2D g2) {
        if (isInvulnerable()) {
            int fc = gp.frameCounter;            // cần biến frameCounter trong GamePanel
            boolean visible = (fc / 6) % 2 == 0; // nháy 6 frame
            if (!visible) return;
        }

        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        boolean attacking = CombatSystem.isAttacking(combat);
        if (attacking) {
            switch (direction) {
                case "up":
                    image = (spriteNum == 1 ? atkUp1 : atkUp2);
                    tempScreenY -= gp.tileSize;
                    break;
                case "down":
                    image = (spriteNum == 1 ? atkDown1 : atkDown2);
                    break;
                case "left":
                    image = (spriteNum == 1 ? atkLeft1 : atkLeft2);
                    tempScreenX -= gp.tileSize;
                    break;
                default: // right
                    image = (spriteNum == 1 ? atkRight1 : atkRight2);
                    break;
            }
        } else {
            switch (direction) {
                case "up":    image = (spriteNum == 1 ? up1    : up2);    break;
                case "down":  image = (spriteNum == 1 ? down1  : down2);  break;
                case "left":  image = (spriteNum == 1 ? left1  : left2);  break;
                default:      image = (spriteNum == 1 ? right1 : right2); break;
            }
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);

        // debug: vẽ body rect
        g2.setColor(Color.red);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

        // debug: vẽ attack box khi Active (world -> screen)
        if (CombatSystem.isAttackActive(combat)) {
            Rectangle atk = combat.getAttackBox();
            if (atk.width > 0 && atk.height > 0) {
                int ax = atk.x - (worldX - screenX);
                int ay = atk.y - (worldY - screenY);
                g2.fillRect(ax, ay, atk.width, atk.height);
                g2.setColor(Color.red);
                g2.drawRect(ax, ay, atk.width, atk.height);
            }
        }
    }

    private void handleAttackInput() {
        if (attackBtnLock > 0) attackBtnLock--;

        if (input.isAttackPressed() && attackBtnLock == 0) {
            attackBtnLock = 6; // ~0.1s @60fps
            if (CombatSystem.canStartAttack(combat)) {
                CombatSystem.startAttack(combat, this);
                System.out.println("Combat started");
            }
        }
    }
}
