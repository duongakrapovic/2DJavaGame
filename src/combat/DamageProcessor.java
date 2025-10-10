package combat;

import entity.Entity;

/** Bộ xử lý sát thương – bọc lệnh gây damage/knockback/i-frame. */
public final class DamageProcessor {
    private DamageProcessor() {}

    /**
     * Áp dụng sát thương lên target:
     * - Tự trừ máu (tối thiểu 1 sau DEF)
     * - Kích hoạt i-frame theo cấu hình của target
     * - Áp dụng knockback (nếu kx, ky != 0)
     */
    public static void applyDamage(Entity target, int rawDamage, int knockbackX, int knockbackY) {
        if (target == null) return;
        target.takeDamage(rawDamage, knockbackX, knockbackY);
    }
}
