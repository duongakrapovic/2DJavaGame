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
import java.awt.event.KeyEvent;

import entity_manager.EntityManager;
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

    // ===== UI SYSTEM =====
    public UIManager uiManager;
    public MessageUI messageUI;
    public FadeUI fadeUI;
    public MainMenuUI mainMenuUI;
    public HealthUI healthUI;
    public MonsterHealthUI monsterHealthUI;

    // ===== PAUSE SYSTEM =====
    private boolean paused = false;
    private PauseOverlay pauseOverlay; // Dùng bản mình viết lại ở ui/

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
        fadeUI = new FadeUI(this);
        healthUI = new HealthUI(this);
        monsterHealthUI = new MonsterHealthUI(this);

        uiManager.add(messageUI);
        uiManager.add(mainMenuUI);
        uiManager.add(fadeUI);
        uiManager.add(healthUI);

        // ===== PAUSE OVERLAY =====
        pauseOverlay = new PauseOverlay(null); // Không có Playing, nhưng vẫn hoạt động
    }

    // Toggle Credits in Main Menu
    public void toggleMainMenuCredits() {
        if (mainMenuUI != null) {
            mainMenuUI.toggleAbout();
        }
    }

    public void setupGame() {
        em.getPlayer().setDefaultValues();
        chunkM.pathMap = "map1";
        gsm.setState(GameState.START);
    }

    public void startGameThread() {
        gameThread = new Thread(new GameLoop(this));
        gameThread.start();
    }

    // ===== UPDATE LOOP =====
    public void update() {
        if (paused) {
            pauseOverlay.update();
            return;
        }

        if (gsm.getState() == GameState.PLAY) {
            chunkM.updateChunks(em.getPlayer().worldX, em.getPlayer().worldY);
            currentMap = uTool.mapNameToIndex(chunkM.pathMap);
            em.update(currentMap);
        }

        uiManager.update();
    }

    // ===== KEYBOARD CONTROL =====
    public void handleKeyPressed(int code) {
        if (!paused) {
            if (code == KeyEvent.VK_ESCAPE) {
                paused = true;
                return;
            }
        } else {
            switch (code) {
                case KeyEvent.VK_ESCAPE -> paused = false;
                case KeyEvent.VK_LEFT -> pauseOverlay.moveLeft();
                case KeyEvent.VK_RIGHT -> pauseOverlay.moveRight();
                case KeyEvent.VK_ENTER -> pauseOverlay.select();
                case KeyEvent.VK_A -> pauseOverlay.decreaseVolume();
                case KeyEvent.VK_D -> pauseOverlay.increaseVolume();
            }
        }
    }

    // ===== DRAW LOOP =====
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        switch (gsm.getState()) {
            case START:
                mainMenuUI.draw(g2);
                break;

            case PLAY:
                tileM.draw(g2, chunkM);
                em.draw(g2, currentMap);
                monsterHealthUI.draw(g2);
                healthUI.draw(g2);
                messageUI.draw(g2);
                fadeUI.draw(g2);

                if (paused) {
                    g2.setColor(new Color(0, 0, 0, 150));
                    g2.fillRect(0, 0, screenWidth, screenHeight);
                    pauseOverlay.draw(g2);
                }
                break;
        }

        frameCounter++;
        if (frameCounter >= 1_000_000) frameCounter = 0;
        g2.dispose();
    }

    // ===== ACCESSORS =====
    public boolean isPaused() { return paused; }
    public void setPaused(boolean value) { this.paused = value; }
    public PauseOverlay getPauseOverlay() { return pauseOverlay; }
}
