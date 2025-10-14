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
                            gp.ui.showTouchMessage("press 'F' to pick key", obj, gp);
                            if (input.isPicked()) {
                                SoundManager.getInstance().playSE(SoundManager.SoundID.COIN);
                                player.hasKey++;
                                objects.remove(index); // xoá object ra khỏi map
                                gp.ui.showMessage("Ya got a key");
                            }
                            break;

                        case "portal":
                            gp.ui.showTouchMessage("press 'F' to tele", obj, gp);
                            if (input.isPicked()) {
                                gp.ui.startFade(() -> {
                                    if ("map1".equals(gp.chunkM.pathMap)) {
                                        gp.chunkM.pathMap = "map2";
                                        gp.chunkM.clearChunks();
                                        gp.chunkM.updateChunks(player.worldX, player.worldY);
                                        gp.ui.showMessage("Teleported to hell!");
                                    } else if ("map2".equals(gp.chunkM.pathMap)) {
                                        gp.chunkM.pathMap = "map1";
                                        gp.chunkM.clearChunks();
                                        gp.chunkM.updateChunks(player.worldX, player.worldY);
                                        gp.ui.showMessage("Teleported to jungle!");
                                    }
                                });
                            }
                            break;

                        default:
                            // các object khác nếu có
                            break;
                    }
                }
            }
        } else {
            gp.ui.hideTouchMessage();
        }
    }

    public void InteractMonster(int index) {
        // tuỳ bạn bật lại sau
    }

    public void InteractNPC(int index) {
        // tuỳ bạn bật lại sau
    }
}
