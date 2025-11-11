package input_manager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;
import sound_manager.SoundManager;
import main.GameState;
import ui.MainMenuUI;
import ui.MessageUI;
import ui.dialogue.DialogueUI;


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

        // ======= MAIN MENU (START STATE) ===========
        if (gp.gsm.getState() == GameState.START) {
            MainMenuUI menu = gp.uiManager.get(MainMenuUI.class);
            if (menu == null) return;
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

        // =========== PLAY STATE ====================
        else if (gp.gsm.getState() == GameState.PLAY) {
            // === DIALOGUE: nếu đang hội thoại, Enter sẽ lật trang ===
            DialogueUI dialogue = gp.uiManager.get(DialogueUI.class);
            if (dialogue != null && dialogue.isActive()) {
                return; // chặn mọi hành động khác khi đang hội thoại
            }
            // === SAVE & LOAD GAME ===
            if (code == KeyEvent.VK_F5) {
                gp.saveManager.saveGame(gp);
                var msgUI = gp.uiManager.get(MessageUI.class);
                if (msgUI != null) msgUI.showTouchMessage("Game Saved!", null, gp);
                return;
            }

            if (code == KeyEvent.VK_F9) {
                gp.saveManager.loadGame(gp);
                var msgUI = gp.uiManager.get(MessageUI.class);
                if (msgUI != null) msgUI.showTouchMessage("Game Loaded!", null, gp);
                return;
            }


            // Press ESC to open the pause menu
            if (code == KeyEvent.VK_ESCAPE) {
                gp.gsm.setState(GameState.PAUSE);
                SoundManager.getInstance().stopMusic();
                return;
            }

            // TODO: handle gameplay keys (movement, attack, etc.)
        }

        // ============ PAUSE STATE ==================
        else if (gp.gsm.getState() == GameState.PAUSE) {
            var pause = gp.uiManager.get(ui.PauseOverlay.class);
            if (pause == null) return;

            switch (code) {
                case KeyEvent.VK_LEFT -> pause.moveLeft();
                case KeyEvent.VK_RIGHT -> pause.moveRight();
                case KeyEvent.VK_ENTER -> pause.select();
            }
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
