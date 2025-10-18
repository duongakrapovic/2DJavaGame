package combat;

import entity.Entity;
import java.awt.Rectangle;

public final class CollisionUtil {
    private CollisionUtil() {}
    public static Rectangle getEntityBodyWorldRect(Entity e) {
        if (e == null) return new Rectangle(0, 0, 0, 0);
        final int offX = (e.solidArea != null) ? e.solidArea.x : 0;
        final int offY = (e.solidArea != null) ? e.solidArea.y : 0;
        final int w    = Math.max(0, (e.solidArea != null) ? e.solidArea.width  : Math.max(1, e.width));
        final int h    = Math.max(0, (e.solidArea != null) ? e.solidArea.height : Math.max(1, e.height));

        return new Rectangle(e.worldX + offX, e.worldY + offY, w, h);
    }

    public static Rectangle getContextBodyWorldRect(CombatContext ctx) {
        if (ctx == null) return new Rectangle(0, 0, 0, 0);

        Rectangle sa = ctx.getSolidArea();
        int offX = (sa != null) ? sa.x : 0;
        int offY = (sa != null) ? sa.y : 0;
        int w    = Math.max(0, (sa != null) ? sa.width  : 1);
        int h    = Math.max(0, (sa != null) ? sa.height : 1);
        return new Rectangle(
                ctx.getWorldX() + offX,
                ctx.getWorldY() + offY,
                w, h
        );
    }

}
