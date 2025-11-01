package combat;

import entity.Entity;
import monster_data.Monster;
import player_manager.Player;

import java.util.List;

public final class CombatSystem {
    private CombatSystem() {}

    // --------- API công khai (đọc trạng thái) ----------
    public static boolean isAttacking(CombatComponent cc)     { return cc.isAttacking(); }
    public static int     getPhase(CombatComponent cc)        { return cc.getAttackPhase(); }
    public static boolean isAttackActive(CombatComponent cc)  { return cc.isAttackActive(); }

    public static boolean canStartAttack(CombatComponent cc) { return AttackPhaseSystem.canStart(cc); }

    public static void startAttack(CombatComponent cc, CombatContext owner) {
        AttackPhaseSystem.start(cc, owner);
    }

    public static void update(CombatComponent cc, CombatContext owner) {
        AttackPhaseSystem.update(cc, owner);
    }

    // mark hit
    public static boolean wasHitThisSwing(CombatComponent cc, Object target) { return cc.wasHitThisSwing(target); }
    public static void    markHitLanded(CombatComponent cc, Object target)   { cc.markHit(target); }

    // i-frames + knockback movement
    public static void updateStatus(Entity e) {
        StatusSystem.update(e);
    }

    // tick entity (giữ thứ tự cũ của bạn; nếu muốn bạn có thể đổi sang Status→Update)
    public static void tick(Entity e) {
        if (e == null || e.combat == null) return;
        update(e.combat, e);
        updateStatus(e);
    }
    // KnockBack (Player → Monster)
    public static int[] computePlayerAttackKnockback(Player p) {
        return KnockbackService.forPlayerAttack(p);
    }
    public static int[] computeMonsterAttackKnockback(Monster m, Player p) {return KnockbackService.forMonsterAttack(m, p);}
    public static void resolvePlayerHits(Player player, List<entity.Entity> monsters) {
        HitResolvePlayer.resolve(player, monsters);
    }

    public static void resolveMonsterHitAgainstPlayer(Monster m, Player player) {
        HitResolveMonster.resolve(m, player);
    }
}
