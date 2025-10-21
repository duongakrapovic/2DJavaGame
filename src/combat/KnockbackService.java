package combat;

import monster_data.Monster;
import player_manager.Player;

public final class KnockbackService {
    private KnockbackService() {}

    private static int clamp(int v, int lo, int hi){ return Math.max(lo, Math.min(hi, v)); }

    // Player → Monster
    public static int[] forPlayerAttack(Player p) {
        int baseForce = (p != null && p.combat != null && p.combat.getKnockbackForce() > 0)
                ? p.combat.getKnockbackForce() : 8;

        // scale nhẹ theo ATK (tuỳ chọn)
        int scaled = (int) Math.round(baseForce * (1.0 + Math.min(Math.max(p.getATK(), 0), 50) * 0.01));

        String dir = (p != null && p.direction != null) ? p.direction.toLowerCase() : "right";
        int kx, ky;
        switch (dir) {
            case "up":    kx = 0;        ky = -scaled; break;
            case "down":  kx = 0;        ky =  scaled; break;
            case "left":  kx = -scaled;  ky = 0;       break;
            case "right": kx =  scaled;  ky = 0;       break;
            // nếu bạn có thêm 8 hướng, có thể bổ sung cases chéo + chuẩn hoá √2
            default:      kx =  scaled;  ky = 0;
        }

        int maxKB = 16;
        return new int[] { clamp(kx, -maxKB, maxKB), clamp(ky, -maxKB, maxKB) };
    }

    // Monster → Player (vector m→player; mượt hơn so với chọn trục lớn nhất)
    public static int[] forMonsterAttack(Monster m, player_manager.Player player) {
        if (m == null || player == null) return new int[]{0,0};

        int dx = player.getWorldX() - m.getWorldX();
        int dy = player.getWorldY() - m.getWorldY();

        double len = Math.hypot(dx, dy);
        int kbForce = (m.combat != null && m.combat.getKnockbackForce() > 0) ? m.combat.getKnockbackForce() : 6;

        int kx = (len == 0) ? 0 : (int)Math.round(kbForce * dx / len);
        int ky = (len == 0) ? 0 : (int)Math.round(kbForce * dy / len);

        if (kx == 0 && dx != 0) kx = dx > 0 ? 1 : -1;
        if (ky == 0 && dy != 0) ky = dy > 0 ? 1 : -1;

        int maxKB = 16;
        kx = Math.max(-maxKB, Math.min(maxKB, kx));
        ky = Math.max(-maxKB, Math.min(maxKB, ky));
        return new int[]{kx, ky};
    }
}
