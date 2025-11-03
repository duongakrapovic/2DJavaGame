/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interact_manager.object_interact;

import main.GamePanel;
import object_data.WorldObject;
import player_manager.Player;
import input_manager.InputController;

public interface IObjectInteraction {
    void interact(GamePanel gp, Player player, InputController input, WorldObject obj);
}

