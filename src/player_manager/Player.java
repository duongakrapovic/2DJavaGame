package player_manager;

import combat.CombatSystem;
import entity.Entity;
import interact_manager.Interact;
import input_manager.InputController;
import main.GamePanel;
import ui.MessageUI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import object_data.weapons.Weapon;

public class Player extends Entity {
    private Weapon currentWeapon;
    public Weapon getCurrentWeapon() { return currentWeapon; }
    public Weapon setCurrentWeapon(Weapon w){return currentWeapon = w;}

    public int hasKey = 0;
    int speedTimer = 0;

    //ui
    private MessageUI msgUI;

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
        this.msgUI = gp.uiManager.get(MessageUI.class);

        // default collision hitbox
        solidArea = new Rectangle(11, 16, 25, 25);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();

        // Managers
        psm = new PlayerSpriteManager(gp);
        psm.loadSprites(this);
        pm = new PlayerMovement(this, gp);
        pa = new PlayerAnimation(this);

        // ---- Combat config
        setStats(100, 3, 1);
    }
    
    public void setDefaultValues() {
        
        worldX = gp.tileSize * (gp.chunkSize / 2 + 7) - 8;
        worldY = gp.tileSize * (gp.chunkSize / 2 + 1) - 8;
        defaultSpeed = 10;
        buffSpeed = 4;
        actualSpeed = defaultSpeed;
        direction = "down";
        animationON = true;
    }

    @Override
    public void update() {
        // === ĐANG BỊ KNOCKBACK → chỉ đẩy & tick hệ thống, bỏ qua input ===
        if (isKnockbackActive()) {
            emo.applyKnockback(this);              // dịch chuyển theo velX/velY + giảm counter
            CombatSystem.tick(this);               // cập nhật phase, i-frames, cooldown...
            pa.update(                             // animation vẫn cập nhật (đang không đi bộ)
                    /*isMoving=*/false,
                    CombatSystem.isAttacking(combat),
                    CombatSystem.getPhase(combat)
            );
            return;                                // QUAN TRỌNG: không xử lý input trong frame này
        }

        int[] delta = pm.calculateMovement();
        pm.move(delta[0], delta[1]);

        pa.update(
                pm.isMoving(),
                CombatSystem.isAttacking(combat),
                CombatSystem.getPhase(combat)
        );

        if (speedTimer > 0) speedTimer--;
        if (speedTimer == 0) actualSpeed = defaultSpeed;

        handleAttackInput();
        CombatSystem.tick(this);
    }



    @Override
    public void draw(Graphics2D g2) {
        if (isInvulnerable()) {
            int fc = gp.frameCounter;
            boolean visible = (fc / 6) % 2 == 0;
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

        // debug: body rect
        g2.setColor(Color.red);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

        // debug: attackBox
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
    public void equipWeapon(Weapon weapon) {
        if (weapon == null) return;
        this.currentWeapon = weapon;

        combat.setTimingFrames(
                weapon.windup(), weapon.active(), weapon.recover(), weapon.cooldown()
        );
        combat.setAttackBoxSize(
                weapon.atkBoxW(), weapon.atkBoxH()
        );

        psm.loadAttackSprites(this, weapon.spriteKey());
    }

}
