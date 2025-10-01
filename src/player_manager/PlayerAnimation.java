// PlayerAnimation.java
package player_manager;

public class PlayerAnimation {
    private final Player p;
    private int frameDelay = 8;

    public PlayerAnimation(Player p) { this.p = p; }

    // nhận trạng thái từ Player, không chạm combat trực tiếp
    public void update(boolean moving, boolean attacking, int attackPhase) {
        // ví dụ flip 1-2 cho chạy/đánh
        if (attacking) {
            p.spriteCounter++;
            if (p.spriteCounter > frameDelay) {
                p.spriteNum = (p.spriteNum == 1 ? 2 : 1);
                p.spriteCounter = 0;
            }
            return;
        }

        if (moving) {
            p.spriteCounter++;
            if (p.spriteCounter > frameDelay) {
                p.spriteNum = (p.spriteNum == 1 ? 2 : 1);
                p.spriteCounter = 0;
            }
        } else {
            p.spriteNum = 1; // đứng yên
        }
    }
}
