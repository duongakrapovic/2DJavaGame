/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import ui.*;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;

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
    public final UIManager uiManager = new UIManager();
    private PauseOverlay pauseOverlay;
    public static final float SCALE = 3f;

    // ===== PAUSE SYSTEM =====
    private boolean paused = false;

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

        // ===== UI INITIALIZATION =====
        pauseOverlay = new PauseOverlay(this);
        uiManager.add(pauseOverlay);
        uiManager.add(new MainMenuUI(this));
        uiManager.add(new MessageUI(this));
        uiManager.add(new FadeUI(this));
        uiManager.add(new HealthUI(this));
        uiManager.add(new MonsterHealthUI(this));
        uiManager.add(new GameOverUI(this));
        // Input
        this.input = new InputManager(this, this);

        // Core managers
        em = new EntityManager(this, input.getKeyController());
        cChecker = new CollisionChecker(this);


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

    // ===== UPDATE LOOP =====
    public void update() {
        // Nếu tạm dừng, chỉ cập nhật UI (ví dụ PauseOverlay)
        if (paused) {
            uiManager.update(gsm.getState());
            return;
        }

        switch (gsm.getState()) {
            case START -> {
                uiManager.update(gsm.getState());
            }
            case PLAY -> {
                chunkM.updateChunks(em.getPlayer().worldX, em.getPlayer().worldY);
                currentMap = uTool.mapNameToIndex(chunkM.pathMap);
                em.update(currentMap);

                // Kiểm tra nếu Player chết => sang Game Over
                if (em.getPlayer() != null && em.getPlayer().isDead()) {
                    setPaused(false);
                    gsm.setState(GameState.GAME_OVER);
                }

                uiManager.update(gsm.getState());
            }
            case GAME_OVER -> {
                uiManager.update(gsm.getState());
            }
        }
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
            }
        }
    }

    // ===== DRAW LOOP =====
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Vẽ thế giới + entity
        if (gsm.getState() != GameState.START)
            tileM.draw(g2, chunkM);
        em.draw(g2, currentMap);

        // Vẽ toàn bộ UI qua UIManager
        uiManager.draw(g2, gsm.getState());


        frameCounter++;
        if (frameCounter >= 1_000_000) frameCounter = 0;
        g2.dispose();
    }


    // ===== ACCESSORS =====
    public boolean isPaused() { return paused; }
    public void setPaused(boolean value) { this.paused = value; }
}