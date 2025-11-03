package input_manager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;
import sound_manager.SoundManager;
import main.GameState;
import ui.MainMenuUI;
import ui.MessageUI;

/**
 * GameCommandHandler.java
 * Handles global keyboard commands (ESC, ENTER, arrows, etc.)
 * for major game states:
 * - Main menu (START)
 * - Gameplay (PLAY)
 * - Pause state (PAUSE)
 *
 * This class does NOT control player movement or combat actions;
 * those are handled in KeyHandler.
 */
public class GameCommandHandler implements KeyListener {

    /** Reference to the main GamePanel instance. */
    private final GamePanel gp;

    /** Constructor: attach to the current GamePanel. */
    public GameCommandHandler(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Called when a key is pressed.
     * Executes different logic depending on the current GameState.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // ===========================================
        // ======= MAIN MENU (START STATE) ===========
        // ===========================================
        if (gp.gsm.getState() == GameState.START) {

            // Get MainMenuUI through UIManager
            MainMenuUI menu = gp.uiManager.get(MainMenuUI.class);
            if (menu == null) return; // safety check

            // Let menu handle ESC for closing credits
            menu.handleKey(code);

            // If credits are showing, ignore other key inputs
            if (menu.isShowingCredits()) return;

            // Move selection up
            if (code == KeyEvent.VK_UP) {
                menu.commandNum--;
                if (menu.commandNum < 0) {
                    menu.commandNum = 3; // wrap to last option
                }
            }

            // Move selection down
            if (code == KeyEvent.VK_DOWN) {
                menu.commandNum++;
                if (menu.commandNum > 3) {
                    menu.commandNum = 0; // wrap to first option
                }
            }

            // Execute menu action
            if (code == KeyEvent.VK_ENTER) {
                menu.select();
            }
        }

        // ===========================================
        // =========== PLAY STATE ====================
        // ===========================================
        else if (gp.gsm.getState() == GameState.PLAY) {
            // === SAVE & LOAD GAME ===
            if (code == KeyEvent.VK_F5) {
                gp.saveManager.saveGame(gp);
                var msgUI = gp.uiManager.get(MessageUI.class);
                if (msgUI != null) msgUI.showTouchMessage("Game Saved!",null,gp);
                return;
            }

            if (code == KeyEvent.VK_F9) {
                gp.saveManager.loadGame(gp);
                var msgUI = gp.uiManager.get(MessageUI.class);
                if (msgUI != null) msgUI.showTouchMessage("Game Loaded!",null,gp);
                return;
            }


            // Press ESC to open the pause menu
            if (code == KeyEvent.VK_ESCAPE) {
                gp.setPaused(true);
                SoundManager.getInstance().stopMusic();
                return;
            }

            // Forward key events to pause overlay if the game is paused
            if (gp.isPaused()) {
                gp.handleKeyPressed(code);
                return;
            }

            // TODO: handle gameplay keys (movement, attack, etc.)
        }

        // ===========================================
        // ======== LEGACY PAUSE STATE ===============
        // ===========================================
        else if (gp.gsm.getState() == GameState.PAUSE) {
            // This is kept for compatibility; real pause logic is in GamePanel.
            gp.handleKeyPressed(code);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used in global command handler
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used in global command handler
    }
}
