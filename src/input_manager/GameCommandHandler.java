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

        // ===== MAIN MENU =====
        if (gp.gsm.getState() == GameState.START) {

            // Case 1: NORMAL MENU (New Game / About / Quit)
            if (!gp.mainMenuUI.isShowingAbout()) {
                if (code == KeyEvent.VK_UP) {
                    gp.mainMenuUI.commandNum--;
                    if (gp.mainMenuUI.commandNum < 0) {
                        gp.mainMenuUI.commandNum = 2; // wrap around
                    }
                }
                if (code == KeyEvent.VK_DOWN) {
                    gp.mainMenuUI.commandNum++;
                    if (gp.mainMenuUI.commandNum > 2) {
                        gp.mainMenuUI.commandNum = 0; // wrap around
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    if (gp.mainMenuUI.commandNum == 0) {
                        // New Game
                        gp.gsm.setState(GameState.PLAY);
                        SoundManager.getInstance().playMusic(SoundManager.SoundID.MUSIC_THEME);
                    }
                    if (gp.mainMenuUI.commandNum == 1) {
                        // Open About screen
                        gp.toggleMainMenuAbout();
                        ;
                    }
                    if (gp.mainMenuUI.commandNum == 2) {
                        // Quit game
                        System.exit(0);
                    }
                }
            }
            // Case 2: ABOUT SCREEN (Only ENTER to go back)
            else {
                if (code == KeyEvent.VK_ENTER) {
                    gp.toggleMainMenuAbout();
                    ; // back to main menu
                }
            }
        }

        // ===== PAUSE MENU =====
        if (gp.gsm.getState() == GameState.PAUSE) {
            if (code == KeyEvent.VK_LEFT) {
                gp.pauseMenuUI.commandNum--;
                if (gp.pauseMenuUI.commandNum < 0) {
                    gp.pauseMenuUI.commandNum = 1;
                }
            }
            if (code == KeyEvent.VK_RIGHT) {
                gp.pauseMenuUI.commandNum++;
                if (gp.pauseMenuUI.commandNum > 1) {
                    gp.pauseMenuUI.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.pauseMenuUI.commandNum == 0) {
                    // Resume game
                    gp.gsm.setState(GameState.PLAY);
                    SoundManager.getInstance().playMusic(SoundManager.SoundID.MUSIC_THEME);
                }
                if (gp.pauseMenuUI.commandNum == 1) {
                    // Quit
                    System.exit(0);
                }
            }
        }

        // ===== ESC key → toggle pause =====
        if (code == KeyEvent.VK_ESCAPE) {
            if (gp.gsm.getState() == GameState.PLAY) {
                gp.gsm.setState(GameState.PAUSE);
                SoundManager.getInstance().stopMusic();
            } else if (gp.gsm.getState() == GameState.PAUSE) {
                gp.gsm.setState(GameState.PLAY);
                SoundManager.getInstance().playMusic(SoundManager.SoundID.MUSIC_THEME);
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
