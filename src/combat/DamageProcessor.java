package combat;

import entity.Entity;

public final class DamageProcessor {
    private DamageProcessor(){}

    public static void applyDamage(Entity target, int rawDamage, int knockbackX, int knockbackY) {
        if (target == null || target.isDead() || target.isInvulnerable()) return;

        int damage = Math.max(1, rawDamage - target.getDEF());

        // reduceHP
        target.reduceHP(damage);

        // i-frame
        target.setInvulnerable(true);
        target.setInvulnCounter(target.getInvulnFrames());

        // knockback
        target.applyKnockback(knockbackX, knockbackY, target.getKnockbackFrames());

        // hook effect (sound , sfx .. )
        target.onDamaged(damage);
    }
}