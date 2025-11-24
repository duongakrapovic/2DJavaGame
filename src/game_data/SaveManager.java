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

public class SaveManager {
    private static final String SAVE_DIR = "saves";
    private static final String SAVE_FILE = "savegame.json";
    private final Gson gson;

    public SaveManager() {
        gson = new GsonBuilder().setPrettyPrinting().create();

        // tạo thư mục save nếu chưa tồn tại
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) dir.mkdir();
    }
    // Lưu game vào JSON
    public void saveGame(GamePanel gp) {
        try {
            // --- Player ---
            Player player = gp.em.getPlayer();
            if (player == null) {
                System.err.println("[SaveManager] Player not found!");
                return;
            }
            PlayerData playerData = new PlayerData(
                    player.worldX,
                    player.worldY,
                    player.getHP(),
                    player.getMaxHP(),
                    (player.getCurrentWeapon() != null ? player.getCurrentWeapon().name : null),
                    gp.currentMap,
                    player.getExp(),
                    player.getLevel()
            );
            // --- Monsters ---
            List<ObjectData> monsterList = new ArrayList<>();
            EntityManager em = gp.em;

            if (em.getMonsters(gp.currentMap) != null) {
                for (var entity : em.getMonsters(gp.currentMap)) {
                    if (entity instanceof Monster mon) {
                        monsterList.add(new ObjectData(
                                mon.name,
                                mon.worldX,
                                mon.worldY,
                                !mon.isDead() // active = còn sống
                        ));
                    }
                }
            }
            // --- Map ---
            int mapIndex = gp.currentMap;
            String mapPath = gp.chunkM.pathMap;

            GameData data = new GameData(playerData, monsterList, mapIndex, mapPath);
            // --- Write JSON ---
            String json = gson.toJson(data);
            Files.write(Paths.get(SAVE_DIR, SAVE_FILE), json.getBytes());

            System.out.println("[SaveManager] Game saved successfully.");

        } catch (IOException e) {
            System.err.println("[SaveManager] Error writing save file.");
            e.printStackTrace();
        }
    }
    // Load game từ JSON
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
                System.err.println("[SaveManager] Save file corrupted.");
                return;
            }
            // --- Restore Player ---
            Player player = gp.em.getPlayer();
            if (player != null && data.player != null) {
                // vị trí
                player.worldX = data.player.worldX;
                player.worldY = data.player.worldY;

                // exp & level
                player.setExp(data.player.exp);
                player.setLevel(data.player.level);

                // chỉ số + HP
                player.setStats(data.player.maxHealth, player.getATK(), player.getDEF());
                player.setHP(data.player.health);

                // --- vũ khí ---
                if (data.player.weaponName != null) {
                    object_data.weapons.Weapon w = null;

                    switch (data.player.weaponName) {
                        case "Leviathan Axe" ->
                                w = new object_data.weapons.Axe(gp, data.mapIndex);
                        case "Argonaut Hero's Sword" ->
                                w = new object_data.weapons.Sword(gp, data.mapIndex);
                        case "Steve Pick" ->
                                w = new object_data.weapons.Pick(gp, data.mapIndex);
                    }

                    if (w != null) {
                        player.setCurrentWeapon(w);
                        player.equipWeapon(w);
                    }
                }
                // --- Map ---
                gp.currentMap = data.player.mapIndex;
                player.mapIndex = gp.currentMap;

                String newMap = "map" + gp.currentMap;
                gp.chunkM.loadMap(newMap);

                if (gp.om != null)
                    gp.om.reloadMapObjects(gp.currentMap);

                gp.em.update(gp.currentMap);
            }
            // --- Restore Monsters ---
            var monsters = gp.em.getMonsters(gp.currentMap);
            if (monsters != null && data.objects != null) {
                for (int i = 0; i < Math.min(monsters.size(), data.objects.size()); i++) {
                    var entity = monsters.get(i);
                    var saved = data.objects.get(i);

                    if (entity instanceof Monster mon) {
                        mon.worldX = saved.worldX;
                        mon.worldY = saved.worldY;

                        if (!saved.active)
                            mon.setHP(0);
                        else
                            mon.revive();
                    }
                }
            }
            // Khởi tạo lại Interact
            if (gp.em != null && gp.em.getPlayer() != null) {
                gp.iR = new interact_manager.Interact(
                        gp,
                        gp.em.getPlayer(),
                        gp.em.getPlayer().input
                );
            }
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
