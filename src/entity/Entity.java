package entity;

import combat.CombatComponent;
import combat.CombatContext;
import combat.CombatSystem;
import combat.DamageProcessor;   // <- dùng để delegate takeDamage
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

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
    public String direction = "down";
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int actionLockCounter = 0;

    // --- collision ---
    public Rectangle solidArea;
    public boolean collisionXOn = false;
    public boolean collisionYOn = false;
    public boolean collisionOn  = false;
    public boolean collision    = false;
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

    // --- knockback ---
    public int velX = 0, velY = 0;
    private int knockbackCounter = 0;
    private int knockbackFrames  = 10; // ~0.16s @60fps

    // --- attack box (shared with CombatComponent) ---
    public Rectangle attackBox = new Rectangle(0, 0, 0, 0);

    // --- systems/manager ---
    protected final GamePanel gp;
    protected final EntityMovement emo;
    protected final EntitySpriteManager esm;
    protected final EntityDraw ed;
    public int mapIndex = 0;

    // --- Combat ECS ---
    public final CombatComponent combat;

    public Entity(GamePanel gp) {
        this.gp = gp;
        this.screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        this.screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        this.emo = new EntityMovement(gp);
        this.esm = new EntitySpriteManager();
        this.ed  = new EntityDraw(gp);

        this.combat = new CombatComponent();
        this.attackBox = combat.getAttackBox(); // dùng chung rect để code vẽ cũ không phải đổi
    }

    // -------- stats ----------
    public void setStats(int maxHp, int atk, int def) {
        this.maxHp = Math.max(1, maxHp);
        this.hp    = this.maxHp;
        this.atk   = Math.max(0, atk);
        this.def   = Math.max(0, def);
    }
    public int  getHP()     { return hp; }
    public int  getMaxHP()  { return maxHp; }
    public int  getATK()    { return atk; }
    public int  getDEF()    { return def; }

    // Cho hệ thống giảm HP
    public void reduceHP(int amount) { hp = Math.max(0, hp - Math.max(0, amount)); }

    // i-frame config
    public void setInvulnFrames(int frames) { invulnFrames = Math.max(0, frames); }
    public int  getInvulnFrames() { return invulnFrames; }

    // trạng thái sống/chết
    @Override public boolean isDead() { return hp <= 0; }

    // i-frame state (để CombatSystem cập nhật)
    public boolean isInvulnerable()           { return invulnerable; }
    public void    setInvulnerable(boolean v) { invulnerable = v; }
    public int     getInvulnCounter()         { return invulnCounter; }
    public void    setInvulnCounter(int v)    { invulnCounter = v; }

    // knockback API (để CombatSystem cập nhật)
    public void applyKnockback(int kbX, int kbY, int durationFrames) {
        velX = kbX; velY = kbY; knockbackCounter = durationFrames;
    }
    public int  getKnockbackCounter()      { return knockbackCounter; }
    public void setKnockbackCounter(int v) { knockbackCounter = v; }
    public int  getKnockbackFrames()       { return knockbackFrames; }
    public boolean isKnockbackActive()     { return knockbackCounter > 0; }

    // -------- game tick ----------
    public void update() {
        if (!isKnockbackActive()) {
            emo.setAction(this);
            emo.move(this);
        }

        // Combat phase + i-frame + knockback (gói gọn trong CombatSystem)
        CombatSystem.tick(this);

        // sprite
        esm.updateSprite(this);
    }

    public void draw(Graphics2D g2) { ed.draw(g2, this); }
    public BufferedImage setup(String path, int w, int h) { return esm.setup(path, w, h); }

    // -------- nhận sát thương: chỉ delegate sang hệ thống ----------
    public void takeDamage(int rawDamage, int knockbackX, int knockbackY) {
        DamageProcessor.applyDamage(this, rawDamage, knockbackX, knockbackY);
    }

    // Hook cho hiệu ứng riêng khi dính đòn (override ở Player/Monster nếu cần)
    public void onDamaged(int damage) {}

    // ===== CombatContext implementation =====
    @Override public int getWorldX() { return worldX; }
    @Override public int getWorldY() { return worldY; }
    @Override public Rectangle getSolidArea() {
        return (solidArea != null)
                ? solidArea
                : new Rectangle(0, 0, Math.max(1, width), Math.max(1, height));
    }
    @Override public String getDirection() { return direction; }
}
