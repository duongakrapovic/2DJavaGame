package player_manager;

import main.GamePanel;
import object_data.WorldObject; // chỉ để rõ ràng; không dùng trực tiếp cũng OK

public class PlayerInteractor {
    private final Player player;
    private final GamePanel gp;

    public PlayerInteractor(Player player, GamePanel gp){
        this.player = player;
        this.gp = gp;
    }

    public void allCheck(int nextX, int nextY){
        gp.cChecker.checkTile(player, nextX, nextY);

        // TÍNH delta từ vị trí hiện tại -> vị trí kế tiếp
        int dx = nextX - player.worldX;
        int dy = nextY - player.worldY;

        // DÙNG API MỚI cho object (WorldObject)
        int objIndex = gp.cChecker.checkWorldObject(
                player,
                gp.om.getObjects(gp.currentMap),
                dx, dy
        );
        player.iR.InteractObject(objIndex);

        int monsterIndex = gp.cChecker.checkEntity(player, gp.em.getMonsters(gp.currentMap), nextX, nextY);
        player.iR.InteractMonster(monsterIndex);

        int npcIndex = gp.cChecker.checkEntity(player, gp.em.getNPCs(gp.currentMap), nextX, nextY);
        player.iR.InteractNPC(npcIndex);
    }
}
