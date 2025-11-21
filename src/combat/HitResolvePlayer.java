package combat;

import entity.Entity;
import monster_data.Monster;
import player_manager.Player;

import java.awt.Rectangle;
import java.util.List;

public final class HitResolvePlayer {
    private HitResolvePlayer() {}

    public static void resolve(Player player, List<Entity> monsters) {
        if (player == null || player.isDead()) return;
        if (!CombatSystem.isAttackActive(player.combat)) return;

        Rectangle attack = player.combat.getAttackBox();
        if (attack == null || attack.width <= 0 || attack.height <= 0) return;

        int rawDamage = Math.max(1, player.getATK());
        int[] knockback = CombatSystem.computePlayerAttackKnockback(player);

        for (Entity e : monsters) {
            if (!(e instanceof Monster)) continue;
            Monster m = (Monster) e;
            if (m.isDead()) continue;

            Rectangle monsterBody = CollisionUtil.getEntityBodyWorldRect(m);
            if (!attack.intersects(monsterBody)) continue;

            if (!CombatSystem.wasHitThisSwing(player.combat, m)) {
                DamageProcessor.applyDamage(m, rawDamage, knockback[0], knockback[1]);
                CombatSystem.markHitLanded(player.combat, m);
            }
        }
    }
}