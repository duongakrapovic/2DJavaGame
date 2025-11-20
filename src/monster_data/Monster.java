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
    public int homeX, homeY; // toạ độ “nhà” để leash + wander quanh

    // --- EXP config ---
    protected int expReward = 1;   // quái này cho bao nhiêu EXP khi chết

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
    // Getter/Setter EXP
    public int getExpReward() {
        return expReward;
    }

    public void setExpReward(int expReward) {
        this.expReward = Math.max(0, expReward);
    }

    // (optional) auto tính exp theo chỉ số quái
    protected void initExpFromStats() {
        int base = (int) (
                getMaxHP() * 0.1 +
                        getATK() * 1.5 +
                        getDEF() * 0.5
        );
        this.expReward = Math.max(1, base);
    }
    @Override
    public void update() {
        // 1) Quyết định có bắt đầu attack không (nếu đang attack thì thôi)
        decideAttack();

        // 2) Giữ vị trí nếu đang attack
        int preX = worldX, preY = worldY;
        boolean holdPos = CombatSystem.isAttacking(combat);

        super.update();

        if (holdPos) {
            worldX = preX;
            worldY = preY;
        }
    }
    public void setHome(int x, int y) {
        this.homeX = x;
        this.homeY = y;
    }
    protected void decideAttack() {
        // Không cho spam: nếu CombatSystem đang trong 1 đòn thì bỏ qua
        if (CombatSystem.isAttacking(this.combat)) return;

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
            tryStartAttackOn(p);
            return;
        }

        // 3) Reach-rectangle: kéo ô meBody theo hướng đang nhìn
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

        // 4) Nếu player ở trong tầm reach -> bắt đầu đòn
        if (reach.intersects(plFat)) {
            tryStartAttackOn(p);
        }
    }

    protected void tryStartAttackOn(Player p) {
        // 1) CombatSystem phải cho phép (cooldown, state…)
        if (!CombatSystem.canStartAttack(this.combat)) return;

        // 2) Xoay mặt 1 lần về phía player
        faceOnceToward(p);

        // 3) LOCK HƯỚNG ĐÁNH: chụp lại hướng tại thời điểm này
        this.attackDir = this.direction;

        // 4) Bắt đầu đòn đánh
        CombatSystem.startAttack(this.combat, this);
        this.combat.clearHitThisSwing();
    }

    private void faceOnceToward(Player p) {
        int dx = p.worldX - this.worldX;
        int dy = p.worldY - this.worldY;
        if (Math.abs(dx) > Math.abs(dy)) {
            this.direction = (dx >= 0) ? "right" : "left";
        } else {
            this.direction = (dy >= 0) ? "down" : "up";
        }
    }

    protected Rectangle getSolidAreaWorld() {
        Rectangle sa = this.getSolidArea();
        return new Rectangle(this.worldX + sa.x, this.worldY + sa.y, sa.width, sa.height);
    }
    protected static Rectangle getSolidAreaWorld(Entity e) {
        Rectangle sa = e.getSolidArea();
        return new Rectangle(e.worldX + sa.x, e.worldY + sa.y, sa.width, sa.height);
    }
    public void onDeath() {
        // 1) EXP
        Player p = (gp != null && gp.em != null) ? gp.em.getPlayer() : null;
        if (p != null) {
            int expGain = getExpReward();
            System.out.println("[EXP] Thưởng cho player: +" + expGain + " EXP từ " + name);
            p.gainExp(expGain);
        } else {
            System.out.println("[EXP] Không tìm thấy player để cộng EXP");
        }

        // 2) 25% drop
        double roll = Math.random();
        System.out.println("[DROP] Roll = " + roll);
        if (roll < 0.25) {
            System.out.println("[DROP] => Rơi HealthPosion");
            spawnHealthPosionDrop();
        } else {
            System.out.println("[DROP] => Không rơi gì");
        }
    }

    private void spawnHealthPosionDrop() {
        if (gp == null || gp.om == null) return;

        gp.om.spawnHealthPosion(this.mapIndex, this.worldX, this.worldY);
    }

    public void reduceHP(int amount) {
        boolean wasDead = isDead();
        System.out.println("[DMG] " + name + " nhận " + amount + " dmg (hp=" + getHP() + ")");

        super.reduceHP(amount);

        System.out.println("[DMG] " + name + " sau khi trừ hp=" + getHP());

        if (!wasDead && isDead()) {
            System.out.println("[DEATH] " + name + " vừa chết");
            onDeath();
        }
    }
}
