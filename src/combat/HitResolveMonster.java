package combat;

import monster_data.Monster;
import player_manager.Player;

import java.awt.Rectangle;

public final class HitResolveMonster {
    private HitResolveMonster() {}

    public static void resolve(Monster m, Player player) {
        if (m == null || m.isDead() || player == null || player.isDead()) return;
        if (!m.combat.isAttackActive()) return;

        Rectangle attack = m.combat.getAttackBox();
        if (attack == null || attack.width <= 0 || attack.height <= 0) return;

        Rectangle playerBody = CollisionUtil.getEntityBodyWorldRect(player);
        if (!attack.intersects(playerBody)) return;

        if (m.combat.wasHitThisSwing(player)) return;

        int rawDamage = Math.max(1, m.getATK());
        int[] kb = KnockbackService.forMonsterAttack(m, player);

        DamageProcessor.applyDamage(player, rawDamage, kb[0], kb[1]);
        m.combat.markHit(player);
    }
}
