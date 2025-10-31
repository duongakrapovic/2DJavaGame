package ui;

import main.GamePanel;
import main.GameState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainMenuUI extends BaseUI {

    // Menu assets
    private BufferedImage backgroundImg;   // pink sky background
    private BufferedImage menuBoardImg;    // wooden board frame
    private BufferedImage buttonAtlas;     // button sprite atlas
    private BufferedImage[][] buttonImgs;  // cut buttons from atlas

    private final int B_WIDTH_DEFAULT = 140;
    private final int B_HEIGHT_DEFAULT = 56;

    public int commandNum = 0;
    private boolean showingCredits = false;

    public MainMenuUI(GamePanel gp) {
        super(gp);
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

        // If showing credits — draw overlay and stop here
        if (showingCredits) {
            drawCreditsScreen(g2);
            return;
        }

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

    /** Draw credit overlay when showingCredits = true */
    private void drawCreditsScreen(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        int centerY = gp.screenHeight / 2;

        g2.drawString("Credits", 80, centerY - 60);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString("Vu Hai Duong", 80, centerY - 20);
        g2.drawString("Luu Duc Thanh Dat", 80, centerY + 10);
        g2.drawString("Do Minh Vu", 80, centerY + 40);

        g2.setFont(new Font("Arial", Font.ITALIC, 18));
        g2.drawString("Press ESC to return", 80, centerY + 90);
    }

    /** Handle menu selection (to switch game state or exit) */
    public void select() {
        if (showingCredits) {
            showingCredits = false; // close credits when ENTER is pressed again
            return;
        }

        switch (commandNum) {
            case 0 -> gp.gsm.setState(GameState.PLAY);      // PLAY
            case 1 -> gp.gsm.setState(GameState.OPTIONS);   // OPTIONS
            case 2 -> System.exit(0);                       // QUIT
            case 3 -> showingCredits = true;                // CREDITS
        }
    }

    /** Public method to close credits with ESC */
    public void handleKey(int code) {
        if (showingCredits && code == KeyEvent.VK_ESCAPE) {
            showingCredits = false;
        }
    }

    /** Optional getter for state */
    public boolean isShowingCredits() {
        return showingCredits;
    }
    @Override
    public boolean shouldRenderIn(GameState state) {
        return state == GameState.START;
    }
}

