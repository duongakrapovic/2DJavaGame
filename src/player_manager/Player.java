/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package player_manager;

import entity.Entity;
import entity.EntityCombat;
import interact_manager.Interact;
import input_manager.InputController;
import main.GamePanel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

/**
 * Player.java
 * Represents the main player character in the game.
 * Handles input, movement, collision, animation, and interactions with objects, NPCs, and monsters.
 */
public class Player extends Entity{
    
    public int hasKey = 0; // Number of keys currently held
    int speedTimer = 0; // Countdown timer for speed buff;
    
    public Interact iR;// Interaction manager
    public final InputController input;// Input handler for movement
    private final PlayerSpriteManager psm;
    private final PlayerMovement pm;
    private final PlayerAnimation pa;
    //combat
    private int attackBtnLock = 0; // chống spam phím

    /**
     * Constructor: initializes player with input, position, collision box, and images.
     * @param gp Reference to the GamePanel
     * @param input Input controller for handling player input
     */
    public Player(GamePanel gp , InputController input ){
        super(gp);
        this.iR = new Interact(gp, this, input);
        this.input = input;

        // Initialize collision box
        solidArea = new Rectangle(11, 16, 25,25);
        solidAreaDefaultX =  solidArea.x;
        solidAreaDefaultY =  solidArea.y;
        
        setDefaultValues();
        
        // Init managers
        psm = new PlayerSpriteManager(gp);
        psm.loadSprites(this);
        pm = new PlayerMovement(this, gp);
        pa = new PlayerAnimation(this);
        //combat

        combat.attackWidth  = 36;
        combat.attackHeight = 36;
        combat.attackWindup = 4;
        combat.attackActive = 6;
        combat.attackRecover= 8;
        combat.attackCooldown = 10;
        setStats(10, 3, 1);

    }
    /** Set default values for position, speed, direction, and animation */
    public void setDefaultValues(){
        // central of the chunk 
        worldX = gp.tileSize * gp.chunkSize / 2;
        worldY = gp.tileSize * gp.chunkSize / 2;
        defaultSpeed = 15;
        buffSpeed = 4;
        actualSpeed = defaultSpeed;
        direction = "down";
        animationON = true;
        //System.out.println("complete setdefault");
    }
    
    /** Update player's state, movement, collision, and interactions */
    @Override
    public void update(){
        int[] delta = pm.calculateMovement();
        pm.move(delta[0], delta[1]);
        pa.update(pm.isMoving() ,combat.isAttacking(), combat.getAttackPhase());

        // Speed buff timer
        if(speedTimer > 0) speedTimer--;
        if(speedTimer == 0) actualSpeed = defaultSpeed;
        // combat
        handleAttackInput();
        combat.update();        // phase tấn công
        updateCombatTick();     // knockback + i-frame
    }
    
    /** Draw the player on the screen with proper sprite */
    @Override
    public void draw(Graphics2D g2){
//      g2.setColor(Color.white);// color to draw objects
//      g2.fillRect(x , y, gp.tileSize , gp.tileSize);
        if (isInvulnerable()) {
            int fc = gp.frameCounter;                  // nhớ có biến frameCounter trong GamePanel
            boolean visible = (fc / 6) % 2 == 0;       // nháy 6 frame
            if (!visible) return;                      // bỏ qua vẽ sprite ở frame này
        }
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;
        boolean attacking = combat.isAttacking();
        if (attacking) {
            switch (direction) {
                case "up":    image = (spriteNum == 1 ? atkUp1    : atkUp2); tempScreenY -= gp.tileSize ;   break;
                case "down":  image = (spriteNum == 1 ? atkDown1  : atkDown2); break;
                case "left":  image = (spriteNum == 1 ? atkLeft1  : atkLeft2); tempScreenX -= gp.tileSize ; break;
                default:      image = (spriteNum == 1 ? atkRight1 : atkRight2); break;
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
        g2.setColor(Color.red);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        //check attackbox

        if (combat.isAttackActive() && attackBox.width > 0 && attackBox.height > 0) {
            // quy đổi world → screen
            int ax = attackBox.x - (worldX - screenX);
            int ay = attackBox.y - (worldY - screenY);

            g2.fillRect(ax, ay, attackBox.width, attackBox.height);
            g2.setColor(Color.red);
            g2.drawRect(ax, ay, attackBox.width, attackBox.height);
        }
    }
    private void handleAttackInput() {
        if (attackBtnLock > 0) attackBtnLock--;

        if (input.isAttackPressed() && attackBtnLock == 0) {
            attackBtnLock = 6; // 6 frame khóa phím (~0.1s ở 60fps)
            if (combat.canStartAttack()) combat.startAttack(); System.out.println("Combat started");
        }
    }

}
