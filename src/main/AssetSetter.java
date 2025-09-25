/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;


import object.ObjectKey;
import object.ObjectPortal;
import object.ObjectDoor;

import monster.SlimeMonster;

import NPC.NPC_Oldman;
import NPC.NPC_Frog;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }
    public void setObject(){
        // map1;
        gp.obj[0][0] = new ObjectKey(gp);
        gp.obj[0][0].worldX = 20 * gp.tileSize;
        gp.obj[0][0].worldY = 20 * gp.tileSize;
        
        gp.obj[0][1] = new ObjectPortal(gp);
        gp.obj[0][1].worldX = 23 * gp.tileSize;
        gp.obj[0][1].worldY = 23 * gp.tileSize;
        
        gp.obj[0][2] = new ObjectDoor(gp);
        gp.obj[0][2].worldX = 22 * gp.tileSize;
        gp.obj[0][2].worldY = 26 * gp.tileSize;
        
        gp.obj[0][3] = new ObjectDoor(gp);
        gp.obj[0][3].worldX = 20 * gp.tileSize;
        gp.obj[0][3].worldY = 24 * gp.tileSize;
        
        // map2
        gp.obj[1][0] = new ObjectKey(gp);
        gp.obj[1][0].worldX = 23 * gp.tileSize;
        gp.obj[1][0].worldY = 20 * gp.tileSize;
        
        gp.obj[1][1] = new ObjectPortal(gp);
        gp.obj[1][1].worldX = 25 * gp.tileSize;
        gp.obj[1][1].worldY = 23 * gp.tileSize;
    }
    public void setMonster(){
        //map1
        gp.monster[0][0] = new SlimeMonster(gp);
        gp.monster[0][0].worldX = 20 * gp.tileSize;
        gp.monster[0][0].worldY = 23 * gp.tileSize;
        //map2
        gp.monster[1][0] = new SlimeMonster(gp);
        gp.monster[1][0].worldX = 20 * gp.tileSize;
        gp.monster[1][0].worldY = 23 * gp.tileSize;
    }
    public void setNPC(){
        //map1
        gp.npc[0][0] = new NPC_Oldman(gp);
        gp.npc[0][0].worldX = 23 * gp.tileSize;
        gp.npc[0][0].worldY = 30 * gp.tileSize;
        
        gp.npc[0][1] = new NPC_Frog(gp);
        gp.npc[0][1].worldX = 30 * gp.tileSize;
        gp.npc[0][1].worldY = 30 * gp.tileSize;
        //map2
        gp.npc[1][0] = new NPC_Oldman(gp);
        gp.npc[1][0].worldX = 23 * gp.tileSize;
        gp.npc[1][0].worldY = 30 * gp.tileSize;
        
        gp.npc[1][1] = new NPC_Frog(gp);
        gp.npc[1][1].worldX = 30 * gp.tileSize;
        gp.npc[1][1].worldY = 30 * gp.tileSize;
    }
}
