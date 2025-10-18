package ui;

import main.GamePanel;
import main.GameState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainMenuUI extends BaseUI {

    private GamePanel gp;

    // Menu assets
    private BufferedImage backgroundImg;   // pink sky background
    private BufferedImage menuBoardImg;    // wooden board frame
    private BufferedImage buttonAtlas;     // button sprite atlas
    private BufferedImage[][] buttonImgs;  // cut buttons from atlas

    private final int B_WIDTH_DEFAULT = 140;
    private final int B_HEIGHT_DEFAULT = 56;

    public int commandNum = 0;
    private boolean showAbout = false;

    public MainMenuUI(GamePanel gp) {
        super(gp);
        this.gp = gp;
        loadImages();
        loadButtons();
    }

    /** Load background, menu board, and button atlas images */
    private void loadImages() {
        try {
            backgroundImg = ImageIO.read(getClass().getResourceAsStream("/ui/background_menu.png"));
            menuBoardImg = ImageIO.read(getClass().getResourceAsStream("/ui/menu_background.png"));
            buttonAtlas = ImageIO.read(getClass().getResourceAsStream("/ui/button_atlas.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Slice the button atlas into separate button sprites (4 rows × 3 columns) */
    private void loadButtons() {
        buttonImgs = new BufferedImage[4][3];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 3; col++) {
                buttonImgs[row][col] = buttonAtlas.getSubimage(
                        col * B_WIDTH_DEFAULT,
                        row * B_HEIGHT_DEFAULT,
                        B_WIDTH_DEFAULT,
                        B_HEIGHT_DEFAULT
                );
            }
        }
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D g2) {
        // Keep sharp pixel-art rendering
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // ===== 1. Draw background =====
        g2.drawImage(backgroundImg, 0, 0, gp.screenWidth, gp.screenHeight, null);

        // ===== 2. Draw centered menu board =====
        int boardX = gp.screenWidth / 2 - menuBoardImg.getWidth() / 2;
        int boardY = gp.screenHeight / 2 - menuBoardImg.getHeight() / 2;
        g2.drawImage(menuBoardImg, boardX, boardY, null);

        // ===== 3. Draw menu buttons =====
        int buttonX = boardX + 70;
        int buttonY = boardY + 80;
        int spacing = 70;

        for (int i = 0; i < 4; i++) {
            int state = 0; // default: normal
            if (i == commandNum) state = 1; // selected (hovered)

            g2.drawImage(buttonImgs[i][state], buttonX, buttonY + i * spacing, null);
        }
    }


    /** Handle menu selection (to switch game state or exit) */
    public void select() {
        switch (commandNum) {
            case 0 -> gp.gsm.setState(GameState.PLAY);
            case 1 -> gp.gsm.setState(GameState.OPTIONS);
            case 2 -> toggleAbout();
            case 3 -> System.exit(0);
        }
    }

    /** Toggle visibility of the About section */
    public void toggleAbout() { showAbout = !showAbout; }

    /** Check if the About screen is active */
    public boolean isShowingAbout() { return showAbout; }
}
