/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
    GamePanel gp;
    public boolean upPressed , downPressed, leftPressed, rightPressed;
    public boolean pickPress , pickPressOnce;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }
    
    // default must be public 
    @Override
    public void keyTyped(KeyEvent e){
        
    }
    @Override
    public void keyPressed(KeyEvent e){
        
        int code = e.getKeyCode();
        // start mode
        if(gp.gameState == gp.gameStart){
            if( code == KeyEvent.VK_UP){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0){
                    gp.ui.commandNum = 1    ;
                }
            } 
            if( code == KeyEvent.VK_DOWN){
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 1){
                    gp.ui.commandNum = 0;
                }
            } 
            if( code == KeyEvent.VK_ENTER){
                
                if(gp.ui.commandNum == 0){// new game 
                    gp.gameState = gp.gamePlay;
                    gp.playMusic(0);
                }
                if(gp.ui.commandNum == 1){// quit
                    System.exit(0);
                }
            } 
        }
        // play mode
        if(gp.gameState == gp.gamePlay){
            if( code == KeyEvent.VK_W){
                upPressed = true;
            } 
            if( code == KeyEvent.VK_S){
                downPressed = true;
            } 
            if( code == KeyEvent.VK_A){
                leftPressed = true;
            } 
            if( code == KeyEvent.VK_D){
                rightPressed = true;
            } 
            if( code == KeyEvent.VK_F){
                if(!pickPress){
                    pickPressOnce = true;
                }
                pickPress = true;
            }
              
        }
        // while in pause 
        if(gp.gameState == gp.gamePause){
            if( code == KeyEvent.VK_UP){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0){
                    gp.ui.commandNum = 1;
                }
            } 
            if( code == KeyEvent.VK_DOWN){
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 1){
                    gp.ui.commandNum = 0;
                }
            } 
            if( code == KeyEvent.VK_ENTER){
                if(gp.ui.commandNum == 0){ // Resume
                    gp.gameState = gp.gamePlay;
                    gp.playMusic(0);
                }
                if(gp.ui.commandNum == 1){ // Quit
                    System.exit(0);
                }
            }
        }
        // switch case of pause key 
        if( code == KeyEvent.VK_ESCAPE){
            if(gp.gameState == gp.gamePlay){
                gp.gameState = gp.gamePause;
                gp.music.stop();
            }
            else if(gp.gameState == gp.gamePause){
                gp.gameState = gp.gamePlay;
                gp.playMusic(0);
            }
        }      
    }
    @Override
    public void keyReleased(KeyEvent e){
        int code = e.getKeyCode();
        if( code == KeyEvent.VK_W){
            upPressed = false;
        } 
        if( code == KeyEvent.VK_S){
            downPressed = false;
        } 
        if( code == KeyEvent.VK_A){
            leftPressed = false;
        } 
        if( code == KeyEvent.VK_D){
            rightPressed = false;
        } 
        if( code == KeyEvent.VK_K){
            pickPress = false;
        }
        if( code == KeyEvent.VK_F){
            pickPress = false;
        }
    }
}
