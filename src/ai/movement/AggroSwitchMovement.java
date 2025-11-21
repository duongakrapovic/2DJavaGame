package ai.movement;

import entity.Entity;

import java.util.function.Predicate;

public class AggroSwitchMovement implements MovementController {
    private final MovementController idleController;
    private final MovementController aggroController;
    private final Predicate<Entity> aggroCondition;

    // --- chống bật/tắt liên tục ---
    private final int onFrames  = 3;
    private final int offFrames = 6;

    private int onCount  = 0;
    private int offCount = 0;
    private boolean isAggro = false;

    public AggroSwitchMovement(MovementController idle, MovementController aggro,
                               Predicate<Entity> condition) {
        this.idleController = idle;
        this.aggroController = aggro;
        this.aggroCondition = condition;
    }

    @Override
    public void decide(Entity e) {
        boolean wantAggro = aggroCondition != null && aggroCondition.test(e);

        if (wantAggro) {
            onCount  = Math.min(onCount + 1, 1000);
            offCount = Math.max(offCount - 1, 0);
        } else {
            offCount = Math.min(offCount + 1, 1000);
            onCount  = Math.max(onCount - 1, 0);
        }

        if (!isAggro && onCount >= onFrames)  { isAggro = true;  offCount = 0; }
        if ( isAggro && offCount >= offFrames){ isAggro = false; onCount  = 0; }

        if (isAggro) aggroController.decide(e);
        else         idleController.decide(e);
    }
}
