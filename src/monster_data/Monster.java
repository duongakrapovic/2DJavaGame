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
    public void onDamaged(int damage) {
        // hiệu ứng khi quái bị trúng đòn: flash, drop, âm thanh...
    }

    public void onDeath() {
        // drop đồ/exp ở đây
    }
}
