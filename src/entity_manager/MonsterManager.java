package entity_manager;

import entity.Entity;
import main.GamePanel;
import monster_data.*;

import java.util.*;

/**
 * Quản lý quái + respawn theo điểm spawn cố định.
 * - Không dùng gp.player, nhận toạ độ player từ outside.
 * - Mỗi SpawnSlot = 1 chỗ spawn 1 con (respawn lại đúng chỗ đó).
 */
public class MonsterManager {
    private final GamePanel gp;

    // Quái đang sống theo map
    private final Map<Integer, List<Entity>> monstersByMap = new HashMap<>();

    // Tất cả điểm spawn của mọi map
    private final List<SpawnSlot> spawnSlots = new ArrayList<>();

    // Bật/tắt check “spawn xa player”
    // 👉 ĐỂ FALSE để test respawn cho chắc, sau này thích thì bật lại
    private final boolean useDistanceCheck = false;

    private static final boolean DEBUG_MONSTER = true;

    // ----------- Cấu trúc 1 slot spawn -----------
    private static class SpawnSlot {
        final int mapId;
        final int worldX, worldY;
        final String monsterId;       // "SLIME", "BAT", "ORC", "BOSS"...
        final long respawnDelayMs;    // thời gian hồi sinh

        Entity current;               // quái đang sống (null nếu slot trống)
        long lastDeathTime = 0L;      // lần cuối con này chết

        SpawnSlot(int mapId, int worldX, int worldY,
                  String monsterId, long respawnDelayMs) {
            this.mapId = mapId;
            this.worldX = worldX;
            this.worldY = worldY;
            this.monsterId = monsterId;
            this.respawnDelayMs = respawnDelayMs;
        }
    }

    public MonsterManager(GamePanel gp) {
        this.gp = gp;

        // Load toàn bộ chunk 1 lần để check được collision khi setup spawn
        gp.chunkM.loadAllChunksSync();

        setupSpawnSlots();  // khai báo toàn bộ bãi quái
        initialSpawn();     // spawn lứa đầu
    }

    // =====================================================
    // 1. KHAI BÁO SPAWN CHO TỪNG MAP
    // =====================================================

    private void setupSpawnSlots() {
        int t = gp.tileSize;  // = 96 px

        // ================= MAP 0: 49 con slime, nhưng chỉ ở tile không collision =================
        for (int ty = 18; ty <= 72; ty += 9) {
            for (int tx = 18; tx <= 72; tx += 9) {
                int wx = tx * t;
                int wy = ty * t;

                if (isBlockedTile(0, wx, wy)) {
                    if (DEBUG_MONSTER) {
                        System.out.println("[SPAWN_SLOT] Bỏ ("+tx+","+ty+") map 0 vì tile collision");
                    }
                    continue;
                }

                addSpawn(0, wx, wy, "SLIME", 15_000L);
            }
        }

        // ================= MAP 1: Orc + Bat + Boss (giãn ra) =================
        addSpawnIfFree(1,  2 * t,  2 * t, "ORC", 35_000);  // Pocket A
        addSpawnIfFree(1,  3 * t,  3 * t, "BAT", 25_000);

        addSpawnIfFree(1,  6 * t,  2 * t, "ORC", 40_000);  // Pocket B
        addSpawnIfFree(1,  7 * t,  3 * t, "BAT", 28_000);

        addSpawnIfFree(1,  9 * t,  2 * t, "BAT", 26_000);  // Pocket C
        addSpawnIfFree(1, 10 * t,  4 * t, "BAT", 26_000);

        // Boss ở khu riêng, tách ra một chút
        addSpawnIfFree(1,  8 * t,  4 * t, "BOSS", 600_000); // ~10 phút respawn

        // MAP 2: nhà, không có quái
    }

    private void addSpawnIfFree(int mapId, int wx, int wy,
                                String monsterId, long respawnDelayMs) {
        if (isBlockedTile(mapId, wx, wy)) {
            if (DEBUG_MONSTER) {
                System.out.println("[SPAWN_SLOT] Bỏ spawn " + monsterId +
                        " tại map " + mapId + " vì tile collision");
            }
            return;
        }
        addSpawn(mapId, wx, wy, monsterId, respawnDelayMs);
    }

    private void addSpawn(int mapId, int wx, int wy,
                          String monsterId, long respawnDelayMs) {
        SpawnSlot slot = new SpawnSlot(mapId, wx, wy, monsterId, respawnDelayMs);
        spawnSlots.add(slot);
        if (DEBUG_MONSTER) {
            System.out.println("[SPAWN_SLOT] + " + monsterId +
                    " @map=" + mapId + " x=" + wx + " y=" + wy +
                    " respawn=" + respawnDelayMs + "ms");
        }
    }

    // =====================================================
    // 2. SPAWN LÚC BẮT ĐẦU GAME + RESPAWN
    // =====================================================

    private void initialSpawn() {
        for (SpawnSlot slot : spawnSlots) {
            spawnNow(slot);
        }
    }

    private void spawnNow(SpawnSlot slot) {
        if (slot.current != null) return;

        Entity m = createMonster(slot.monsterId, slot.mapId);
        if (m == null) {
            if (DEBUG_MONSTER) {
                System.out.println("[SPAWN] createMonster NULL cho " + slot.monsterId);
            }
            return;
        }

        m.worldX = slot.worldX;
        m.worldY = slot.worldY;

        if (m instanceof monster_data.Monster monster) {
            monster.setHome(slot.worldX, slot.worldY);
        }

        monstersByMap
                .computeIfAbsent(slot.mapId, k -> new ArrayList<>())
                .add(m);

        slot.current = m;

        if (DEBUG_MONSTER) {
            String type = (slot.lastDeathTime == 0) ? "INIT" : "RESPAWN";
            System.out.println("[SPAWN " + type + "] " + slot.monsterId +
                    " @map=" + slot.mapId +
                    " x=" + slot.worldX + " y=" + slot.worldY);
        }
    }

