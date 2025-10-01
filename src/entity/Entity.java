/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// this stores variables that will be use in player , monster and npc class.
package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    // default setting
    public int worldX, worldY;
    public int width, height;
    public final int screenX;
    public final int screenY;

    // set for animaion
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage atkUp1, atkUp2, atkDown1, atkDown2, atkLeft1, atkLeft2, atkRight1, atkRight2;
    public BufferedImage staticImage;// use for enity without animation
    public String direction = "down";
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int actionLockCounter = 0;

    // set collision auto false
    public Rectangle solidArea;
    public boolean collisionXOn = false;
    public boolean collisionYOn = false;
    public boolean collisionOn = false;
    public boolean collision  = false;

    // set for colision area of entity
    public int solidAreaDefaultX, solidAreaDefaultY;

    //enity state
    public String name;
    public int defaultSpeed, actualSpeed, buffSpeed;
    public boolean animationON = false;

    // default values
    private int hp = 1;
    private int maxHp = 1;
    private int atk = 1;
    private int def = 0;

    // invulnerable frames
    private boolean invulnerable = false;
    private int invulnFrames = 20;// 0.33 ms
    private int invulnCounter = 0 ;

    // knockback
    public int velX = 0, velY = 0;
    private int knockbackCounter = 0;
    private int knockbackFrames = 10; // ~0.16s @60fps

    // attack box
    public Rectangle attackBox = new Rectangle(0,0,0,0);

    //setter and getter
    public int getHP() { return hp; }
    public int getMaxHP() { return maxHp; }
    public int getATK() { return atk; }
    public int getDEF() { return def; }

    public void setInvulnFrames(int frames) { invulnFrames = Math.max(0, frames); }
    public int  getInvulnFrames() { return invulnFrames; }

    public void setStats(int maxHp, int atk, int def) {
        this.maxHp = Math.max(1, maxHp);
        this.hp = this.maxHp;
        this.atk = Math.max(0, atk);
        this.def = Math.max(0, def);
    }

    public void applyKnockback(int kbX, int kbY, int durationFrames) {
        this.velX = kbX;
        this.velY = kbY;
        this.knockbackCounter = durationFrames;
    }
    public boolean isKnockbackActive() {return knockbackCounter > 0;
    }
    public boolean isDead() { return hp <= 0; }
    public boolean isInvulnerable() { return invulnerable; }

    // system
    protected GamePanel gp;
    protected final EntityMovement emo;
    protected final EntitySpriteManager esm;
    protected final EntityDraw ed;
    protected final EntityCombat combat;
    public int mapIndex = 0;
    // constructor
    public Entity(GamePanel gp) {
        this.gp = gp;
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        emo = new EntityMovement(gp);
        esm = new EntitySpriteManager();
        ed  = new EntityDraw(gp);

        combat = new EntityCombat(this);

        // ⭐ RẤT QUAN TRỌNG: dùng chung Rectangle
        this.attackBox = combat.attackBox;
    }
    public EntityCombat getCombat() { return combat; }
    //
// In Entity
    public void update() {
        if (!isKnockbackActive()) {
            emo.setAction(this);
            emo.move(this);
        }
        esm.updateSprite(this);
        updateCombatTick() ;
    }

    public void draw(Graphics2D g2){
        ed.draw(g2, this);
    }
    public BufferedImage setup(String path, int w, int h){
        return esm.setup(path, w, h);
    }
    // combat
    public void takeDamage(int rawDmg, int kbX, int kbY) {
        if (invulnerable || isDead()) return;

        int dmg = Math.max(1, rawDmg - def);
        hp = Math.max(0, hp - dmg);

        // i-frame
        invulnerable  = true;
        invulnCounter = invulnFrames;

        // knockback
        applyKnockback(kbX, kbY, knockbackFrames);

        onDamaged(dmg); // hook
    }

    // optional: override in Player/Monster for sfx/flash/drop
    protected void onDamaged(int dmg) {}

    public void updateCombatTick() {
        // i-frame
        if (invulnerable && --invulnCounter <= 0) invulnerable = false;

        // knockback motion
        if (knockbackCounter > 0) {
            worldX += velX;
            worldY += velY;
            if (--knockbackCounter <= 0) { velX = 0; velY = 0; }
        }
    }
}
