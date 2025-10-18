package combat;

import java.awt.Rectangle;

public interface CombatContext {
    int getWorldX();
    int getWorldY();
    Rectangle getSolidArea();
    String getDirection();
    boolean isDead();
}
