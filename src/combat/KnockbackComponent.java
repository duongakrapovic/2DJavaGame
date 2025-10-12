package combat;

import entity.Entity;
public final class KnockbackComponent {
    private KnockbackComponent() {}

    public static void apply(Entity entity, int knockbackX, int knockbackY, int durationFrames) {
        if (entity != null) entity.applyKnockback(knockbackX, knockbackY, durationFrames);
    }
}
