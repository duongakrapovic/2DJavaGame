package combat;

import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

public class CombatComponent {

    // frame phase
    private int windupFrames   = 6;   // lấy đà
    private int activeFrames   = 6;   // thời gian bật hitbox
    private int recoverFrames  = 10;  // hạ tay
    private int cooldownFrames = 12;  // hồi chiêu sau khi kết thúc đòn

    // --- knockback config
    private int knockbackForce = 0;

    // --- hitbox size default
    private int attackWidth  = 36;
    private int attackHeight = 36;

    // phase status
    private boolean isAttacking = false;
    private int attackPhase = 0;           // 0: idle, 1: windup, 2: active, 3: recover
    private int phaseTimerFrames = 0;      // bộ đếm cho phase hiện tại
    private int cooldownCounterFrames = 0; // bộ đếm hồi chiêu

    // --- Attack hitbox
    private final Rectangle attackBox = new Rectangle();

    private final Set<Object> hitThisSwing = new HashSet<>();

    // ================== API ==================

    // set size
    public void setAttackBoxSize(int width, int height) {
        this.attackWidth  = Math.max(1, width);
        this.attackHeight = Math.max(1, height);
        this.attackBox.setSize(this.attackWidth, this.attackHeight);
    }

    // hit-once-per-swing
    public boolean wasHitThisSwing(Object target) { return hitThisSwing.contains(target); }
    public void    markHit(Object target)         { if (target != null) hitThisSwing.add(target); }
    public void    clearHitThisSwing()            { hitThisSwing.clear(); }

    // Set frames
    public void setTimingFrames(int windup, int active, int recover, int cooldown) {
        this.windupFrames   = Math.max(0, windup);
        this.activeFrames   = Math.max(0, active);
        this.recoverFrames  = Math.max(0, recover);
        this.cooldownFrames = Math.max(0, cooldown);
    }

    public int  getKnockbackForce()         { return knockbackForce; }
    public void setKnockbackForce(int kb)   { this.knockbackForce = Math.max(0, kb); }

    // ================== API truy cập trạng thái ==================
    public boolean  isAttacking()            { return isAttacking; }
    public boolean  isAttackActive()         { return isAttacking && attackPhase == 2; }
    public int      getAttackPhase()         { return attackPhase; }
    public Rectangle getAttackBox()          { return attackBox; }

    // ================== Package-access cho CombatSystem/AttackPhaseSystem ==================
    int  getWindupFrames()               { return windupFrames; }
    int  getActiveFrames()               { return activeFrames; }
    int  getRecoverFrames()              { return recoverFrames; }
    int  getCooldownFrames()             { return cooldownFrames; }
    int  getAttackWidth()                { return attackWidth; }
    int  getAttackHeight()               { return attackHeight; }

    boolean getIsAttacking()             { return isAttacking; }
    void    setIsAttacking(boolean v)    { isAttacking = v; }

    int  getAttackPhaseInternal()        { return attackPhase; }
    void setAttackPhaseInternal(int v)   { attackPhase = v; }

    int  getPhaseTimerFrames()           { return phaseTimerFrames; }
    void setPhaseTimerFrames(int v)      { phaseTimerFrames = v; }

    int  getCooldownCounterFrames()      { return cooldownCounterFrames; }
    void setCooldownCounterFrames(int v) { cooldownCounterFrames = v; }

    @SuppressWarnings("Unused")
    public static void setCooldown(CombatComponent cc, int frames) {
        cc.setCooldownCounterFrames(Math.max(0, frames));
    }
    public static int getCooldown(CombatComponent cc) {
        return cc.getCooldownCounterFrames();
    }
    public static int attackWidth(CombatComponent cc)  { return cc.getAttackWidth(); }
    public static int attackHeight(CombatComponent cc) { return cc.getAttackHeight(); }
}
