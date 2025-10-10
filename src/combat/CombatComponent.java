package combat;

import java.awt.Rectangle;

/** Component dữ liệu combat – tên biến đầy đủ, không viết tắt. */
public class CombatComponent {

    // --- Cấu hình nhịp đòn (tính theo frame @60fps) ---
    private int windupFrames   = 6;   // lấy đà
    private int activeFrames   = 6;   // thời gian bật hitbox
    private int recoverFrames  = 10;  // hạ tay
    private int cooldownFrames = 12;  // hồi chiêu sau khi kết thúc đòn

    // --- Kích thước hitbox tấn công (world-space) ---
    private int attackWidth  = 36;
    private int attackHeight = 36;

    // --- Trạng thái hiện tại của đòn ---
    private boolean isAttacking = false;
    private int attackPhase = 0;           // 0: idle, 1: windup, 2: active, 3: recover
    private int phaseTimerFrames = 0;      // bộ đếm cho phase hiện tại
    private int cooldownCounterFrames = 0; // bộ đếm hồi chiêu

    // --- Hitbox tấn công (tọa độ world) ---
    private final Rectangle attackBox = new Rectangle();

    // ================== API cấu hình ==================

    /** Đặt kích thước hitbox tấn công. */
    public void setAttackBoxSize(int width, int height) {
        this.attackWidth = width;
        this.attackHeight = height;
    }

    /** Đặt số frame cho các giai đoạn: windup, active, recover, cooldown. */
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
}
