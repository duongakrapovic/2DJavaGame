/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interact_manager.object_interact;

import main.GamePanel;
import object_data.WorldObject;
import player_manager.Player;
import input_manager.InputController;
import ui.effects.MessageUI;
import ui.effects.FadeUI;

public class PortalInteraction implements IObjectInteraction {

    @Override
    public void interact(GamePanel gp, Player player, InputController input, WorldObject obj) {
        MessageUI msgUI = gp.uiManager.get(MessageUI.class);
        FadeUI fadeUI = gp.uiManager.get(FadeUI.class);

        if (msgUI != null) msgUI.showTouchMessage("press 'F' to tele", obj, gp);
        if (input.isPicked()) {
            if (fadeUI != null) fadeUI.startFade(() -> {
                if ("map0".equals(gp.chunkM.pathMap)) {
                    gp.chunkM.pathMap = "map1";
                    gp.currentMap = 1;
                } else if ("map1".equals(gp.chunkM.pathMap)) {
                    gp.chunkM.pathMap = "map0";
                    gp.currentMap = 0;
                }

                var destList = gp.om.getObjects(gp.currentMap);
                WorldObject dest = null;
                for (var wo : destList) {
                    if (wo != null && "portal".equals(wo.name)) {
                        dest = wo;
                        break;
                    }
                }
                if (dest != null) {
                    gp.em.getPlayer().worldX = dest.worldX;
                    gp.em.getPlayer().worldY = dest.worldY + gp.tileSize;
                    gp.em.getPlayer().mapIndex = gp.currentMap;
                }

                gp.chunkM.clearChunks();
                gp.chunkM.updateChunks(gp.em.getPlayer().worldX, gp.em.getPlayer().worldY);

                if (msgUI != null) msgUI.showTouchMessage("Teleported!", obj, gp);
            });
        }
    }
}

