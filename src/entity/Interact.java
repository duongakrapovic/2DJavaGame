/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.*;

import main.GamePanel;
import main.KeyHandler;
import sound_manager.SoundManager;

public class Interact extends Entity{
    GamePanel gp;
    KeyHandler keyH;
    Player player;
    
    public Interact(GamePanel gp , KeyHandler keyH, Player player){
        super(gp);
        this.gp = gp;
        this.keyH = keyH;
        this.player = player;
    }
    //  OBJECT
    public void InteractObject(int index){
        if (index != 999){
            List<Entity> objects = gp.em.getObjects(gp.currentMap);

            if(index >= 0 && index < objects.size()){
                Entity obj = objects.get(index);

                if(obj != null && obj.mapIndex == gp.currentMap){
                    String objectName = obj.name;

                    switch(objectName){
                        case "key":
                            gp.ui.showTouchMessage("press 'F' to pick key", obj, gp);
                            if(keyH.pickPressOnce){
                                SoundManager.getInstance().playSE(SoundManager.SoundID.COIN);
                                player.hasKey++;
                                objects.remove(index); // xoá object
                                gp.ui.showMessage("Ya got a key");
                                keyH.pickPress = false;
                            }
                            break;

                        case "portal":
                            gp.ui.showTouchMessage("press 'F' to tele", obj, gp);
                            if(keyH.pickPressOnce){
                                gp.ui.startFade(() -> {
                                    if("map1".equals(gp.chunkM.pathMap)){
                                        gp.chunkM.pathMap = "map2";
                                        gp.chunkM.clearChunks();
                                        gp.chunkM.updateChunks(player.worldX, player.worldY);
                                        gp.ui.showMessage("Teleported to hell!");
                                    }
                                    else if("map2".equals(gp.chunkM.pathMap)){
                                        gp.chunkM.pathMap = "map1";
                                        gp.chunkM.clearChunks();
                                        gp.chunkM.updateChunks(player.worldX, player.worldY);
                                        gp.ui.showMessage("Teleported to jungle!");
                                    }
                                });
                                keyH.pickPress = false;
                            }
                            break;
                    }
                }
            }
        }
        else{
            gp.ui.hideTouchMessage();
        }
    }

    
    public void InteractMonster(int index){
        if (index != 999){
            List<Entity> monsters = gp.em.getMonsters(gp.currentMap);;

            if(index >= 0 && index < monsters.size()){
                Entity monster = monsters.get(index);

                if(monster != null && monster.mapIndex == gp.currentMap){
                    String monsterName = monster.name;

                    switch(monsterName){
                        case "Green Slime":
                            // xử lý đánh slime hoặc hội thoại
                            break;
                    }
                }
            }
        }
    }
    
    public void InteractNPC(int index){
        if (index != 999){
            List<Entity> npcs = gp.em.getNPCs(gp.currentMap);

            if(index >= 0 && index < npcs.size()){
                Entity npc = npcs.get(index);

                if(npc != null && npc.mapIndex == gp.currentMap){
                    String npcName = npc.name;

                    switch(npcName){
                        case "Oldman":
                            // mở hội thoại, quest...
                            break;
                    }
                }
            }
        }
    }
}
