/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interact_manager.object_interact;

import object_data.WorldObject;
import player_manager.Player;
import input_manager.InputController;
import main.GamePanel;
import ui.MessageUI;
import java.util.List;

public class ObjectInteract {

    private final GamePanel gp;
    private final Player player;
    private final InputController input;
    private final MessageUI msgUI;

    public ObjectInteract(GamePanel gp, Player player, InputController input) {
        this.gp = gp;
        this.player = player;
        this.input = input;
        this.msgUI = gp.uiManager.get(MessageUI.class);
    }

    public void handle(int index) {
        if (index != 999) {
            List<WorldObject> objects = gp.em.getWorldObjects(gp.currentMap);
            if (index >= 0 && index < objects.size()) {
                WorldObject obj = objects.get(index);
                if (obj != null && obj.mapIndex == gp.currentMap) {
                    // gọi đúng handler tương ứng
                    IObjectInteraction handler = ObjectInteractionFactory.getHandler(obj.name);
                    if (handler != null) handler.interact(gp, player, input, obj);
                    else if (msgUI != null)
                        msgUI.showTouchMessage("Unknown object: " + obj.name, obj, gp);
                }
            }
        } else if (msgUI != null) {
            msgUI.hideTouchMessage();
        }
    }
}

