/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package player_manager;

import main.GamePanel;

public class PlayerInteractor {
    private final Player player;
    private final GamePanel gp;

    public PlayerInteractor(Player player, GamePanel gp){
        this.player = player;
        this.gp = gp;
    }

    public void allCheck(int nextX, int nextY){
        gp.cChecker.checkTile(player, nextX, nextY);
        int objIndex = gp.cChecker.checkEntity(player, gp.em.getObjects(gp.currentMap), nextX, nextY);
        player.iR.InteractObject(objIndex);

        int monsterIndex = gp.cChecker.checkEntity(player, gp.em.getMonsters(gp.currentMap), nextX, nextY);
        player.iR.InteractMonster(monsterIndex);

        int npcIndex = gp.cChecker.checkEntity(player, gp.em.getNPCs(gp.currentMap), nextX, nextY);
        player.iR.InteractNPC(npcIndex);
    }
}

