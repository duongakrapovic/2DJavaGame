/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.List;
import input_manager.InputController;
import sound_manager.SoundManager;
import main.GamePanel;

public class Interact{
    private final GamePanel gp;
    private final Player player;
    private final InputController input;
 
    
    public Interact(GamePanel gp,Player player, InputController input ) {
        this.gp = gp;
        this.player = player;
        this.input = input;
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
                            if(input.isPicked()){
                                SoundManager.getInstance().playSE(SoundManager.SoundID.COIN);
                                player.hasKey++;
                                objects.remove(index); // xoá object
                                gp.ui.showMessage("Ya got a key");
                            
                            }
                            break;

                        case "portal":
                            gp.ui.showTouchMessage("press 'F' to tele", obj, gp);
                            if(input.isPicked()){
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
//        if (index != 999){
//            List<Entity> monsters = gp.em.getMonsters(gp.currentMap);;
//
//            if(index >= 0 && index < monsters.size()){
//                Entity monster = monsters.get(index);
//
//                if(monster != null && monster.mapIndex == gp.currentMap){
//                    String monsterName = monster.name;
//
//                    switch(monsterName){
//                        case "Green Slime":
//                            // xử lý đánh slime hoặc hội thoại
//                            break;
//                    }
//                }
//            }
//        }
    }
    
    public void InteractNPC(int index){
//        if (index != 999){
//            List<Entity> npcs = gp.em.getNPCs(gp.currentMap);
//
//            if(index >= 0 && index < npcs.size()){
//                Entity npc = npcs.get(index);
//
//                if(npc != null && npc.mapIndex == gp.currentMap){
//                    String npcName = npc.name;
//
//                    switch(npcName){
//                        case "Oldman":
//                            // mở hội thoại, quest...
//                            break;
//                    }
//                }
//            }
//        }
    }
}
