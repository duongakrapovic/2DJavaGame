/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interact_manager.object_interact;

import main.GamePanel;
import object_data.WorldObject;
import player_manager.Player;
import input_manager.InputController;
import sound_manager.SoundManager;
import ui.MessageUI;
import java.util.List;

public class HealthPosionInteraction implements IObjectInteraction {

    @Override
    public void interact(GamePanel gp, Player player, InputController input, WorldObject obj) {
        MessageUI msgUI = gp.uiManager.get(MessageUI.class);
        List<WorldObject> objects = gp.em.getWorldObjects(gp.currentMap);

        if (msgUI != null) msgUI.showTouchMessage("press 'F' to heal health", obj, gp);
        if (input.isPicked()) {
            SoundManager.getInstance().playSE(SoundManager.SoundID.COIN);
            objects.remove(obj);
            if (msgUI != null) msgUI.showTouchMessage("That close!", obj, gp);
            // Nếu có hệ thống máu thì thêm dòng hồi máu ở đây
            // player.health = Math.min(player.maxHealth, player.health + 50);
        }
    }
}

