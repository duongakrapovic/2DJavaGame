package combat;

import entity.Entity;
import java.awt.Rectangle;

public final class CombatSystem {
    private CombatSystem() {}

    // --------- API công khai (đọc trạng thái) ----------
    public static boolean isAttacking(CombatComponent cc)   { return cc.isAttacking(); }
    public static int     getPhase(CombatComponent cc)      { return cc.getAttackPhase(); }
    public static boolean isAttackActive(CombatComponent cc) { return cc.isAttackActive(); }

    public static boolean canStartAttack(CombatComponent cc) {
        return !cc.isAttacking() && cc.getCooldownCounterFrames() == 0;
    }

    public static void startAttack(CombatComponent cc, CombatContext owner) {
        // Nếu đã chết thì không cho bắt đầu
        if (owner.isDead()) return; // << thêm
        cc.setIsAttacking(true);
        cc.setAttackPhaseInternal(1); // windup
        cc.setPhaseTimerFrames(cc.getWindupFrames());
        updateAttackBoxToOwnerFacing(cc, owner);
    }

    public static void update(CombatComponent cc, CombatContext owner) {
        // Nếu owner chết: tắt hẳn trạng thái tấn công và hitbox
        if (owner.isDead()) { // << thêm
            cc.setIsAttacking(false);
            cc.setAttackPhaseInternal(0);
            cc.setCooldownCounterFrames(0);
            cc.getAttackBox().setBounds(0, 0, 0, 0);
            return;
        }

        // hồi chiêu
        if (cc.getCooldownCounterFrames() > 0) {
            cc.setCooldownCounterFrames(Math.max(0, cc.getCooldownCounterFrames() - 1)); // << clamp
        }

        if (!cc.isAttacking()) return;

        // đếm lùi phase
        cc.setPhaseTimerFrames(Math.max(0, cc.getPhaseTimerFrames() - 1)); // << clamp
        if (cc.getPhaseTimerFrames() > 0) return;

        int phase = cc.getAttackPhase();
        if (phase == 1) { // Windup -> Active
            cc.setAttackPhaseInternal(2);
            cc.setPhaseTimerFrames(cc.getActiveFrames());
            updateAttackBoxToOwnerFacing(cc, owner);
        } else if (phase == 2) { // Active -> Recover
            cc.setAttackPhaseInternal(3);
            cc.setPhaseTimerFrames(cc.getRecoverFrames());
            cc.getAttackBox().setBounds(0, 0, 0, 0); // clear khi sang recover
        } else { // Recover -> End
            cc.setIsAttacking(false);
            cc.setAttackPhaseInternal(0);
            cc.setCooldownCounterFrames(cc.getCooldownFrames());
            cc.getAttackBox().setBounds(0, 0, 0, 0); // << đảm bảo clear
        }
    }

    public static void updateAttackBoxToOwnerFacing(CombatComponent cc, CombatContext owner) {
        Rectangle body = owner.getSolidArea();
        int boxX = owner.getWorldX() + body.x;
        int boxY = owner.getWorldY() + body.y;

        switch (owner.getDirection()) {
            case "up":    boxY -= cc.getAttackHeight(); break;
            case "down":  boxY += body.height;          break;
            case "left":  boxX -= cc.getAttackWidth();  break;
            default:      boxX += body.width;           break; // "right" hoặc null
        }
        cc.getAttackBox().setBounds(boxX, boxY, cc.getAttackWidth(), cc.getAttackHeight());
    }

    public static void markHitLanded(CombatComponent cc, Object target) {
        // chỗ này bạn có thể lưu Set<Object> nếu muốn chống multi-hit
    }

    /** Cập nhật i-frame & knockback mỗi frame. */
    public static void updateStatus(Entity e) {
        if (e.isInvulnerable()) {
            int c = e.getInvulnCounter() - 1;
            e.setInvulnCounter(Math.max(0, c));                 // << clamp
            if (c <= 0) e.setInvulnerable(false);
        }
        if (e.getKnockbackCounter() > 0) {
            e.worldX += e.velX;
            e.worldY += e.velY;
            int k = e.getKnockbackCounter() - 1;
            e.setKnockbackCounter(Math.max(0, k));              // << clamp
            if (k <= 0) { e.velX = 0; e.velY = 0; }
        }
    }

    /** Tick tổng hợp: combat phase + status (i-frame/knockback). */
    public static void tick(Entity e) {
        update(e.combat, e);
        updateStatus(e);
    }
}
