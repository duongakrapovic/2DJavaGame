package combat;

import java.awt.Rectangle;

public final class AttackPhaseSystem {
    private AttackPhaseSystem() {}

    public static boolean canStart(CombatComponent cc) {
        return !cc.isAttacking() && cc.getCooldownCounterFrames() == 0;
    }

    public static void start(CombatComponent cc, CombatContext owner) {
        if (owner == null || owner.isDead()) return;
        if (cc.getAttackWidth() <= 0 || cc.getAttackHeight() <= 0) return;

        cc.setIsAttacking(true);
        cc.setAttackPhaseInternal(1); // windup
        cc.setPhaseTimerFrames(cc.getWindupFrames());
        alignAttackBox(cc, owner);
        cc.clearHitThisSwing();
    }

    public static void update(CombatComponent cc, CombatContext owner) {
        if (owner == null || owner.isDead()) {
            cc.setIsAttacking(false);
            cc.setAttackPhaseInternal(0);
            cc.setCooldownCounterFrames(0);
            cc.getAttackBox().setBounds(0, 0, 0, 0);
            cc.clearHitThisSwing();
            return;
        }

        // cooldown
        int cd = cc.getCooldownCounterFrames();
        if (cd > 0) cc.setCooldownCounterFrames(Math.max(0, cd - 1));

        if (!cc.isAttacking()) return;

        if (cc.getAttackPhase() == 2) {
            alignAttackBox(cc, owner);
        }

        // phase timer
        int timer = Math.max(0, cc.getPhaseTimerFrames() - 1);
        cc.setPhaseTimerFrames(timer);
        if (timer > 0) return;

        int phase = cc.getAttackPhase();
        if (phase == 1) { // Windup -> Active
            cc.setAttackPhaseInternal(2);
            cc.setPhaseTimerFrames(cc.getActiveFrames());
            alignAttackBox(cc, owner);
            cc.clearHitThisSwing();
        } else if (phase == 2) { // Active -> Recover
            cc.setAttackPhaseInternal(3);
            cc.setPhaseTimerFrames(cc.getRecoverFrames());
            cc.getAttackBox().setBounds(0, 0, 0, 0);
            cc.clearHitThisSwing();
        } else { // Recover -> End
            cc.setIsAttacking(false);
            cc.setAttackPhaseInternal(0);
            cc.setCooldownCounterFrames(cc.getCooldownFrames());
            cc.getAttackBox().setBounds(0, 0, 0, 0);
            cc.clearHitThisSwing();
        }
    }

    // attack box direction
    public static void alignAttackBox(CombatComponent cc, CombatContext owner) {
        Rectangle body = owner.getSolidArea();
        int bx = owner.getWorldX() + body.x;
        int by = owner.getWorldY() + body.y;
        int bw = body.width;
        int bh = body.height;

        String dir = owner.getDirection();  // hướng mặc định

        // 🔒 CHỈ MONSTER mới bị lock theo attackDir
        if (owner instanceof entity.Entity e) {
            if (e instanceof monster_data.Monster &&          // chỉ quái
                    CombatSystem.isAttacking(e.combat) &&
                    e.attackDir != null && !e.attackDir.isEmpty()) {

                dir = e.attackDir; // boss + quái dùng hướng đã lock
            }
        }

        dir = (dir == null) ? "right" : dir.toLowerCase();

        int ax, ay;
        switch (dir) {
            case "up":
                ax = bx + (bw - cc.getAttackWidth()) / 2;
                ay = by - cc.getAttackHeight();
                break;
            case "down":
                ax = bx + (bw - cc.getAttackWidth()) / 2;
                ay = by + bh;
                break;
            case "left":
                ax = bx - cc.getAttackWidth();
                ay = by + (bh - cc.getAttackHeight()) / 2;
                break;
            default: // right
                ax = bx + bw;
                ay = by + (bh - cc.getAttackHeight()) / 2;
                break;
        }

        cc.getAttackBox().setBounds(ax, ay, cc.getAttackWidth(), cc.getAttackHeight());
    }
}
