package player_manager;

import combat.CombatSystem;
import entity.Entity;
import interact_manager.Interact;
import input_manager.InputController;
import main.GamePanel;
import main.GameState;
import ui.effects.MessageUI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import object_data.weapons.Weapon;

public class Player extends Entity {
    private Weapon currentWeapon;

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public Weapon setCurrentWeapon(Weapon w) {
        return currentWeapon = w;
    }

    public int hasKey = 0;
    int speedTimer = 0;

    // --- Level / EXP ---
    private int level = 1;
    private int exp = 0;
    private int expToNext = 10;

    // Base stats + tăng mỗi level
    private int baseHp = 15;
    private int baseAtk = 3;
    private int baseDef = 2;

    private int hpPerLevel = 3;
    private int atkPerLevel = 1;
    private int defPerLevel = 1;

    // UI
    private MessageUI msgUI;

    // managers
    public Interact iR;
    public final InputController input;
    private final PlayerSpriteManager psm;
    private final PlayerMovement pm;
    private final PlayerAnimation pa;

    // combat input
    private int attackBtnLock = 0; // chống spam phím

    // --- Interaction debounce (avoid F-key spamming) ---
    private boolean interacting = false;

    public void setInteracting(boolean value) {
        this.interacting = value;
    }

    public boolean isInteracting() {
        return interacting;
    }

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

        // ---- Combat config (dựa trên level)
        recalcStatsFromLevel();
    }

    public void setDefaultValues() {
        mapIndex = 3; // rất quan trọng

        // Ví dụ spawn trước cửa map3
        worldX = gp.tileSize * 15;
        worldY = gp.tileSize * 22;

        defaultSpeed = 5;
        buffSpeed = 4;
        actualSpeed = defaultSpeed;
        direction = "up";
        animationON = true;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getExpToNext() {
        return expToNext;
    }

    public int setLevel(int level) {
        this.level = level;
        return level;
    }

    public int setExp(int exp) {
        this.exp = exp;
        return exp;
    }

    @Override
    public void update() {
        // === ĐANG BỊ KNOCKBACK → chỉ đẩy & tick hệ thống, bỏ qua input ===
        if (isKnockbackActive()) {
            emo.applyKnockback(this);
            CombatSystem.tick(this);
            pa.update(
                    false,
                    CombatSystem.isAttacking(combat),
                    CombatSystem.getPhase(combat)
            );
            return;
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
        handleNPCInteraction();
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
                case "up":
                    image = (spriteNum == 1 ? up1 : up2);
                    break;
                case "down":
                    image = (spriteNum == 1 ? down1 : down2);
                    break;
                case "left":
                    image = (spriteNum == 1 ? left1 : left2);
                    break;
                default:
                    image = (spriteNum == 1 ? right1 : right2);
                    break;
            }
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);

//        g2.setColor(Color.red);
//        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

        if (CombatSystem.isAttackActive(combat)) {
            Rectangle atk = combat.getAttackBox();
            if (atk.width > 0 && atk.height > 0) {
                int ax = atk.x - (worldX - screenX);
                int ay = atk.y - (worldY - screenY);
//                g2.fillRect(ax, ay, atk.width, atk.height);
//                g2.setColor(Color.red);
//                g2.drawRect(ax, ay, atk.width, atk.height);
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

    private void handleNPCInteraction() {
        // Tìm NPC đang chạm player
        int npcIndex = gp.cChecker.checkEntity(this, gp.em.getNPCs(gp.currentMap), worldX, worldY);

        // Không chạm NPC nào -> reset interacting để lần sau lại gần vẫn hiện hint
        if (npcIndex == 999) {
            setInteracting(false);
            return;
        }

        // Lấy NPC
        Entity npc = gp.em.getNPCs(gp.currentMap).get(npcIndex);
        if (npc == null) return;

        if (gp.gsm.getState() != GameState.PLAY) return;

        // === 1) LẠI GẦN NHƯNG CHƯA BẤM E -> HIỆN HINT ===
        if (!input.isTalkPressed()) {

            if (msgUI == null && gp.uiManager != null) {
                msgUI = gp.uiManager.get(MessageUI.class);
            }

            // Chỉ hint cho ông nội + tránh spam bằng isInteracting()
            if (msgUI != null && "oldman".equalsIgnoreCase(npc.name) && !isInteracting()) {
                msgUI.showTouchMessage(
                        "Press 'E' to talk to your grandpa.",
                        npc,   // MessageUI bám theo vị trí ông nội
                        gp
                );
                setInteracting(true);
            }

            // chưa bấm E thì chỉ hint, không mở thoại
            return;
        }

        // === 2) BẤM E -> MỞ ĐỐI THOẠI ===

        // Nếu DialogueUI đang mở sẵn thì bỏ qua
        ui.effects.DialogueUI dialogue = gp.uiManager.get(ui.effects.DialogueUI.class);
        if (dialogue != null && dialogue.isActive()) return;

        npc.facePlayer();
        npc.speak(gp);
        gp.gsm.setState(GameState.DIALOGUE);
    }

    // count stats with level (không còn rank)
    private void recalcStatsFromLevel() {
        int hp = baseHp + (level - 1) * hpPerLevel;
        int atk = baseAtk + (level - 1) * atkPerLevel;
        int def = baseDef + (level - 1) * defPerLevel;

        // dùng setStats của Entity
        setStats(hp, atk, def);
    }

    // exp to next level
    private int calcExpToNext(int lv) {
        // ví dụ: 10 * 1.2^(lv-1)
        double base = 10.0;
        return (int) Math.round(base * Math.pow(1.2, lv - 1));
    }

    /**
     * Player nhận thêm EXP khi giết quái
     */
    public void gainExp(int amount) {
        if (amount <= 0) return;

        int beforeExp = exp;
        int beforeLevel = level;

        System.out.println(
                "[EXP] gainExp +" + amount +
                        " | trước: exp=" + beforeExp + "/" + expToNext +
                        " lv=" + beforeLevel
        );

        this.exp += amount;

        // Lên nhiều level nếu exp dư
        while (exp >= expToNext) {
            exp -= expToNext;
            levelUp();  // trong levelUp cũng sẽ in debug + hiện thông báo
        }

        System.out.println(
                "[EXP] sau gainExp: exp=" + exp + "/" + expToNext +
                        " lv=" + level
        );
    }

    private void levelUp() {
        level++;
        expToNext = calcExpToNext(level);

        recalcStatsFromLevel();   // cập nhật HP/ATK/DEF

        // Hồi full máu khi lên level
        setHP(getMaxHP());

        // --- Thông báo LEVEL UP bằng tiếng Anh ---
        if (msgUI == null && gp != null && gp.uiManager != null) {
            msgUI = gp.uiManager.get(MessageUI.class);
        }
        if (msgUI != null) {
            msgUI.showTouchMessage(
                    "LEVEL UP!  You reached level " + level + "! Get stronger and stronger",
                    null, // không gắn với object nào cụ thể
                    gp
            );
        }
    }

    // Hồi 1 lượng máu cố định
    public void heal(int amount) {
        if (amount <= 0) return;
        int max = getMaxHP();
        int cur = getHP();
        int newHp = Math.min(max, cur + amount);
        setHP(newHp);
    }

    // Hồi theo % máu tối đa (vd 0.1 = 10%)
    public void healPercent(double percent) {
        if (percent <= 0) return;
        int max = getMaxHP();
        int healAmount = (int) Math.round(max * percent);
        if (healAmount <= 0) healAmount = 1; // luôn hồi ít nhất 1 máu
        heal(healAmount);
    }
}
