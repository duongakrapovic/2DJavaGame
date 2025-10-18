package combat;

import entity.Entity;
import monster_data.Monster;
import player_manager.Player;

import java.awt.Rectangle;
import java.util.List;

public final class CombatSystem {
    private CombatSystem() {}

    // --------- API công khai (đọc trạng thái) ----------
    public static boolean isAttacking(CombatComponent cc)     { return cc.isAttacking(); }
    public static int     getPhase(CombatComponent cc)        { return cc.getAttackPhase(); }
    public static boolean isAttackActive(CombatComponent cc)  { return cc.isAttackActive(); }

    public static boolean canStartAttack(CombatComponent cc) {
        return !cc.isAttacking() && cc.getCooldownCounterFrames() == 0;
    }

    public static void startAttack(CombatComponent cc, CombatContext owner) {
        if (owner == null || owner.isDead()) return;
        if (cc.getAttackWidth() <= 0 || cc.getAttackHeight() <= 0) return;

        cc.setIsAttacking(true);
        cc.setAttackPhaseInternal(1); // windup
        cc.setPhaseTimerFrames(cc.getWindupFrames());
        updateAttackBoxToOwnerFacing(cc, owner);
        cc.clearHitThisSwing(); // chuẩn bị swing mới
    }

    public static void update(CombatComponent cc, CombatContext owner) {
        if (owner == null || owner.isDead()) {
            cc.setIsAttacking(false);
            cc.setAttackPhaseInternal(0);
            cc.setCooldownCounterFrames(0);
            cc.getAttackBox().setBounds(0, 0, 0, 0);
            cc.clearHitThisSwing();
            return;
        }

        // cooldown

        int cd = cc.getCooldownCounterFrames();
        if (cd > 0) cc.setCooldownCounterFrames(Math.max(0, cd - 1));

        if (!cc.isAttacking()) return;

        if (cc.getAttackPhase() == 2) {
            updateAttackBoxToOwnerFacing(cc, owner);
        }

        // phase counter
        int timer = Math.max(0, cc.getPhaseTimerFrames() - 1);
        cc.setPhaseTimerFrames(timer);
        if (timer > 0) return;

        int phase = cc.getAttackPhase();
        if (phase == 1) { // Windup -> Active
            cc.setAttackPhaseInternal(2);
            cc.setPhaseTimerFrames(cc.getActiveFrames());
            updateAttackBoxToOwnerFacing(cc, owner);
            cc.clearHitThisSwing();
        } else if (phase == 2) { // Active -> Recover
            cc.setAttackPhaseInternal(3);
            cc.setPhaseTimerFrames(cc.getRecoverFrames());
            cc.getAttackBox().setBounds(0, 0, 0, 0);
            cc.clearHitThisSwing();
        } else { // Recover -> End
            cc.setIsAttacking(false);
            cc.setAttackPhaseInternal(0);
            cc.setCooldownCounterFrames(cc.getCooldownFrames());
            cc.getAttackBox().setBounds(0, 0, 0, 0);
            cc.clearHitThisSwing();
        }
    }

    // attack box counting
    public static void updateAttackBoxToOwnerFacing(CombatComponent cc, CombatContext owner) {
        Rectangle body = owner.getSolidArea();
        int boxX = owner.getWorldX() + body.x;
        int boxY = owner.getWorldY() + body.y;

        String dir = owner.getDirection();
        if ("up".equals(dir)) {
            boxY -= cc.getAttackHeight();
        } else if ("down".equals(dir)) {
            boxY += body.height;
        } else if ("left".equals(dir)) {
            boxX -= cc.getAttackWidth();
        } else { // right (mặc định)
            boxX += body.width;
        }
        cc.getAttackBox().setBounds(boxX, boxY, cc.getAttackWidth(), cc.getAttackHeight());
    }
    // mark hit
    public static boolean wasHitThisSwing(CombatComponent cc, Object target) {
        return cc.wasHitThisSwing(target);
    }
    public static void markHitLanded(CombatComponent cc, Object target) {
        cc.markHit(target);
    }

