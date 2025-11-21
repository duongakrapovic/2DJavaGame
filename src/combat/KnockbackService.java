package combat;

import monster_data.Monster;
import player_manager.Player;

public final class KnockbackService {
    private KnockbackService() {}

    // cấu hình nhanh
    private static final int DEFAULT_PLAYER_KB = 3;
    private static final int DEFAULT_MONSTER_KB = 3;
    private static final int MAX_KB = 3;

    private static int clamp(int v, int lo, int hi){ return Math.max(lo, Math.min(hi, v)); }

    // Player → Monster
    public static int[] forPlayerAttack(Player p) {
        if (p == null || p.combat == null) return new int[]{0,0};

        int baseForce = (p.combat.getKnockbackForce() > 0)
                ? p.combat.getKnockbackForce()
                : DEFAULT_PLAYER_KB;

        // ATK an toàn khi null
        int atk = 0;
        try { atk = Math.max(0, p.getATK()); } catch (Exception ignore) {}

        // scale nhẹ theo ATK (0..+50%); điều chỉnh tuỳ game
        int scaled = (int) Math.round(baseForce * (1.0 + Math.min(atk, 50) * 0.01));

        String dir = null;
        try { dir = p.getDirection(); } catch (Exception ignore) {}
        dir = (dir == null) ? "right" : dir.toLowerCase();

        int kx, ky;
        switch (dir) {
            case "up":    kx = 0;        ky = -scaled; break;
            case "down":  kx = 0;        ky =  scaled; break;
            case "left":  kx = -scaled;  ky = 0;       break;
            case "right": kx =  scaled;  ky = 0;       break;
            default:      kx =  scaled;  ky = 0;
        }

        return new int[]{ clamp(kx, -MAX_KB, MAX_KB), clamp(ky, -MAX_KB, MAX_KB) };
    }

    // Monster → Player (vector m→player; mượt)
    public static int[] forMonsterAttack(Monster m, Player player) {
        if (m == null || m.isDead() || player == null || player.isDead()) return new int[]{0,0};

        int kbForce = (m.combat != null && m.combat.getKnockbackForce() > 0)
                ? m.combat.getKnockbackForce()
                : DEFAULT_MONSTER_KB;

        int dx = player.getWorldX() - m.getWorldX();
        int dy = player.getWorldY() - m.getWorldY();

        double len = Math.hypot(dx, dy);
        int kx = (len == 0) ? 0 : (int) Math.round(kbForce * dx / len);
        int ky = (len == 0) ? 0 : (int) Math.round(kbForce * dy / len);

        // đảm bảo có tối thiểu 1px theo hướng đúng khi len nhỏ
        if (kx == 0 && dx != 0) kx = dx > 0 ? 1 : -1;
        if (ky == 0 && dy != 0) ky = dy > 0 ? 1 : -1;

        return new int[]{ clamp(kx, -MAX_KB, MAX_KB), clamp(ky, -MAX_KB, MAX_KB) };
    }
}