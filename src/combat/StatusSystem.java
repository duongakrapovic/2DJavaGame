package combat;

import entity.Entity;

public final class StatusSystem {
    private StatusSystem() {}


    public static void update(Entity e) {
        if (e == null) return;

        if (e.isInvulnerable()) {
            int next = e.getInvulnCounter() - 1;
            if (next <= 0) {
                e.setInvulnCounter(0);
                e.setInvulnerable(false);
            } else {
                e.setInvulnCounter(next);
            }
        }
    }
}