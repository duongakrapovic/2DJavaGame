package entity;

import combat.CombatSystem;
import main.GamePanel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class EntityDraw {
    private final GamePanel gp;
    public EntityDraw(GamePanel gp){
        this.gp = gp;
    }

    public void draw(Graphics2D g2, Entity e) {
        var player = gp.em.getPlayer();
        final int camOriginX = player.worldX - player.screenX;
        final int camOriginY = player.worldY - player.screenY;

        final int screenX = e.worldX - camOriginX;
        final int screenY = e.worldY - camOriginY;

        boolean visible =
                e.worldX + gp.tileSize > player.worldX - player.screenX &&
                        e.worldX - gp.tileSize < player.worldX + player.screenX &&
                        e.worldY + gp.tileSize > player.worldY - player.screenY &&
                        e.worldY - gp.tileSize < player.worldY + player.screenY;
        if (!visible) return;

        boolean skipSprite = false;
        if (e.isInvulnerable()) {
            int fc = gp.frameCounter;
            skipSprite = ((fc / 6) % 2 != 0);
        }

        BufferedImage image = null;
        boolean attacking = CombatSystem.isAttacking(e.combat);

        if (e.animationON) {
            if (attacking) {
                switch (e.direction) {
                    case "up":    image = (e.spriteNum == 1 ? nz(e.atkUp1,    e.up1)   : nz(e.atkUp2,    e.up2));    break;
                    case "down":  image = (e.spriteNum == 1 ? nz(e.atkDown1,  e.down1) : nz(e.atkDown2,  e.down2));  break;
                    case "left":  image = (e.spriteNum == 1 ? nz(e.atkLeft1,  e.left1) : nz(e.atkLeft2,  e.left2));  break;
                    default:      image = (e.spriteNum == 1 ? nz(e.atkRight1, e.right1): nz(e.atkRight2, e.right2)); break;
                }
            } else {
                switch (e.direction) {
                    case "up":    image = (e.spriteNum == 1 ? e.up1    : e.up2);    break;
                    case "down":  image = (e.spriteNum == 1 ? e.down1  : e.down2);  break;
                    case "left":  image = (e.spriteNum == 1 ? e.left1  : e.left2);  break;
                    default:      image = (e.spriteNum == 1 ? e.right1 : e.right2); break;
                }
            }
        } else {
            image = e.staticImage;
        }
        if (image == null) image = nz(e.down1, e.staticImage);

        if (!skipSprite) {
            g2.drawImage(image, screenX, screenY, e.width, e.height, null);
        }

        if (e.solidArea != null) {
            g2.setColor(Color.RED);
            g2.drawRect(screenX + e.solidArea.x, screenY + e.solidArea.y, e.solidArea.width, e.solidArea.height);
        }

        if (CombatSystem.isAttackActive(e.combat)) {
            Rectangle atk = e.combat.getAttackBox();
            if (atk != null && atk.width > 0 && atk.height > 0) {
                int ax = atk.x - camOriginX;
                int ay = atk.y - camOriginY;

                g2.setColor(new Color(255, 32, 32, 140));
                g2.fillRect(ax, ay, atk.width, atk.height);
                g2.setColor(Color.RED);
                g2.drawRect(ax, ay, atk.width, atk.height);
            }
        }
    }

    private static BufferedImage nz(BufferedImage a, BufferedImage b) {
        return a != null ? a : b;
    }
}
