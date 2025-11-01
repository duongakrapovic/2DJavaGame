/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interact_manager.npc_interact;

import main.GamePanel;
import player_manager.Player;
import input_manager.InputController;

public class NPCInteract {
    private final GamePanel gp;
    private final Player player;
    private final InputController input;

    public NPCInteract(GamePanel gp, Player player, InputController input) {
        this.gp = gp;
        this.player = player;
        this.input = input;
    }

    public void handle(int index) {
        // TODO: xử lý NPC sau
    }
}

