/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.JPanel;
import java.awt.*;

import game_data.SaveManager;
import entity_manager.EntityManager;
import entity_manager.ObjectManager;
import tile.ChunkManager;
import tile.TileManager;
import input_manager.InputManager;
import interact_manager.Interact;
import ui.health.HealthUI;
import ui.health.MonsterHealthUI;
import ui.base.UIManager;
import ui.effects.FadeUI;
import ui.effects.MessageUI;
import ui.health.PlayerStatusUI;
import ui.screens.gameover.GameOverUI;
import ui.screens.mainmenu.MainMenuUI;
import ui.screens.pause.PauseOverlay;


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

    // ===== SAVE  =====
    public final SaveManager saveManager = new SaveManager();

    // ===== OTHERS =====
    public CollisionChecker cChecker;
    public UtilityTool uTool = new UtilityTool();
    public int frameCounter = 0;
    public Interact iR;
    // ===== ENTITY MANAGER =====
    public EntityManager em;
    public final ObjectManager om = new ObjectManager(this);

    // ===== UI SYSTEM =====
    public final UIManager uiManager = new UIManager();
    private PauseOverlay pauseOverlay;
    public static final float SCALE = 3f;

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
        uiManager.add(new PlayerStatusUI(this));
        uiManager.add(new MonsterHealthUI(this));
        uiManager.add(new GameOverUI(this));
        uiManager.add(new ui.effects.DialogueUI(this));
        // Input
        this.input = new InputManager(this, this);

        // Core managers
        em = new EntityManager(this, input.getKeyController());
        cChecker = new CollisionChecker(this);
        iR = new Interact(this, em.getPlayer(), input.getKeyController());

    }


    public void setupGame() {
        em.getPlayer().setDefaultValues();
        chunkM.pathMap = "map3";
        gsm.setState(GameState.START);
    }

    public void restartGame() {
        currentMap = 3;
        chunkM.pathMap = "map3";
        chunkM.loadMap("map3");
        em.getPlayer().setDefaultValues();
        em.getPlayer().setHP(em.getPlayer().getMaxHP());
        em.getPlayer().mapIndex = currentMap;
        if (om != null) {
            om.reloadMapObjects(currentMap);
        }
        cChecker = new CollisionChecker(this);
        if (em != null && em.getPlayer() != null) {
            iR = new interact_manager.Interact(this, em.getPlayer(), em.getPlayer().input);
        }
        em.update(currentMap);
        gsm.setState(GameState.PLAY);
    }

    public void startGameThread() {
        gameThread = new Thread(new GameLoop(this));
        gameThread.start();
    }

    // ===== UPDATE LOOP =====
    public void update() {
        switch (gsm.getState()) {
            case START, GAME_OVER -> uiManager.update(gsm.getState());

            case PLAY -> {
                chunkM.updateChunks(em.getPlayer().worldX, em.getPlayer().worldY);
                currentMap = uTool.mapNameToIndex(chunkM.pathMap);
                em.update(currentMap);
                if (em.getPlayer() != null && em.getPlayer().isDead()) {
                    gsm.setState(GameState.GAME_OVER);
                    return;
                }

                uiManager.update(GameState.PLAY);
            }
            case DIALOGUE -> {
                uiManager.update(gsm.getState());
            }
            case PAUSE -> {
                uiManager.update(GameState.PAUSE);
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

        om.draw(g2, currentMap , em.getPlayer());
        em.draw(g2, currentMap);
        uiManager.draw(g2, gsm.getState());

        frameCounter++;
        if (frameCounter >= 1_000_000) frameCounter = 0;
        g2.dispose();
    }
}