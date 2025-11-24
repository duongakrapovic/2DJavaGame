package entity;

import combat.CombatSystem;
import main.GamePanel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class EntityDraw {
    private final GamePanel gp;

    public EntityDraw(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2, Entity e) {
        // Bảo vệ NPE nếu chưa có player / em
        if (gp.em == null || gp.em.getPlayer() == null) {
            return;
        }

        var player = gp.em.getPlayer();

        // Gốc camera (world → screen)
        final int camOriginX = player.worldX - player.screenX;
        final int camOriginY = player.worldY - player.screenY;

        final int screenX = e.worldX - camOriginX;
        final int screenY = e.worldY - camOriginY;

        // Culling: ngoài vùng camera thì không vẽ
        boolean visible =
                e.worldX + gp.tileSize > player.worldX - player.screenX &&
                        e.worldX - gp.tileSize < player.worldX + player.screenX &&
                        e.worldY + gp.tileSize > player.worldY - player.screenY &&
                        e.worldY - gp.tileSize < player.worldY + player.screenY;

        if (!visible) return;

        // Nháy sprite khi invulnerable
        boolean skipSprite = false;
        if (e.isInvulnerable()) {
            int fc = gp.frameCounter;
            skipSprite = ((fc / 6) % 2 != 0);
        }

        // Trạng thái combat
        boolean attacking = CombatSystem.isAttacking(e.combat);

        // 🔒 Hướng dùng cho animation:
        // - Nếu đang tấn công: ưu tiên attackDir (đã lock khi bắt đầu đòn)
        // - Nếu không: dùng direction bình thường
        String dirForAnim = e.direction;
        if (attacking && e.attackDir != null && !e.attackDir.isEmpty()) {
            dirForAnim = e.attackDir;
        }

        BufferedImage image = null;

        // ================= CHỌN SPRITE =================
        if (e.animationON) {
            if (attacking) {
                // VẼ SPRITE ĐÁNH THEO dirForAnim (LOCKED DIRECTION)
                switch (dirForAnim) {
                    case "up":
                        image = (e.spriteNum == 1
                                ? nz(e.atkUp1, e.up1)
                                : nz(e.atkUp2, e.up2));
                        break;
                    case "down":
                        image = (e.spriteNum == 1
                                ? nz(e.atkDown1, e.down1)
                                : nz(e.atkDown2, e.down2));
                        break;
                    case "left":
                        image = (e.spriteNum == 1
                                ? nz(e.atkLeft1, e.left1)
                                : nz(e.atkLeft2, e.left2));
                        break;
                    default: // "right"
                        image = (e.spriteNum == 1
                                ? nz(e.atkRight1, e.right1)
                                : nz(e.atkRight2, e.right2));
                        break;
                }
            } else {
                // VẼ SPRITE WALK/IDLE THEO dirForAnim
                switch (dirForAnim) {
                    case "up":
                        image = (e.spriteNum == 1 ? e.up1 : e.up2);
                        break;
                    case "down":
                        image = (e.spriteNum == 1 ? e.down1 : e.down2);
                        break;
                    case "left":
                        image = (e.spriteNum == 1 ? e.left1 : e.left2);
                        break;
                    default: // "right"
                        image = (e.spriteNum == 1 ? e.right1 : e.right2);
                        break;
                }
            }
        } else {
            image = e.staticImage;
        }

        if (image == null) {
            image = nz(e.down1, e.staticImage);
        }

        // ================= VẼ SPRITE =================
        if (!skipSprite) {
            int tempX = screenX;
            int tempY = screenY;

            // offset 1.5 tile cho hướng UP
            final int offUp15 = (2 * gp.tileSize) / 3;  // ~1.5 * tileSize
            final int offLeft = gp.tileSize;            // 1 tile sang trái

            // Chỉ offset khi có attackAnim và đang tấn công
            if (attacking && e.hasAttackAnim) {
                if ("up".equals(dirForAnim))   tempY -= offUp15;
                if ("left".equals(dirForAnim)) tempX -= offLeft;
            }

            // Nếu có attackAnim: vẽ đúng kích thước frame attack
            final boolean useImageSize = attacking && e.hasAttackAnim;
            int drawW = useImageSize ? image.getWidth()  : e.width;
            int drawH = useImageSize ? image.getHeight() : e.height;

            int drawX = tempX;
            int drawY = tempY;

            g2.drawImage(image, drawX, drawY, drawW, drawH, null);
        }

        // ================= DEBUG: SOLID AREA =================
//        if (e.solidArea != null) {
//            g2.setColor(Color.RED);
//            g2.drawRect(
//                    screenX + e.solidArea.x,
//                    screenY + e.solidArea.y,
//                    e.solidArea.width,
//                    e.solidArea.height
//            );
//        }

        // ================= DEBUG: ATTACK BOX ACTIVE =================
//        if (CombatSystem.isAttackActive(e.combat)) {
//            Rectangle atk = e.combat.getAttackBox();
//            if (atk != null && atk.width > 0 && atk.height > 0) {
//                int ax = atk.x - camOriginX;
//                int ay = atk.y - camOriginY;
//
//                g2.setColor(new Color(255, 32, 32, 140));
//                g2.fillRect(ax, ay, atk.width, atk.height);
//                g2.setColor(Color.RED);
//                g2.drawRect(ax, ay, atk.width, atk.height);
//            }
//        }
    }
    private static BufferedImage nz(BufferedImage a, BufferedImage b) {
        return a != null ? a : b;
    }
}
