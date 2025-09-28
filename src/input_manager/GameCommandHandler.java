/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package input_manager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;
import sound_manager.SoundManager;
import main.GameState;

public class GameCommandHandler implements KeyListener {
    private final GamePanel gp;

    public GameCommandHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // start mode
        if(gp.gsm.getState() == GameState.START){
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
                    gp.gsm.setState(GameState.PLAY);
                    SoundManager.getInstance().playMusic(SoundManager.SoundID.MUSIC_THEME);
                }
                if(gp.ui.commandNum == 1){// quit
                    System.exit(0);
                }
            } 
        }

        // while in pause 
        if(gp.gsm.getState() == GameState.PAUSE){
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
                    gp.gsm.setState(GameState.PLAY);
                    SoundManager.getInstance().playMusic(SoundManager.SoundID.MUSIC_THEME);
                }
                if(gp.ui.commandNum == 1){ // Quit
                    System.exit(0);
                }
            }
        }
        // switch case of pause key 
        if( code == KeyEvent.VK_ESCAPE){
            if(gp.gsm.getState() == GameState.PLAY){
                gp.gsm.setState(GameState.PAUSE);
                SoundManager.getInstance().stopMusic();
            }
            else if(gp.gsm.getState()  == GameState.PAUSE){
                gp.gsm.setState(GameState.PLAY);
                SoundManager.getInstance().playMusic(SoundManager.SoundID.MUSIC_THEME);
            }
        }  
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}

