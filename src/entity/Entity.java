package entity;

import combat.*;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import ai.movement.MovementController;

/** Base class for all world entities (player, monsters, NPCs). */
public class Entity implements CombatContext {

    // --- world/screen ---
    public int worldX, worldY;
    public int width, height;
    public final int screenX;
    public final int screenY;

    // --- animation ---
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage atkUp1, atkUp2, atkDown1, atkDown2, atkLeft1, atkLeft2, atkRight1, atkRight2;
    public BufferedImage staticImage;
    public String direction = "down";  // hướng cho di chuyển / AI
    public String attackDir = "down";  // hướng đã lock cho animation tấn công
    public int spriteCounter = 0;
    public int spriteNum = 1;

    // --- collision ---
    public Rectangle solidArea;
    public boolean collisionXOn = false;
    public boolean collisionYOn = false;
    public boolean collisionOn = false;
    public boolean collision = false;
    public int solidAreaDefaultX, solidAreaDefaultY;

    // --- state / stats ---
    public String name;
    public int defaultSpeed, actualSpeed, buffSpeed;
    public boolean animationON = false;

    private int hp = 1, maxHp = 1, atk = 1, def = 0;

    // --- i-frames ---
    private boolean invulnerable = false;
    private int invulnFrames = 20; // ~0.33s @60fps
    private int invulnCounter = 0;

    // --- Fields (thêm hoặc giữ nếu đã có) ---
    public int velX = 0, velY = 0;
    private int knockbackCounter = 0;
    private int knockbackFrames = 12;

    // --- attack box (shared with CombatComponent) ---
    public Rectangle attackBox = new Rectangle(0, 0, 0, 0);

    // --- systems/manager ---
    public final GamePanel gp;
    protected final EntityMovement emo;
    protected final EntitySpriteManager esm;
    protected final EntityDraw ed;
    public int mapIndex = 0;

    // === Dialogue System ===
    public String[][] dialogues = new String[20][20];  // [set][line]
    public int dialogueSet = 0;

    // --- Combat ECS ---
    public final CombatComponent combat;

    // --- Movement Controller (AI / input strategy) ---
    protected MovementController controller;
    public boolean hasAttackAnim = false; // default: false (Slime, v.v.)
    public Entity lastHitBy = null;

    public Entity(GamePanel gp) {
        this.gp = gp;
        this.screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        this.screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        this.emo = new EntityMovement(gp);
        this.esm = new EntitySpriteManager();
        this.ed = new EntityDraw(gp);

        this.combat = new CombatComponent();
        this.attackBox = combat.getAttackBox(); // dùng chung rect để code vẽ cũ không phải đổi
        this.attackDir = this.direction;  // init mặc định
    }

    // -------- controller ----------
    public void setController(MovementController c) {
        this.controller = c;
    }

    public MovementController getController() {
        return controller;
    }

    // -------- stats ----------
    public void setStats(int maxHp, int atk, int def) {
        this.maxHp = Math.max(1, maxHp);
        this.hp = this.maxHp;
        this.atk = Math.max(0, atk);
        this.def = Math.max(0, def);
    }

    public int getHP() {
        return hp;
    }

    public int getMaxHP() {
        return maxHp;
    }

    public int getATK() {
        return atk;
    }

    public int getDEF() {
        return def;
    }

    public void setHP(int value) {
        this.hp = Math.max(0, Math.min(value, maxHp));
    }
    public void reduceHP(int amount) {
        int dmg = Math.max(0, amount);
        int old = hp;
        hp = Math.max(0, hp - dmg);

        System.out.println("[HP] " + name +
                " -" + dmg +
                " (" + old + " -> " + hp + ")");
    }

    public void setInvulnFrames(int frames) {
        invulnFrames = Math.max(0, frames);
    }

    public int getInvulnFrames() {
        return invulnFrames;
    }

    @Override
    public boolean isDead() {
        return hp <= 0;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean v) {
        invulnerable = v;
    }

    public int getInvulnCounter() {
        return invulnCounter;
    }

    public void setInvulnCounter(int v) {
        invulnCounter = v;
    }

    public int getKnockbackFrames() {
        return knockbackFrames;
    }

    public int getKnockbackCounter() {
        return knockbackCounter;
    }

    public void setKnockbackCounter(int v) {
        knockbackCounter = v;
    }

    public boolean isKnockbackActive() {
        return knockbackCounter > 0;
    }

    public void setKnockbackFrames(int f) {
        knockbackFrames = Math.max(1, f);
    }

    public void clearVelocity() {
        this.velX = 0;
        this.velY = 0;
    }

    @Override
    public int getWorldX() {
        return worldX;
    }

    @Override
    public int getWorldY() {
        return worldY;
    }

    @Override
    public Rectangle getSolidArea() {
        return (solidArea != null)
                ? solidArea
                : new Rectangle(0, 0, Math.max(1, width), Math.max(1, height));
    }

    @Override
    public String getDirection() {
        return direction;
    }

    protected int[] computeDelta() {
        int dx = 0, dy = 0;
        switch (direction) {
            case "up":
                dy = -actualSpeed;
                break;
            case "down":
                dy = actualSpeed;
                break;
            case "left":
                dx = -actualSpeed;
                break;
            case "right":
                dx = actualSpeed;
                break;
        }
        return new int[]{dx, dy};
    }

    public void update() {
        // Nếu đang KB → chỉ đẩy; bỏ qua input/AI ở frame này
        if (isKnockbackActive()) {
            emo.applyKnockback(this);
        } else {
            if (controller != null) {
                controller.decide(this);
                emo.moveByDirection(this);
            } else {
                int[] d = computeDelta();
                emo.moveWithDelta(this, d[0], d[1]);
            }
        }

        CombatSystem.tick(this);     // (giữ như cũ)
        StatusSystem.update(this);   // chỉ timers (i-frames…), KHÔNG translate
        esm.updateSprite(this);
    }


    public void draw(Graphics2D g2) {
        ed.draw(g2, this);
    }

    public BufferedImage setup(String path, int w, int h) {
        return esm.setup(path, w, h);
    }

    public void onDamaged(int damage) {
    }

    public void applyKnockback(int kbX, int kbY, int durationFrames) {
        velX = kbX;
        velY = kbY;
        knockbackCounter = durationFrames;
    }

    // --- Save/Load support ---

    public void revive() {
        this.hp = Math.max(1, maxHp); // hồi sinh với full máu
    }

    // === Dialogue Support ===
    public void facePlayer() {
        if (gp == null || gp.em == null) return;
        var player = gp.em.getPlayer();
        if (player == null) return;
        switch (player.direction) {
            case "up" -> direction = "down";
            case "down" -> direction = "up";
            case "left" -> direction = "right";
            case "right" -> direction = "left";
        }
    }

    public void speak(GamePanel gp) {
        // Each NPC subclass overrides this to start its dialogue
    }

}