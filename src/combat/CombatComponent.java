package combat;

import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;
public class CombatComponent {

    // frame phase
    private int windupFrames   = 6;   // lấy đà
    private int activeFrames   = 6;   // thời gian bật hitbox
    private int recoverFrames  = 10;  // hạ tay
    private int cooldownFrames = 12;  // hồi chiêu sau khi kết thúc đònz

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

    // API

    // set size
    public void setAttackBoxSize(int width, int height) {
        this.attackWidth = width;
        this.attackHeight = height;
    }
    private final Set<Object> hitThisSwing = new HashSet<>();
    public  boolean wasHitThisSwing(Object target) { return hitThisSwing.contains(target); }
    public  void     markHit(Object target)        { if (target != null) hitThisSwing.add(target); }
    public  void     clearHitThisSwing()           { hitThisSwing.clear(); }
    // Set frames
    public void setTimingFrames(int windup, int active, int recover, int cooldown) {
        this.windupFrames   = windup;
        this.activeFrames   = active;
        this.recoverFrames  = recover;
        this.cooldownFrames = cooldown;
    }

    // ================== API truy cập trạng thái ==================

    public boolean isAttacking()             { return isAttacking; }
    public boolean isAttackActive()          { return isAttacking && attackPhase == 2; }
    public int      getAttackPhase()         { return attackPhase; }
    public Rectangle getAttackBox()          { return attackBox; }

    // ================== Package-access cho CombatSystem ==================
    int      getWindupFrames()               { return windupFrames; }
    int      getActiveFrames()               { return activeFrames; }
    int      getRecoverFrames()              { return recoverFrames; }
    int      getCooldownFrames()             { return cooldownFrames; }
    int      getAttackWidth()                { return attackWidth; }
    int      getAttackHeight()               { return attackHeight; }

    boolean  getIsAttacking()                { return isAttacking; }
    void     setIsAttacking(boolean value)   { isAttacking = value; }

    int      getAttackPhaseInternal()        { return attackPhase; }
    void     setAttackPhaseInternal(int v)   { attackPhase = v; }

    int      getPhaseTimerFrames()           { return phaseTimerFrames; }
    void     setPhaseTimerFrames(int v)      { phaseTimerFrames = v; }

    int      getCooldownCounterFrames()      { return cooldownCounterFrames; }
    void     setCooldownCounterFrames(int v) { cooldownCounterFrames = v; }
    public static void setCooldown(CombatComponent cc, int frames) {
        cc.setCooldownCounterFrames(Math.max(0, frames));
    }
    public static int getCooldown(CombatComponent cc) {
        return cc.getCooldownCounterFrames();
    }

    // (nếu bạn chưa thêm 2 hàm này thì thêm luôn, để lấy kích thước từ ngoài package)
    public static int attackWidth(CombatComponent cc)  { return cc.getAttackWidth(); }
    public static int attackHeight(CombatComponent cc) { return cc.getAttackHeight(); }

}
