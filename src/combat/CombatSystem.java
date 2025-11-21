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

        // DEBUG: log mỗi lần bất kỳ entity nào bắt đầu đòn
        if (owner instanceof entity.Entity) {          // import entity.Entity nếu chưa có
            entity.Entity e = (entity.Entity) owner;

            if (e.gp != null) {
                System.out.println(
                        "[ATTACK START] by=" + e.name +
                                " frame=" + e.gp.frameCounter +
                                " phase=" + cc.getAttackPhase()
                );
            } else {
                System.out.println(
                        "[ATTACK START] by=" + e.name +
                                " phase=" + cc.getAttackPhase()
                );
            }
        } else {
            // phòng trường hợp owner là loại khác Entity
            System.out.println(
                    "[ATTACK START] by=" + owner.getClass().getSimpleName() +
                            " phase=" + cc.getAttackPhase()
            );
        }

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
