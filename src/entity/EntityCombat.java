package entity;

import java.awt.Rectangle;

public class EntityCombat {
    private final Entity e;

    // --- Attack state ---
    private boolean attacking;
    private int attackCounter;        // đếm ngược tổng frame còn lại của đòn
    private int attackPhase;          // 0 idle, 1 windup, 2 active, 3 recover
    private int attackCooldownCounter;

    // cấu hình nhịp đòn (frame @60fps)
    public int attackWindup   = 6;    // lấy đà
    public int attackActive   = 6;    // thời gian hitbox bật
    public int attackRecover  = 10;   // hạ tay
    public int attackCooldown = 12;   // hồi chiêu sau khi đòn kết thúc

    // hitbox đòn đánh (CHUNG với Entity)
    public final Rectangle attackBox = new Rectangle(0,0,0,0);
    public int attackWidth  = 36;
    public int attackHeight = 36;

    // nếu muốn hitbox bám theo thân trong suốt Active
    public boolean followDuringActive = true;

    public EntityCombat(Entity owner) {
        this.e = owner;
        // trỏ chung trong Entity constructor:
        // owner.attackBox = this.attackBox;  (đã làm ở Entity)
    }

    public boolean isAttacking()      { return attacking; }
    public int     getAttackPhase()   { return attackPhase; }   // 0/1/2/3
    public int     getCooldownLeft()  { return attackCooldownCounter; }

    public boolean canStartAttack() {
        return !attacking && attackCooldownCounter <= 0 && !e.isDead();
    }

    public void startAttack() {
        if (!canStartAttack()) return;
        attacking     = true;
        attackPhase   = 1; // windup
        attackCounter = attackWindup + attackActive + attackRecover;

        // KHÔNG đặt hitbox ở đây (đang windup)
        onAttackStart();
    }

    public boolean isAttackActive() {
        return attacking && attackPhase == 2;
    }

    // hooks
    protected void onAttackStart() { /* sfx/particles nếu cần */ }
    protected void onAttackEnd()   { /* sfx/particles nếu cần */ }

    private void clearAttackBox() {
        attackBox.setBounds(0, 0, 0, 0);
    }

    private void positionAttackBoxByDirection() {
        int bodyX = e.worldX + (e.solidArea != null ? e.solidArea.x : 0);
        int bodyY = e.worldY + (e.solidArea != null ? e.solidArea.y : 0);
        int bodyW = (e.solidArea != null ? e.solidArea.width  : e.width);
        int bodyH = (e.solidArea != null ? e.solidArea.height : e.height);

        int w = attackWidth;
        int h = attackHeight;

        switch (e.direction) {
            case "up":
                attackBox.setBounds(
                        bodyX + (bodyW - w) / 2,
                        bodyY - h,
                        w, h
                );
                break;
            case "down":
                attackBox.setBounds(
                        bodyX + (bodyW - w) / 2,
                        bodyY + bodyH,
                        w, h
                );
                break;
            case "left":
                attackBox.setBounds(
                        bodyX - w,
                        bodyY + (bodyH - h) / 2,
                        w, h
                );
                break;
            default: // right
                attackBox.setBounds(
                        bodyX + bodyW,
                        bodyY + (bodyH - h) / 2,
                        w, h
                );
                break;
        }
    }

    public void update() {
        // hồi chiêu giữa các đòn
        if (attackCooldownCounter > 0) attackCooldownCounter--;

        if (!attacking) {
            clearAttackBox();
            return;
        }

        int total = attackWindup + attackActive + attackRecover;
        int t = total - attackCounter; // số frame đã trôi qua từ khi start

        if (t < attackWindup) {
            // WINDUP: không có hitbox
            attackPhase = 1;
            clearAttackBox();
        } else if (t < attackWindup + attackActive) {
            // ACTIVE: bật hitbox
            attackPhase = 2;
            // bám theo thân mỗi frame (nếu followDuringActive = true)
            if (followDuringActive || attackBox.width == 0) {
                positionAttackBoxByDirection();
            }
        } else {
            // RECOVER: tắt hitbox
            attackPhase = 3;
            clearAttackBox();
        }

        if (--attackCounter <= 0) {
            attacking = false;
            attackPhase = 0;
            clearAttackBox(); // tắt hẳn hitbox khi kết thúc
            attackCooldownCounter = attackCooldown;
            onAttackEnd();
        }
    }

    public int[] getKnockbackVector() {
        int kbPower = 6; // lực đẩy
        switch (e.direction) {
            case "up":    return new int[]{0, -kbPower};
            case "down":  return new int[]{0,  kbPower};
            case "left":  return new int[]{-kbPower, 0};
            case "right": return new int[]{ kbPower, 0};
            default:      return new int[]{0, 0};
        }
    }
}
