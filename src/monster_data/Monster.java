package monster_data;

import combat.CombatSystem;
import entity.Entity;
import main.GamePanel;
import player_manager.Player;

import java.awt.Rectangle;

/** Base cho quái: tấn công rời rạc như player (CombatSystem). Không có contact damage. */
public abstract class Monster extends Entity {

    // --- Cấu hình đòn tấn công ---
    protected int attackDamage         = 1;   // damage mỗi đòn
    protected int attackKnockback      = 6;   // px/frame đẩy player khi trúng
    protected int attackTriggerRadius  = 36;  // px: vào phạm vi này sẽ ra đòn
    protected int faceLockThreshold    = 4;   // px: ưu tiên trục có lệch lớn hơn khi quay mặt
    protected int atkW, atkH; // kích thước reach & attackBox mặc định

    public Monster(GamePanel gp) {
        super(gp);

        this.combat.setAttackBoxSize(gp.tileSize, gp.tileSize * 3 / 2);
        this.combat.setTimingFrames(8, 6, 10, 30) ;
        this.attackTriggerRadius = Math.max(gp.tileSize, 48);
        this.faceLockThreshold = 6;

        // QUAN TRỌNG: tránh spawn với cooldown > 0
        this.atkW = gp.tileSize;
        this.atkH = gp.tileSize * 3 / 2;  // tỉ lệ 3/2 bạn muốn
        this.combat.setAttackBoxSize(atkW, atkH);
    }


    @Override
    public void update() {
        decideAttack();                 // ngắm & quyết định sớm
        int preX = worldX, preY = worldY;
        boolean hold = CombatSystem.isAttacking(combat) || /* vùng giữ vị trí */ false;
        super.update();                 // có CombatSystem.tick(this)
        if (hold) { worldX = preX; worldY = preY; }  // “đứng thế” khi vung
    }


    /** Khi player vào tầm: quay mặt đúng hướng và khởi phát đòn mới (nếu sẵn sàng). */
    protected void decideAttack() {
        Player p = gp.em.getPlayer();
        if (p == null || p.isDead()) return;

        final int px = p.worldX, py = p.worldY;
        final int dx = px - this.worldX, dy = py - this.worldY;

        // 1) Chọn hướng ổn định (ưu tiên trục chênh lệch lớn hơn + ngưỡng "nêm")
        final int absDx = Math.abs(dx), absDy = Math.abs(dy);
        if (absDx - absDy > faceLockThreshold) {
            this.direction = (dx >= 0) ? "right" : "left";
        } else if (absDy - absDx > faceLockThreshold) {
            this.direction = (dy >= 0) ? "down" : "up";
        } else {
            if ("left".equals(direction) || "right".equals(direction)) {
                this.direction = (dx >= 0) ? "right" : "left";
            } else {
                this.direction = (dy >= 0) ? "down" : "up";
            }
        }

        // 2) Lấy thân ở WORLD
        Rectangle meBody = getSolidAreaWorld();
        Rectangle plBody = getSolidAreaWorld(p);

        // Nới nhẹ để đỡ hụt 1–2 px
        Rectangle plFat = new Rectangle(plBody);
        plFat.grow(2, 2);

        // 3) Nếu đã CHẠM THÂN -> đánh ngay (bỏ qua reach) khi hết cooldown
        if (meBody.intersects(plFat)) {
            if (CombatSystem.canStartAttack(this.combat)) {
                CombatSystem.startAttack(this.combat, this);
                this.combat.clearHitThisSwing();
            }
            return;
        }

        // 4) REACH-RECTANGLE: mở rộng thân theo hướng đang nhìn bằng kích thước đòn
        Rectangle reach = new Rectangle(meBody);
        final int rw = (atkW > 0 ? atkW : gp.tileSize);
        final int rh = (atkH > 0 ? atkH : gp.tileSize);

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

        // 5) Nếu player lọt vào REACH -> bắt đầu đòn (nếu sẵn sàng)
        if (reach.intersects(plFat)) {
            if (CombatSystem.canStartAttack(this.combat)) {
                CombatSystem.startAttack(this.combat, this);
                this.combat.clearHitThisSwing();
            }
            return;
        }

        // (Tuỳ chọn) Fallback theo bán kính tâm để đỡ “khó tính” quá
        long d2 = (long) dx * dx + (long) dy * dy;
        long r  = (long) Math.max(attackTriggerRadius, gp.tileSize);
        if (d2 <= r * r && CombatSystem.canStartAttack(this.combat)) {
            CombatSystem.startAttack(this.combat, this);
            this.combat.clearHitThisSwing();
        }
    }




    // ===== Helpers: solidArea theo toạ độ thế giới =====
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
