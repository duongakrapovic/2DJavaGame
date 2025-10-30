package combat;

import entity.Entity;
import monster_data.Monster;
import player_manager.Player;

import java.awt.Rectangle;
import java.util.List;

public final class HitResolvePlayer {
    private HitResolvePlayer() {}

    public static void resolve(Player player, List<Entity> monsters) {
        if (player == null) return;
        if (!player.combat.isAttackActive()) return;

        Rectangle attack = player.combat.getAttackBox();
        if (attack == null || attack.width <= 0 || attack.height <= 0) return;

        int rawDamage = Math.max(1, player.getATK());
        int[] knockback = KnockbackService.forPlayerAttack(player);

        for (Entity e : monsters) {
            if (!(e instanceof Monster)) continue;
            Monster m = (Monster) e;
            if (m.isDead()) continue;

            Rectangle monsterBody = CollisionUtil.getEntityBodyWorldRect(m);
            if (!attack.intersects(monsterBody)) continue;

            if (!player.combat.wasHitThisSwing(m)) {
                DamageProcessor.applyDamage(m, rawDamage, knockback[0], knockback[1]);
                player.combat.markHit(m);
            }
        }
    }
}
