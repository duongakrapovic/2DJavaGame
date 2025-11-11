package ui.screens.pause;

import main.GamePanel;
import main.GameState;
import sound_manager.SoundManager;
import ui.base.BaseUI;
import ui.utilz.LoadSave;
import ui.utilz.Constants;
import java.awt.*;
import java.awt.image.BufferedImage;
import static ui.utilz.Constants.UI.PauseButtons.SOUND_SIZE;
import static ui.utilz.Constants.UI.URMButtons.*;


public class PauseOverlay extends BaseUI {
    private BufferedImage bgImg;
    private int bgX, bgY, bgW, bgH;

    private SoundButton musicB, sfxB;
    private UrmButton menuB, replayB, resumeB;
    private int focusIndex = 4; // 0=Music, 1=SFX, 2=Menu, 3=Replay, 4=Resume

    // === Constructor ===
    public PauseOverlay(GamePanel gp) {
        super(gp);
        loadBackground();
        initButtons();
    }

    private void loadBackground() {
        bgImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        float scale = gp.tileSize / 48f;
        bgW = Math.round(bgImg.getWidth() * scale);
        bgH = Math.round(bgImg.getHeight() * scale);
        bgX = gp.screenWidth / 2 - bgW / 2;
        bgY = gp.screenHeight / 2 - bgH / 2;
    }

    private void initButtons() {
        int centerX = bgX + bgW / 2;

        // === SOUND BUTTON ===
        final float REF_W = 258f;
        final float REF_H = 389f;
        final float MUSIC_RIGHT_X = 208f;
        final float MUSIC_CENTER_Y = 111f;

        int soundX = bgX + Math.round((MUSIC_RIGHT_X / REF_W) * bgW) - 200;
        int soundY = bgY + Math.round((MUSIC_CENTER_Y / REF_H) * bgH) - SOUND_SIZE / 2 + 220;
        musicB = new SoundButton(soundX, soundY);

        // === URM BUTTONS ===
        int groupW = URM_SIZE * 3 + Constants.UI.PauseButtons.URM_GAP * 2;
        int baseY = bgY + (int) (bgH * (Constants.UI.PauseButtons.GROUP_Y - 0.215f));
        int startX = centerX - groupW / 2 + (int) (bgW * -0.01f);

        menuB   = new UrmButton(startX, baseY, 2);
        replayB = new UrmButton(startX + URM_SIZE + Constants.UI.PauseButtons.URM_GAP, baseY, 1);
        resumeB = new UrmButton(startX + (URM_SIZE + Constants.UI.PauseButtons.URM_GAP) * 2, baseY, 0);
    }

    // === Update & Draw ===
    @Override
    public void update() {
        musicB.update();
    }

    @Override
    public void draw(Graphics2D g2) {
        // Background
        g2.drawImage(bgImg, bgX, bgY, bgW, bgH, null);
        // Buttons
        musicB.draw(g2);
        menuB.draw(g2);
        replayB.draw(g2);
        resumeB.draw(g2);
        highlightFocusedButton(g2);
    }

    // === Điều kiện hiển thị trong UIManager ===
    @Override
    public boolean shouldRenderIn(GameState state) {
        return gp.gsm.getState() == GameState.PAUSE;
    }

    @Override
    public boolean shouldDraw() {
        return gp.gsm.getState() == GameState.PAUSE;
    }

    @Override
    public boolean shouldUpdate() {
        return gp.gsm.getState() == GameState.PAUSE;
    }

    private void highlightFocusedButton(Graphics2D g2) {
        Rectangle r = switch (focusIndex) {
            case 0 -> musicB.getBounds();
            case 1 -> menuB.getBounds();
            case 2 -> replayB.getBounds();
            case 3 -> resumeB.getBounds();
            default -> null;
        };
        if (r != null) {
            g2.setColor(new Color(255, 255, 255, 80));
            g2.fillRoundRect(r.x - 4, r.y - 4, r.width + 8, r.height + 8, 10, 10);
        }
    }

    // === Keyboard control ===
    public void moveLeft() {
        focusIndex = (focusIndex - 1 + 5) % 5;
    }

    public void moveRight() {
        focusIndex = (focusIndex + 1) % 5;
    }

    public void select() {
        switch (focusIndex) {
            case 0 -> toggleMusic();
            case 1 -> goMenu();
            case 2 -> restart();
            case 3 -> resume();
        }
    }

    // === Actions ===
    private void toggleMusic() {
        musicB.setMuted(!musicB.isMuted());
        if (musicB.isMuted())
            SoundManager.getInstance().stopMusic();
        else
            SoundManager.getInstance().playMusic(SoundManager.SoundID.MUSIC_THEME);
    }

    private void goMenu() {
        gp.gsm.setState(GameState.START);
    }

    private void restart() {
        gp.restartGame();
        if (!musicB.isMuted()) {
            SoundManager.getInstance().playMusic(SoundManager.SoundID.MUSIC_THEME);
        }
    }

    private void resume() {
        gp.gsm.setState(GameState.PLAY);
        if (!musicB.isMuted()) {
            SoundManager.getInstance().playMusic(SoundManager.SoundID.MUSIC_THEME);
        }
    }
}
