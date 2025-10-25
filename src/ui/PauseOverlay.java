package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.GameState;
import sound_manager.SoundManager;

/** Overlay tạm dừng: vẽ nền + 3 nút URM, điều khiển bằng phím. */
public class PauseOverlay {

    private final GamePanel gp;

    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;

    private final UrmButton[] buttons;
    private int currentChoice = 2; // 0 = Menu, 1 = Replay, 2 = Resume

    private final AudioOptions audioOptions;

    public PauseOverlay(GamePanel gp) {
        this.gp = gp;
        loadBackground();

        // bố trí 3 nút theo nền (tự canh theo kích thước panel)
        int btnSize = Math.max(48, gp.tileSize); // hiển thị theo tileSize cho đẹp
        int spacing = btnSize + gp.tileSize / 3;
        int baseX = gp.screenWidth / 2 - (btnSize * 3 + spacing * 2) / 2;
        int y = bgY + bgH - (btnSize + gp.tileSize);

        buttons = new UrmButton[] {
                new UrmButton(baseX,               y, btnSize, btnSize, 2), // Menu
                new UrmButton(baseX + spacing,     y, btnSize, btnSize, 1), // Replay
                new UrmButton(baseX + spacing * 2, y, btnSize, btnSize, 0)  // Resume
        };

        audioOptions = new AudioOptions(gp);
    }

    private void loadBackground() {
        try (InputStream is = getClass().getResourceAsStream("/ui/pause_menu.png")) {
            backgroundImg = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // scale nền theo panel
        bgW = Math.min(gp.screenWidth - gp.tileSize * 4, backgroundImg.getWidth() * 3);
        bgH = Math.min(gp.screenHeight - gp.tileSize * 4, backgroundImg.getHeight() * 3);
        bgX = (gp.screenWidth - bgW) / 2;
        bgY = (gp.screenHeight - bgH) / 4;
    }

    public void update() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setMouseOver(i == currentChoice);
            buttons[i].update();
        }
        audioOptions.update();
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);
        for (UrmButton b : buttons) b.draw(g);

        g.setColor(Color.WHITE);
        int textY = bgY + bgH + gp.tileSize / 2;
        g.drawString("← / → : Chọn nút     ENTER : Chấp nhận     ESC : Tiếp tục", bgX, textY);
        g.drawString("A / D : Giảm / tăng âm lượng", bgX, textY + gp.tileSize);

        audioOptions.draw(g);
    }

    // ====== Điều khiển bằng phím ======
    public void moveLeft() {
        currentChoice = (currentChoice + buttons.length - 1) % buttons.length;
        SoundManager.playSFX("hover");
    }

    public void moveRight() {
        currentChoice = (currentChoice + 1) % buttons.length;
        SoundManager.playSFX("hover");
    }

    public void select() {
        SoundManager.playSFX("click");
        switch (currentChoice) {
            case 0 -> { // Menu
                gp.gsm.setState(GameState.START);
                gp.setPaused(false);
                SoundManager.getInstance().playMusic(SoundManager.SoundID.MUSIC_THEME);
            }
            case 1 -> { // Replay
                gp.setupGame();
                gp.setPaused(false);
                SoundManager.getInstance().playMusic(SoundManager.SoundID.MUSIC_THEME);
            }
            case 2 -> { // Resume
                gp.setPaused(false);
                SoundManager.getInstance().playMusic(SoundManager.SoundID.MUSIC_THEME);
            }
        }
    }

    public void increaseVolume() { audioOptions.increaseVolume(); }
    public void decreaseVolume() { audioOptions.decreaseVolume(); }
}
