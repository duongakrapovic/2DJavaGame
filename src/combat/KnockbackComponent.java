package combat;

import entity.Entity;

/** Tiện ích áp knockback – để sau này tách khỏi Entity nếu cần. */
public final class KnockbackComponent {
    private KnockbackComponent() {}

    public static void apply(Entity entity, int knockbackX, int knockbackY, int durationFrames) {
        if (entity != null) entity.applyKnockback(knockbackX, knockbackY, durationFrames);
    }
}
