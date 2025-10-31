/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package player_manager;

import main.GamePanel;

public class PlayerMovement {
    private final Player player;
    private final PlayerInteractor pi;
    public PlayerMovement(Player player, GamePanel gp){
        this.player = player;
        this.pi = new PlayerInteractor(player, gp);
    }

    public int[] calculateMovement(){
        // in java , the top left corner is x = 0 and y = 0;
        // x valuaes increase to the right
        // y valuaes increase as they go down
        //System.out.println("done");
        int deltaMoveX = 0;
        int deltaMoveY = 0;
        if(player.input.isUpPressed() == true || player.input.isDownPressed() == true ||
            player.input.isLeftPressed() == true || player.input.isRightPressed() == true){

            if(player.input.isUpPressed() == true){//w
                player.direction = "up";
                deltaMoveY -= player.actualSpeed;
                //System.out.println(deltaMoveX);
            }
            if(player.input.isDownPressed() == true){//s
                player.direction = "down";
                deltaMoveY += player.actualSpeed;
            }
            if(player.input.isLeftPressed() == true){//a
                player.direction = "left"; 
                deltaMoveX -= player.actualSpeed;
            }
            if(player.input.isRightPressed()== true){//d
                player.direction = "right";
                deltaMoveX += player.actualSpeed;
            }
            
            /* there is a problem that when player go diagonally , the speed will be
            speed * sqrt(2). therefore, we have to normalize movement vector,
            the true position of charactor should be lower than before, 
            */
            // normalize diagonal
            double length = Math.sqrt(deltaMoveX*deltaMoveX + deltaMoveY*deltaMoveY);
            if(length != 0){
                deltaMoveX = (int)Math.round(deltaMoveX/length * player.actualSpeed);
                deltaMoveY = (int)Math.round(deltaMoveY/length * player.actualSpeed);
            }
        }
        return new int[]{deltaMoveX, deltaMoveY};
    }
    
    public boolean isMoving(){
        return player.input.isUpPressed() || player.input.isDownPressed() ||
               player.input.isLeftPressed() || player.input.isRightPressed();
    }
    
    public void move(int dx, int dy){
        /* 
        there is a problem , If the position touches the boundary, it is impossible 
        to press 2 keys to move diagonally and the result is to move in a 
        normal direction. therefore, valriable collisionOn is for static object and tiles.
        if we use that val for both coordinates x and y , we can not move in the most 
        possible direction. I use 2 val called collisionXOn collisionYOn for the movement
        of player so i can calculate the posible future move. 
        
        -> we have to predict the path that the player can move.
        */ 
        
        // X
        player.collisionOn=false;
        player.collisionXOn=false;      
        int nextX = player.worldX + dx;
        int nextY = player.worldY;
        pi.allCheck(nextX, nextY);
        if(!player.collisionXOn && !player.collisionOn)
            player.worldX = nextX;
        //System.out.println(player.worldX);
        // Y
        player.collisionOn=false;
        player.collisionYOn=false;
        
        nextX = player.worldX;
        nextY = player.worldY + dy;
        pi.allCheck(nextX, nextY);
        if(!player.collisionYOn && !player.collisionOn) 
            player.worldY = nextY;
        //System.out.println( player.worldY);
    }
}

