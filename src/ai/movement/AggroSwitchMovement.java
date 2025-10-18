package ai.movement;

import entity.Entity;

import java.util.function.Predicate;

/** Chuyển controller theo điều kiện (ví dụ: gần player thì chase, xa thì wander). */
public class AggroSwitchMovement implements MovementController {
    private final MovementController idleController;
    private final MovementController aggroController;
    private final Predicate<Entity> aggroCondition;

    public AggroSwitchMovement(MovementController idle, MovementController aggro,
                               Predicate<Entity> condition) {
        this.idleController = idle;
        this.aggroController = aggro;
        this.aggroCondition = condition;
    }

    @Override
    public void decide(Entity e) {
        if (aggroCondition != null && aggroCondition.test(e)) {
            aggroController.decide(e);
        } else {
            idleController.decide(e);
        }
    }
}
