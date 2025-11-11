/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package input_manager;


public interface InputController {
    boolean isUpPressed();
    boolean isDownPressed();
    boolean isLeftPressed();   
    boolean isRightPressed();
    boolean isPicked();
    boolean isAttackPressed();
    boolean isTalkPressed();
    void resetTalkKey();
}
