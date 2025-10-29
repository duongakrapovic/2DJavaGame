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

public class Interact {
    private final GamePanel gp;
    private final Player player;
    private final InputController input;

    public Interact(GamePanel gp, Player player, InputController input) {
        this.gp = gp;
        this.player = player;
        this.input = input;
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
                        gp.messageUI.showTouchMessage("press 'F' to pick key", obj, gp);
                        if (input.isPicked()) {
                            SoundManager.getInstance().playSE(SoundManager.SoundID.COIN);
                            player.hasKey++;
                            objects.remove(index); // xoá object ra khỏi map
                            gp.messageUI.showMessage("Ya got a key!");
                        }
                        break;
                    case "manaposion":
                        gp.messageUI.showTouchMessage("press 'F' to heal mana", obj, gp);
                        if (input.isPicked()) {
                            SoundManager.getInstance().playSE(SoundManager.SoundID.COIN);
                            objects.remove(index); // xoá object ra khỏi map
                            gp.messageUI.showMessage("full fuel!");
                        }
                        break;
                    case "healthposion":
                        gp.messageUI.showTouchMessage("press 'F' to heal health", obj, gp);
                        if (input.isPicked()) {
                            SoundManager.getInstance().playSE(SoundManager.SoundID.COIN);
                            objects.remove(index); // xoá object ra khỏi map
                            gp.messageUI.showMessage("That close!");
                        }
                        break;    
                    case "portal":
                        gp.messageUI.showTouchMessage("press 'F' to tele", obj, gp);
                        if (input.isPicked()) {
                            gp.fadeUI.startFade(() -> {
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

                                gp.messageUI.showMessage("Teleported!");
                            });
                        }
                        break;
                    case "door":
                        gp.messageUI.showTouchMessage("press 'F' to shopping", obj, gp);
                        if (input.isPicked()) {
                            gp.fadeUI.startFade(() -> {
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

                                gp.messageUI.showMessage("shop!");
                            });
                        }
                        break;
                    }
                }
            }
        } else {
            gp.messageUI.hideTouchMessage();
        }
    }

    public void InteractMonster(int index) {
        // optional
    }

    public void InteractNPC(int index) {
        // optional
    }
}
