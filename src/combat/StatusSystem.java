package combat;

import entity.Entity;

public final class StatusSystem {
    private StatusSystem() {}

    public static void update(Entity e) {
        if (e == null) return;

        // i-frames
        if (e.isInvulnerable()) {
            int next = e.getInvulnCounter() - 1;
            if (next <= 0) {
                e.setInvulnCounter(0);
                e.setInvulnerable(false);
            } else {
                e.setInvulnCounter(next);
            }
        }

        // knockback motion
        int kb = e.getKnockbackCounter();
        if (kb > 0) {
            e.translate(e.getVelX(), e.getVelY());
            int next = kb - 1;
            if (next <= 0) {
                e.setKnockbackCounter(0);
                e.clearVelocity();
            } else {
                e.setKnockbackCounter(next);
            }
        }
    }
}