    // i -frames
    /** Cập nhật i-frame & knockback mỗi frame. */
    public static void updateStatus(Entity e) {
        if (e == null) return;

        // i-frames
        if (e.isInvulnerable()) {
            int next = e.getInvulnCounter() - 1;
            if (next <= 0) {
                e.setInvulnCounter(0);
                e.setInvulnerable(false);
            } else {
                e.setInvulnCounter(next);
            }
        }

        // knockback
        int kb = e.getKnockbackCounter();
        if (kb > 0) {
            e.worldX += e.velX;
            e.worldY += e.velY;
            int next = kb - 1;
            if (next <= 0) {
                e.setKnockbackCounter(0);
                e.velX = 0;
                e.velY = 0;
            } else {
                e.setKnockbackCounter(next);
            }
        }
    }

    public static void tick(Entity e) {
        update(e.combat, e);
        updateStatus(e);
    }

   // KnockBack
    public static int[] computePlayerAttackKnockback(Player p) {
        int force = 8; // px/frame
        String dir = p.direction;
        if ("up".equals(dir))    return new int[] { 0, -force };
        if ("down".equals(dir))  return new int[] { 0,  force };
        if ("left".equals(dir))  return new int[] { -force, 0 };
        return new int[] { force, 0 }; // right
    }

    // touch to hit for Monsetr
    public static void resolvePlayerHits(Player player, List<Entity> monsters) {
        if (player == null) return;
        if (!isAttackActive(player.combat)) return;

        Rectangle attack = player.combat.getAttackBox();
        if (attack == null || attack.width <= 0 || attack.height <= 0) return;

        int rawDamage = Math.max(1, player.getATK());
        int[] knockback = computePlayerAttackKnockback(player); // {kx, ky}

        for (Entity e : monsters) {
            if (!(e instanceof Monster)) continue;
            Monster m = (Monster) e;
            if (m.isDead()) continue;

            Rectangle monsterBody = CollisionUtil.getEntityBodyWorldRect(m);
            if (attack.intersects(monsterBody)) {
                // use markhit to avoid multi hit
                if (!wasHitThisSwing(player.combat, m)) {
                    DamageProcessor.applyDamage(m, rawDamage, knockback[0], knockback[1]);
                    markHitLanded(player.combat, m);
                }
            }
        }
    }

    /** Thân Player chạm thân quái → Player nhận sát thương/knockback. */
    public static void resolveMonsterContacts(Player player, List<Entity> monsters) {
        if (player == null || player.isDead()) return;

        Rectangle playerBody = CollisionUtil.getEntityBodyWorldRect(player);

        // Nới 1px để “chạm mép” cũng tính
        Rectangle playerContact = new Rectangle(playerBody);
        playerContact.grow(1, 1);

        for (Entity e : monsters) {
            if (!(e instanceof Monster)) continue;
            Monster m = (Monster) e;
            if (m.isDead()) continue;

            Rectangle monsterBody = CollisionUtil.getEntityBodyWorldRect(m);
            if (playerContact.intersects(monsterBody)) {
                if (!player.isInvulnerable()) {
                    int raw = Math.max(1, m.getATK());

                    // Knockback từ quái -> player (dựa trên tâm)
                    int pcx = playerBody.x + playerBody.width  / 2;
                    int pcy = playerBody.y + playerBody.height / 2;
                    int mcx = monsterBody.x + monsterBody.width  / 2;
                    int mcy = monsterBody.y + monsterBody.height / 2;

                    int kx = Integer.compare(pcx, mcx) * 6;
                    int ky = Integer.compare(pcy, mcy) * 6;

                    player.takeDamage(raw, kx, ky);
                    System.out.println("[CONTACT DMG] " + m.name + " -> Player HP="
                            + player.getHP() + "/" + player.getMaxHP());
                }
            }
        }
    }
}
