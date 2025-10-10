package combat;

import java.awt.Rectangle;

/** Điều khiển vòng đời đòn đánh, cập nhật attackBox theo hướng – tên rõ ràng. */
public final class CombatSystem {
    private CombatSystem() {}

    // --------- API công khai (đọc trạng thái) ----------
    public static boolean isAttacking(CombatComponent cc)    { return cc.isAttacking(); }
    public static int     getPhase(CombatComponent cc)        { return cc.getAttackPhase(); }
    public static boolean isAttackActive(CombatComponent cc)  { return cc.isAttackActive(); }

    /** Có thể bắt đầu đòn mới không (đang không tấn công và hết hồi chiêu). */
    public static boolean canStartAttack(CombatComponent cc) {
        return !cc.isAttacking() && cc.getCooldownCounterFrames() == 0;
    }

    /** Bắt đầu đòn đánh: chuyển sang pha Windup. */
    public static void startAttack(CombatComponent cc, CombatContext owner) {
        cc.setIsAttacking(true);
        cc.setAttackPhaseInternal(1); // windup
        cc.setPhaseTimerFrames(cc.getWindupFrames());
        updateAttackBoxToOwnerFacing(cc, owner); // đặt hitbox theo hướng hiện tại
    }

    /** Gọi mỗi frame để cập nhật tiến trình đòn. */
    public static void update(CombatComponent cc, CombatContext owner) {
        // hồi chiêu (nếu có)
        if (cc.getCooldownCounterFrames() > 0) {
            cc.setCooldownCounterFrames(cc.getCooldownCounterFrames() - 1);
        }

        if (!cc.isAttacking()) return;

        // đếm lùi phase hiện tại
        cc.setPhaseTimerFrames(cc.getPhaseTimerFrames() - 1);
        if (cc.getPhaseTimerFrames() > 0) return;

        // hết timer cho phase -> chuyển phase
        int phase = cc.getAttackPhase();
        if (phase == 1) { // Windup -> Active
            cc.setAttackPhaseInternal(2);
            cc.setPhaseTimerFrames(cc.getActiveFrames());
            updateAttackBoxToOwnerFacing(cc, owner);
        } else if (phase == 2) { // Active -> Recover
            cc.setAttackPhaseInternal(3);
            cc.setPhaseTimerFrames(cc.getRecoverFrames());
            // tắt hitbox khi vào recover
            cc.getAttackBox().setBounds(0, 0, 0, 0);
        } else { // Recover -> End
            cc.setIsAttacking(false);
            cc.setAttackPhaseInternal(0);
            cc.setCooldownCounterFrames(cc.getCooldownFrames());
        }
    }

    /** Tính lại attackBox dựa trên hướng và solidArea của owner (tọa độ world). */
    public static void updateAttackBoxToOwnerFacing(CombatComponent cc, CombatContext owner) {
        Rectangle body = owner.getSolidArea();
        int boxX = owner.getWorldX() + body.x;
        int boxY = owner.getWorldY() + body.y;

        switch (owner.getDirection()) {
            case "up":
                boxY -= cc.getAttackHeight();
                break;
            case "down":
                boxY += body.height;
                break;
            case "left":
                boxX -= cc.getAttackWidth();
                break;
            default: // "right"
                boxX += body.width;
                break;
        }
        cc.getAttackBox().setBounds(boxX, boxY, cc.getAttackWidth(), cc.getAttackHeight());
    }

    /** (Tùy chọn) đánh dấu đã trúng để chống multi-hit trong 1 swing. */
    public static void markHitLanded(CombatComponent cc, Object target) {
        // bạn có thể lưu set<targetId> ở đây nếu cần
    }
}