    /**
     * Factory: tạo đúng loại quái theo id + map.
     */
    private Entity createMonster(String id, int mapId) {
        return switch (id) {
            case "SLIME" -> new RedSlimeMonster(gp, mapId);
            case "BAT"   -> new BatMonster(gp, mapId);
            case "ORC"   -> new OrcMonster(gp, mapId);
            case "BOSS"  -> new SkeletonLord(gp, mapId);
            default      -> null;
        };
    }

    // =====================================================
    // 3. API PUBLIC
    // =====================================================

    public List<Entity> getMonsters(int mapId) {
        return monstersByMap.getOrDefault(mapId, Collections.emptyList());
    }

    /**
     * Gọi từ GamePanel:
     * monsterManager.update(currentMap, player.worldX, player.worldY);
     */
    public void update(int mapId, int playerX, int playerY) {
        List<Entity> list = monstersByMap.get(mapId);
        if (list != null) {
            Iterator<Entity> it = list.iterator();
            while (it.hasNext()) {
                Entity e = it.next();
                e.update();

                if (isDead(e)) {
                    if (DEBUG_MONSTER) {
                        System.out.println("[DEAD] " + e.name + " @map=" + mapId +
                                " x=" + e.worldX + " y=" + e.worldY);
                    }
                    registerDeath(e);
                    it.remove();
                }
            }
        }

        handleRespawn(mapId, playerX, playerY);
    }

    public void draw(java.awt.Graphics2D g2, int mapId) {
        for (Entity m : getMonsters(mapId)) {
            m.draw(g2);
        }
    }

    // =====================================================
    // 4. CHẾT + RESPWAN
    // =====================================================

    /**
     * TODO: sửa hàm này cho đúng với logic chết của bạn.
     *
     * 👉 Nếu Entity có boolean alive:
     *      return !e.alive;
     *
     * 👉 Nếu Entity có hp:
     *      return e.hp <= 0;
     *
     * Đoạn dưới là ví dụ, bạn chỉnh cho khớp với Entity của bạn.
     */
    private boolean isDead(Entity e) {
        boolean dead = e.isDead();   // dùng hàm trong Entity (hp <= 0)

        if (DEBUG_MONSTER && dead) {
            System.out.println("[DEAD] " + e.name +
                    " hp=" + e.getHP() +
                    " (MonsterManager.isDead)");
        }

        return dead;
    }

    /** Khi quái chết, tìm slot chứa nó để đánh dấu lastDeathTime + giải phóng slot. */
    private void registerDeath(Entity e) {
        long now = System.currentTimeMillis();
        for (SpawnSlot slot : spawnSlots) {
            if (slot.current == e) {
                slot.current = null;
                slot.lastDeathTime = now;

                if (DEBUG_MONSTER) {
                    System.out.println("[DEATH_SLOT] " + slot.monsterId +
                            " slot freed @map=" + slot.mapId +
                            " x=" + slot.worldX + " y=" + slot.worldY +
                            " time=" + now);
                }
                break;
            }
        }
    }

    private void handleRespawn(int currentMapId, int playerX, int playerY) {
        long now = System.currentTimeMillis();

        for (SpawnSlot slot : spawnSlots) {
            if (slot.mapId != currentMapId) continue;    // chỉ xử lý map hiện tại
            if (slot.current != null) continue;          // slot đang có quái

            // chưa từng có quái chết ở slot này ⇒ bỏ qua respawn (initialSpawn đã lo)
            if (slot.lastDeathTime == 0L) continue;

            long waited = now - slot.lastDeathTime;
            if (waited < slot.respawnDelayMs) {
                if (DEBUG_MONSTER) {
                    System.out.println("[RESPAWN_WAIT] " + slot.monsterId +
                            " còn " + (slot.respawnDelayMs - waited) + "ms");
                }
                continue;
            }

            if (useDistanceCheck && !isFarFromPlayer(slot.worldX, slot.worldY, playerX, playerY)) {
                if (DEBUG_MONSTER) {
                    System.out.println("[RESPAWN_NEAR_PLAYER] " + slot.monsterId +
                            " @(" + slot.worldX + "," + slot.worldY + ")" +
                            " player=(" + playerX + "," + playerY + ")");
                }
                continue;
            }

            if (DEBUG_MONSTER) {
                System.out.println("[RESPAWN_OK] " + slot.monsterId + " -> spawnNow");
            }
            spawnNow(slot);
        }
    }

    /**
     * Đảm bảo quái không respawn quá sát player.
     * Nếu muốn bật lại, đặt useDistanceCheck = true.
     */
    private boolean isFarFromPlayer(int x, int y, int playerX, int playerY) {
        int dx = x - playerX;
        int dy = y - playerY;
        int safeRadius = gp.tileSize * 5; // tránh spawn trong bán kính 5 tile quanh player
        return dx * dx + dy * dy > safeRadius * safeRadius;
    }

    private boolean isBlockedTile(int mapId, int worldX, int worldY) {
        return gp.tileM.isCollisionAtWorld(worldX, worldY, gp.chunkM);
    }
}
