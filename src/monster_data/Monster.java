package monster_data;

import entity.Entity;
import main.GamePanel;

public abstract class Monster extends Entity {
    protected int contactDamage = 1;   // dmg khi chạm player
    protected int expOnKill = 1;       // điểm thưởng

    public Monster(GamePanel gp) {
        super(gp);
    }

    @Override
    protected void onDamaged(int dmg) {
        // hiệu ứng bị đánh
        if (isDead()) {
            onDeath();
        }
    }

    protected void onDeath() {
        // drop đồ/exp ở đây
    }
}
