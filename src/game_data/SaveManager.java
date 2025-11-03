package game_data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import player_manager.Player;
import monster_data.Monster;
import entity_manager.EntityManager;
import main.GamePanel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * SaveManager.java
 * Handles saving/loading Player + Monster + Map data.
 * Fully compatible with your current EntityManager structure.
 */
public class SaveManager {

    private static final String SAVE_DIR = "saves";
    private static final String SAVE_FILE = "savegame.json";
    private final Gson gson;

    public SaveManager() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) dir.mkdir();
    }

    /** Save current game state */
    public void saveGame(GamePanel gp) {
        try {
            // ==== PLAYER ====
            Player player = gp.em.getPlayer();
            if (player == null) {
                System.err.println("[SaveManager] Player not found!");
                return;
            }

            List<InventoryData> inventoryList = new ArrayList<>();


            PlayerData playerData = new PlayerData(
                    player.worldX,
                    player.worldY,
                    player.getHP(),
                    player.getMaxHP(),
                    (player.getCurrentWeapon() != null ? player.getCurrentWeapon().name : null),
                    gp.currentMap
            );


            // ==== MONSTERS ====
            List<ObjectData> monsterList = new ArrayList<>();
            EntityManager em = gp.em;
            if (em.getMonsters(gp.currentMap) != null) {
                for (var entity : em.getMonsters(gp.currentMap)) {
                    if (entity instanceof Monster mon) {
                        monsterList.add(new ObjectData(
                                mon.name,
                                mon.worldX,
                                mon.worldY,
                                !mon.isDead()   // nếu chưa chết => active = true
                        ));
                    }
                }
            }

            // ==== MAP INFO ====
            int mapIndex = gp.currentMap;
            String mapPath = gp.chunkM.pathMap;

            GameData data = new GameData(playerData, monsterList, mapIndex, mapPath);

            // ==== WRITE FILE ====
            String json = gson.toJson(data);
            Files.write(Paths.get(SAVE_DIR, SAVE_FILE), json.getBytes());
            System.out.println("[SaveManager] Game saved successfully.");

        } catch (IOException e) {
            System.err.println("[SaveManager] Error writing save file.");
            e.printStackTrace();
        }
    }

    /** Load game state */
    public void loadGame(GamePanel gp) {
        try {
            File saveFile = new File(SAVE_DIR, SAVE_FILE);
            if (!saveFile.exists()) {
                System.out.println("[SaveManager] No save file found.");
                return;
            }

            String json = new String(Files.readAllBytes(saveFile.toPath()));
            GameData data = gson.fromJson(json, GameData.class);
            if (data == null) {
                System.err.println("[SaveManager] Save file corrupted or empty.");
                return;
            }

            // ==== RESTORE PLAYER ====
            Player player = gp.em.getPlayer();
            if (player != null && data.player != null) {
                player.worldX = data.player.worldX;
                player.worldY = data.player.worldY;
                player.setHP(data.player.health);
                player.setStats(data.player.maxHealth, player.getATK(), player.getDEF());
                player.setHP(data.player.health);
                // Restore map
                gp.currentMap = data.player.mapIndex;
                String newMap = "map" + gp.currentMap;

                gp.chunkM.loadMap(newMap);
                gp.em.update(gp.currentMap);

            }



            // ==== RESTORE MONSTERS ====
            var monsters = gp.em.getMonsters(gp.currentMap);
            if (monsters != null && data.objects != null) {
                for (int i = 0; i < Math.min(monsters.size(), data.objects.size()); i++) {
                    var entity = monsters.get(i);
                    var saved = data.objects.get(i);
                    if (entity instanceof Monster mon) {
                        mon.worldX = saved.worldX;
                        mon.worldY = saved.worldY;
                        if (!saved.active) {
                            mon.setHP(0); // chết (hp=0)
                        } else {
                            mon.revive(); // hồi sinh
                        }
                    }
                }
            }

            // ==== RESTORE MAP ====
            gp.currentMap = data.mapIndex;
            if (data.mapPath != null) gp.chunkM.pathMap = data.mapPath;

            System.out.println("[SaveManager] Game loaded successfully.");

        } catch (IOException e) {
            System.err.println("[SaveManager] Error reading save file.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[SaveManager] Unexpected error while loading.");
            e.printStackTrace();
        }
    }
}
