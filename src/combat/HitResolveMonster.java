package combat;

import monster_data.Monster;
import player_manager.Player;

import java.awt.Rectangle;

public final class HitResolveMonster {
    private HitResolveMonster() {}

    public static void resolve(Monster m, Player player) {
        if (m == null || player == null || m.isDead() || player.isDead()) return;
        if (!CombatSystem.isAttackActive(m.combat)) return;

        Rectangle attack = m.combat.getAttackBox();
        if (attack == null || attack.width <= 0 || attack.height <= 0) return;

        Rectangle playerBody = CollisionUtil.getEntityBodyWorldRect(player);
        if (!attack.intersects(playerBody)) return;

        if (CombatSystem.wasHitThisSwing(m.combat, player)) return;

        int rawDamage = Math.max(1, m.getATK());
        int[] kb = CombatSystem.computeMonsterAttackKnockback(m, player);

        DamageProcessor.applyDamage(player, rawDamage, kb[0], kb[1]);

        CombatSystem.markHitLanded(m.combat, player);
    }
}