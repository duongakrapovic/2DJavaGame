package combat;

import entity.Entity;
import java.awt.Rectangle;

/** Tiện ích va chạm: trả về body-rectangle ở tọa độ world. */
public final class CollisionUtil {
    private CollisionUtil() {}

    /** Body rect của một Entity theo world-space. */
    public static Rectangle getEntityBodyWorldRect(Entity entity) {
        if (entity == null) return null;
        Rectangle solid = (entity.solidArea != null)
                ? entity.solidArea
                : new Rectangle(0, 0, Math.max(1, entity.width), Math.max(1, entity.height));
        return new Rectangle(
                entity.worldX + solid.x,
                entity.worldY + solid.y,
                solid.width,
                solid.height
        );
    }

    /** Body rect từ một CombatContext theo world-space. */
    public static Rectangle getContextBodyWorldRect(CombatContext context) {
        Rectangle solid = context.getSolidArea();
        return new Rectangle(
                context.getWorldX() + solid.x,
                context.getWorldY() + solid.y,
                solid.width,
                solid.height
        );
    }
}
