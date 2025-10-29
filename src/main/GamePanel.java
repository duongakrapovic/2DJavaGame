/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import ui.*;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import entity_manager.EntityManager;
import entity_manager.ObjectManager;
import tile.ChunkManager;
import tile.TileManager;
import input_manager.InputManager;

public class GamePanel extends JPanel {
    // ===== SCREEN SETTING =====
    public final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 25; // width
    public final int maxScreenRow = 14; // height
    public final int screenWidth = tileSize * maxScreenCol;  // 786 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // ===== WORLD SETTING =====
    public final int maxWorldCol = 32 * 3;
    public final int maxWorldRow = 32 * 3;
    public final int chunkSize = 32;

    // ===== SYSTEM =====
    public TileManager tileM = new TileManager(this);
    public ChunkManager chunkM = new ChunkManager(chunkSize, this);
    private final InputManager input;

    // ===== OTHERS =====
    public CollisionChecker cChecker;
    public UtilityTool uTool = new UtilityTool();
    public int frameCounter = 0;

    // ===== ENTITY MANAGER =====
    public EntityManager em;
    public final ObjectManager om = new ObjectManager(this);

    // ===== UI SYSTEM =====
    public UIManager uiManager;
    public MessageUI messageUI;
    public FadeUI fadeUI;
    public MainMenuUI mainMenuUI;
    public PauseMenuUI pauseMenuUI;
    public HealthUI healthUI;

    // ===== MAP =====
    public int numMaps = 3;
    public int currentMap = 0;

    // ===== GAME STATE =====
    public final GameStateManager gsm = new GameStateManager();

    // ===== THREAD =====
    Thread gameThread;

    public GamePanel() {
        // Window setup
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        // Input
        this.input = new InputManager(this, this);

        // Core managers
        em = new EntityManager(this, input.getKeyController());
        cChecker = new CollisionChecker(this);

        // ===== UI INITIALIZATION =====
        uiManager = new UIManager();

        messageUI = new MessageUI(this);
        mainMenuUI = new MainMenuUI(this);
        pauseMenuUI = new PauseMenuUI(this);
        fadeUI = new FadeUI(this);
        healthUI = new HealthUI(this);

        uiManager.add(messageUI);
        uiManager.add(mainMenuUI);
        uiManager.add(pauseMenuUI);
        uiManager.add(fadeUI);
        uiManager.add(healthUI);
    }

    // Toggle the "About" screen in Main Menu
    public void toggleMainMenuAbout() {
        if (mainMenuUI != null) {
            mainMenuUI.toggleAbout();
        }
    }

    public void setupGame() {
        em.getPlayer().setDefaultValues();
        chunkM.pathMap = "map0";
        gsm.setState(GameState.START);
    }

    public void startGameThread() {
        gameThread = new Thread(new GameLoop(this));
        gameThread.start();
    }

    public void update() {
        if (gsm.getState() == GameState.PLAY) {
            chunkM.updateChunks(em.getPlayer().worldX, em.getPlayer().worldY);
            currentMap = uTool.mapNameToIndex(chunkM.pathMap);
            em.update(currentMap);
        } else if (gsm.getState() == GameState.PAUSE) {
            // nothing happen
        }

        uiManager.update();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        switch (gsm.getState()) {
            case START:
                // Only draw main menu
                mainMenuUI.draw(g2);
                break;

            case PLAY:
                // Draw world and entities
                tileM.draw(g2, chunkM);
                em.draw(g2, currentMap);

                // Gameplay UI
                healthUI.draw(g2);
                messageUI.draw(g2);
                fadeUI.draw(g2);
                break;

            case PAUSE:
                // Draw background world
                tileM.draw(g2, chunkM);
                em.draw(g2, currentMap);

                // Dark overlay
                g2.setColor(new Color(0, 0, 0, 150));
                g2.fillRect(0, 0, screenWidth, screenHeight);

                // Pause menu
                pauseMenuUI.draw(g2);
                break;
        }
        // Frame counter
        frameCounter++;
        if (frameCounter >= 1_000_000) {
            frameCounter = 0;
        }
        g2.dispose();
    }
}
