/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package player_manager;

public class PlayerAnimation {
    private final Player player;
    private int counter = 0;

    public PlayerAnimation(Player player){this.player = player;}

    public void update(boolean isMoving){
        if(isMoving){
            counter++;
            if(counter > 8){
                player.spriteNum = (player.spriteNum==1) ? 2 : 1;
                counter = 0;
            }
        } else {
            counter++;
            if(counter > 15){
                counter = 0;
                player.spriteNum = 1;
            }
        }
    }
}

