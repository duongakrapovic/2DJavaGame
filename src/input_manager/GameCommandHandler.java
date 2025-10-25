package input_manager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;
import sound_manager.SoundManager;
import main.GameState;

/**
 * Lớp GameCommandHandler chịu trách nhiệm xử lý các phím chức năng toàn cục
 * (ESC, ENTER, mũi tên điều hướng, v.v...) cho các trạng thái chính của trò chơi:
 * - Menu chính (START)
 * - Trạng thái chơi (PLAY)
 * - Trạng thái tạm dừng (PAUSE)
 *
 * Lớp này không xử lý di chuyển nhân vật hay hành động trong gameplay;
 * phần đó được quản lý bởi KeyHandler.
 */
public class GameCommandHandler implements KeyListener {

    /** Tham chiếu đến GamePanel chính để truy cập state và UI. */
    private final GamePanel gp;

    /** Khởi tạo handler và gắn với GamePanel hiện tại. */
    public GameCommandHandler(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Hàm xử lý khi người chơi nhấn phím.
     * Tùy theo trạng thái game hiện tại mà thực hiện hành động tương ứng.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // =====================================
        // ======= MENU CHÍNH (START STATE) =====
        // =====================================
        if (gp.gsm.getState() == GameState.START) {

            // Di chuyển lên / xuống giữa các lựa chọn trong menu
            if (code == KeyEvent.VK_UP) {
                gp.mainMenuUI.commandNum--;
                if (gp.mainMenuUI.commandNum < 0) {
                    gp.mainMenuUI.commandNum = 3; // vòng về lựa chọn cuối cùng
                }
            }
            if (code == KeyEvent.VK_DOWN) {
                gp.mainMenuUI.commandNum++;
                if (gp.mainMenuUI.commandNum > 3) {
                    gp.mainMenuUI.commandNum = 0; // vòng về đầu
                }
            }

            // Xử lý khi người chơi nhấn ENTER tại menu chính
            if (code == KeyEvent.VK_ENTER) {
                switch (gp.mainMenuUI.commandNum) {
                    case 0 -> { // PLAY
                        gp.gsm.setState(GameState.PLAY);
                        SoundManager.getInstance().playMusic(SoundManager.SoundID.MUSIC_THEME);
                    }
                    case 1 -> { // OPTIONS
                        // Tạm thời để trống, có thể mở menu tùy chọn sau
                    }
                    case 2 -> { // QUIT
                        System.exit(0);
                    }
                    case 3 -> { // CREDITS
                        gp.toggleMainMenuCredits(); // chuyển sang phần giới thiệu nhóm
                    }
                }
            }
        }

        // =====================================
        // =========== TRẠNG THÁI CHƠI ==========
        // =====================================
        else if (gp.gsm.getState() == GameState.PLAY) {

            // Nhấn ESC để bật menu tạm dừng
            if (code == KeyEvent.VK_ESCAPE) {
                gp.setPaused(true); // bật cờ tạm dừng trong GamePanel
                SoundManager.getInstance().stopMusic();
                return;
            }

            // Nếu đang ở trạng thái tạm dừng, chuyển tiếp phím sang GamePanel
            if (gp.isPaused()) {
                gp.handleKeyPressed(code);
                return;
            }

            // TODO: xử lý các phím gameplay khác ở đây (nhảy, tấn công, di chuyển...)
        }

        // =====================================
        // ======= TRẠNG THÁI PAUSE (CŨ) =======
        // =====================================
        // Giữ lại để tương thích, nhưng hiện tại việc tạm dừng
        // được xử lý trực tiếp trong GamePanel qua biến paused.
        else if (gp.gsm.getState() == GameState.PAUSE) {
            gp.handleKeyPressed(code);
        }
    }

    /** Không sử dụng trong hệ thống phím chung. */
    @Override
    public void keyReleased(KeyEvent e) {}

    /** Không sử dụng trong hệ thống phím chung. */
    @Override
    public void keyTyped(KeyEvent e) {}
}
