/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import main.GamePanel;
import main.KeyHandler;

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
    public void InteractObject(int index){
        int i = index;
        if (i != 999){
            String objectName = gp.obj[gp.currentMap][i].name;
            Entity obj = gp.obj[gp.currentMap][i];
            
            switch(objectName){
                case "key":
                gp.ui.showTouchMessage("press 'F' to pick key", obj, gp);
                if(keyH.pickPressOnce == true){
                    gp.playSE(1);
                    player.hasKey++;
                    gp.obj[gp.currentMap][i] = null;
                    gp.ui.showMessage("Ya got a key");
                    keyH.pickPress = false;
                }
                break;
                case "portal":
                gp.ui.showTouchMessage("press 'F' to tele", obj, gp);
                if(keyH.pickPressOnce == true){
                    // use ".equal" to compairthe content of the string 
                    // == only compair memory address
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
        else{
             gp.ui.hideTouchMessage();
        }
    }
    
    public void InteractMonster(int index){
        int i = index;
        if (i != 999){
            String monsterName = gp.monster[gp.currentMap][i].name;
            Entity monster = gp.obj[gp.currentMap][i];
            
            switch(monsterName){
                case "Green Slime":
                break;
                  
            }
        }
    }
    
    public void InteractNPC(int index){
        int i = index;
        if (i != 999){
            String npcName = gp.npc[gp.currentMap][i].name;
            Entity npc = gp.npc[gp.currentMap][i];
            
            switch(npcName){
                case "Oldman":
                break;
                  
            }
        }
    }
    
}
