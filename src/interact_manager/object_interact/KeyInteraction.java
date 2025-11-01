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

public class KeyInteraction implements IObjectInteraction {

    @Override
    public void interact(GamePanel gp, Player player, InputController input, WorldObject obj) {
        MessageUI msgUI = gp.uiManager.get(MessageUI.class);
        List<WorldObject> objects = gp.em.getWorldObjects(gp.currentMap);

        if (msgUI != null) msgUI.showTouchMessage("press 'F' to pick key", obj, gp);
        if (input.isPicked()) {
            SoundManager.getInstance().playSE(SoundManager.SoundID.COIN);
            player.hasKey++;
            objects.remove(obj);
            if (msgUI != null) msgUI.showTouchMessage("Ya got a key!", obj, gp);
        }
    }
}

