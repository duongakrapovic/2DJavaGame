/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interact_manager;

import object_data.WorldObject;  // <<- dùng WorldObject thay vì Entity
import player_manager.Player;
import java.util.List;
import input_manager.InputController;
import sound_manager.SoundManager;
import main.GamePanel;
import ui.MessageUI;
import ui.FadeUI;




public class Interact {
    private final GamePanel gp;
    private final Player player;
    private final InputController input;
    private MessageUI msgUI;
    private FadeUI fadeUI;

    public Interact(GamePanel gp, Player player, InputController input) {
        this.gp = gp;
        this.player = player;
        this.input = input;

        this.msgUI = gp.uiManager.get(MessageUI.class);
        this.fadeUI = gp.uiManager.get(FadeUI.class);
    }

    // OBJECT
    public void InteractObject(int index) {

        if (index != 999) {
            // Lấy object theo map hiện tại
            List<WorldObject> objects = gp.em.getWorldObjects(gp.currentMap); // <<- đổi API

            if (index >= 0 && index < objects.size()) {
                WorldObject obj = objects.get(index);
                if (obj != null && obj.mapIndex == gp.currentMap) {
                    String objectName = obj.name;

                    switch (objectName) {
                    case "key":
                        if (msgUI != null) msgUI.showTouchMessage("press 'F' to pick key", obj, gp);
                        if (input.isPicked()) {
                            SoundManager.getInstance().playSE(SoundManager.SoundID.COIN);
                            player.hasKey++;
                            objects.remove(index); // xoá object ra khỏi map
                            if (msgUI != null) msgUI.showTouchMessage("Ya got a key!", obj, gp);
                        }
                        break;
                    case "manaposion":
                        if (msgUI != null) msgUI.showTouchMessage("press 'F' to heal mana", obj, gp);
                        if (input.isPicked()) {
                            SoundManager.getInstance().playSE(SoundManager.SoundID.COIN);
                            objects.remove(index); // xoá object ra khỏi map
                            if (msgUI != null) msgUI.showTouchMessage("full fuel!", obj, gp);
                        }
                        break;
                    case "healthposion":
                        if (msgUI != null) msgUI.showTouchMessage("press 'F' to heal health", obj, gp);
                        if (input.isPicked()) {
                            SoundManager.getInstance().playSE(SoundManager.SoundID.COIN);
                            objects.remove(index); // xoá object ra khỏi map
                            if (msgUI != null) msgUI.showTouchMessage("That close!", obj, gp);
                        }
                        break;    
                    case "portal":
                        if (msgUI != null) msgUI.showTouchMessage("press 'F' to tele", obj, gp);
                        if (input.isPicked()) {
                            if (fadeUI != null) fadeUI.startFade(() -> {
                                if ("map0".equals(gp.chunkM.pathMap)) {
                                    gp.chunkM.pathMap = "map1";
                                    gp.currentMap = 1;                         // <<< QUAN TRỌNG
                                } else if ("map1".equals(gp.chunkM.pathMap)) {
                                    gp.chunkM.pathMap = "map0";
                                    gp.currentMap = 0;                         // <<< QUAN TRỌNG
                                }

                                // teleport to other map's portal
                                var destList = gp.em.getWorldObjects(gp.currentMap);
                                WorldObject dest = null;
                                for (var wo : destList) { if (wo != null && "portal".equals(wo.name)) { dest = wo; break; } }
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
                        break;
                    case "door":
                        if (msgUI != null) msgUI.showTouchMessage("press 'F' to shopping", obj, gp);
                        if (input.isPicked()) {
                            if (fadeUI != null) fadeUI.startFade(() -> {
                                if ("map0".equals(gp.chunkM.pathMap)) {
                                    gp.chunkM.pathMap = "map3";
                                    gp.currentMap = 3;                         // <<< QUAN TRỌNG
                                } else if ("map3".equals(gp.chunkM.pathMap)) {
                                    gp.chunkM.pathMap = "map0";
                                    gp.currentMap = 0;                         // <<< QUAN TRỌNG
                                }

                                // teleport to other map's portal
                                var destList = gp.em.getWorldObjects(gp.currentMap);
                                WorldObject dest = null;
                                for (var wo : destList) { if (wo != null && "door".equals(wo.name)) { dest = wo; break; } }
                                if (dest != null) {
                                    gp.em.getPlayer().worldX = dest.worldX;
                                    gp.em.getPlayer().worldY = dest.worldY;
                                    gp.em.getPlayer().mapIndex = gp.currentMap;
                                }

                                gp.chunkM.clearChunks();
                                gp.chunkM.updateChunks(gp.em.getPlayer().worldX, gp.em.getPlayer().worldY);

                                if (msgUI != null) msgUI.showTouchMessage("shop!", obj, gp);
                            });
                        }
                        break;
                    }
                }
            }
        } else {
            if (msgUI != null) msgUI.hideTouchMessage();
        }
    }

    public void InteractMonster(int index) {
        // optional
    }

    public void InteractNPC(int index) {
        // optional
    }
}
