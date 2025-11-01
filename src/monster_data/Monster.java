package monster_data;

import combat.CombatSystem;
import entity.Entity;
import main.GamePanel;
import player_manager.Player;
import java.awt.Rectangle;

public abstract class Monster extends Entity {

    // config combat
    protected int attackDamage         = 1;
    protected int attackKnockback      = 6;
    protected int attackTriggerRadius  = 36;
    protected int faceLockThreshold    = 4;
    protected int atkW, atkH;

    public Monster(GamePanel gp) {
        super(gp);

        this.combat.setAttackBoxSize(gp.tileSize, gp.tileSize * 3 / 2);
        this.combat.setTimingFrames(8, 6, 10, 30) ;
        this.attackTriggerRadius = Math.max(gp.tileSize, 48);
        this.faceLockThreshold = 6;
        this.atkW = gp.tileSize;
        this.atkH = gp.tileSize * 3 / 2;
        this.combat.setAttackBoxSize(atkW, atkH);
    }


    @Override
    public void update() {
        decideAttack();
        int preX = worldX, preY = worldY;
        boolean hold = CombatSystem.isAttacking(combat) ;
        super.update();
        if (hold) { worldX = preX; worldY = preY; }
    }

    protected void decideAttack() {
        // 0) Lấy player an toàn
        Player p = (gp.em != null ? gp.em.getPlayer() : null);
        if (p == null || p.isDead()) return;

        // 1) Tính body rect của quái & player (world space)
        Rectangle meBody = getSolidAreaWorld();
        Rectangle plBody = getSolidAreaWorld(p);

        // Player “béo” hơn 1 chút để dễ trúng (tránh hụt vì lẻ pixel)
        Rectangle plFat = new Rectangle(plBody);
        plFat.grow(2, 2);

        // 2) Nếu đã chạm thân -> đánh ngay
        if (meBody.intersects(plFat)) {
            if (CombatSystem.canStartAttack(this.combat)) {
                // Không đổi hướng khi đang giữa pha; chỉ face khi bắt đầu
                faceOnceToward(p);
                CombatSystem.startAttack(this.combat, this);
                this.combat.clearHitThisSwing();
            }
            return;
        }

        // 3) Reach-rectangle: kéo ô meBody theo hướng đang nhìn
        //    => cho phép “đánh tầm ngắn” khi đứng sát trước mặt
        final int rw = (atkW > 0 ? atkW : gp.tileSize);
        final int rh = (atkH > 0 ? atkH : gp.tileSize);

        Rectangle reach = new Rectangle(meBody);
        switch (this.direction) {
            case "up":
                reach.y      -= rh;
                reach.height += rh;
                break;
            case "down":
                reach.height += rh;
                break;
            case "left":
                reach.x     -= rw;
                reach.width += rw;
                break;
            default: // "right"
                reach.width += rw;
                break;
        }

        // 4) Nếu player ở trong tầm reach -> bắt đầu đòn (nếu hết CD)
        if (reach.intersects(plFat)) {
            if (CombatSystem.canStartAttack(this.combat)) {
                faceOnceToward(p); // chỉ set hướng 1 lần tại thời điểm bắt đầu
                CombatSystem.startAttack(this.combat, this);
                this.combat.clearHitThisSwing();
            }
        }
    }

    // === Helper: chỉ xoay mặt 1 lần khi bắt đầu đòn ===
    private void faceOnceToward(Player p) {
        int dx = p.worldX - this.worldX;
        int dy = p.worldY - this.worldY;
        if (Math.abs(dx) > Math.abs(dy)) {
            this.direction = (dx >= 0) ? "right" : "left";
        } else {
            this.direction = (dy >= 0) ? "down" : "up";
        }
    }

    // === Đổi sang world-rect tiện dùng ===
    protected Rectangle getSolidAreaWorld() {
        Rectangle sa = this.getSolidArea();
        return new Rectangle(this.worldX + sa.x, this.worldY + sa.y, sa.width, sa.height);
    }
    protected static Rectangle getSolidAreaWorld(Entity e) {
        Rectangle sa = e.getSolidArea();
        return new Rectangle(e.worldX + sa.x, e.worldY + sa.y, sa.width, sa.height);
    }

    @Override
    public void onDamaged(int damage) { }
    public void onDeath() { }
}