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
import tile.ChunkManager;
import tile.TileManager;
import input_manager.InputManager;

public class GamePanel extends JPanel{
    //SCREEN SETTING
    public final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48 tile
    // 16x16 is so small on modern moniter
    //therefore we time it up so now
    // the 16x16 look "scale" time on moniter
    // need to be public otherwise the Player class cannot access
    // need to be public to br access from other packages
    public final int maxScreenCol = 25; // width
    public final int maxScreenRow = 14; // height
    public final int screenWidth = tileSize * maxScreenCol;// 786 pixels
    public final int screenHeight = tileSize * maxScreenRow;// 576 pixels

    // WORLD SETTING
    public final int maxWorldCol = 32 * 3;
    public final int maxWorldRow = 32 * 3;
    public final int chunkSize = 32;

    //SYSTEM
    public TileManager tileM = new TileManager(this);
    public ChunkManager chunkM = new ChunkManager(chunkSize, this);
    private final InputManager input;

    // OTHERS
    public CollisionChecker cChecker;// xu ly tach 1 class khac
    public UtilityTool uTool = new UtilityTool();// vut

    //ENTITY MANAGER
    public EntityManager em;

    //UI Manager
    public UIManager uiManager;
    // will be create after manager

    // Individual UI references
    public MessageUI messageUI;
    public FadeUI fadeUI;
    public MainMenuUI mainMenuUI;
    public PauseMenuUI pauseMenuUI;
    public HealthUI healthUI;
    //MAP
    public int numMaps = 3;       // numbers of map
    public int currentMap = 0;    // current map index

    // GAME STATE
    public final GameStateManager gsm = new GameStateManager();
    //use GameState not GameStateManager for code

    // THREAD
    Thread gameThread;

    public GamePanel(){
        // set the preferred size of the game window
        this.setPreferredSize(new Dimension(screenWidth , screenHeight));
        // set background color for the panel (black screen by default)
        this.setBackground(Color.black);
        // snable double buffering to reduce flickering and improve rendering performance
        this.setDoubleBuffered(true);
        // add the key so the panel can receive keyboard inputs
        this.input = new InputManager(this, this);
        // initialize all entity managers before UI
        // so that UI can safely access them if needed
        em = new EntityManager(this, input.getKeyController());
        cChecker = new CollisionChecker(this);

// create UI manager and UI elements
        uiManager = new UIManager();

        messageUI = new MessageUI(this);
        mainMenuUI = new MainMenuUI(this);
        pauseMenuUI = new PauseMenuUI(this);
        fadeUI = new FadeUI(this);
        healthUI = new HealthUI(this);

        uiManager.add(messageUI);    // message UI
        uiManager.add(mainMenuUI);   // main menu UI
        uiManager.add(pauseMenuUI);  // pause menu UI
        uiManager.add(fadeUI);       // fade UI
        uiManager.add(healthUI);     // health bar UI


    }

    public void setupGame(){
        em.getPlayer().setDefaultValues();
        chunkM.pathMap = "map1";
        gsm.setState(GameState.START);
        //System.out.println(em.getPlayer());
    }

    public void startGameThread(){
        gameThread = new Thread(new GameLoop(this));
        gameThread.start();
    }

    public void update(){
        //System.out.println("in update");
//        System.out.println(gameState == gameStart);
        if(gsm.getState() == GameState.PLAY){
            //System.out.println("start update chunk");
            chunkM.updateChunks(em.getPlayer().worldX, em.getPlayer().worldY);
            //System.out.println("end update chunk");
            currentMap = uTool.mapNameToIndex(chunkM.pathMap);
            em.update(currentMap);
        }
        if(gsm.getState() == GameState.PAUSE){
            //nothing happen
        }
        //System.out.println("out update");
        uiManager.update();

    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        switch (gsm.getState()) {
            case START:
                // Chỉ vẽ main menu
                mainMenuUI.draw(g2);
                break;

            case PLAY:
                // Vẽ map và entity
                tileM.draw(g2, chunkM);
                em.draw(g2, currentMap);

                // UI trong gameplay
                healthUI.draw(g2);
                messageUI.draw(g2);
                fadeUI.draw(g2);
                break;

            case PAUSE:
                // Vẽ world làm nền
                tileM.draw(g2, chunkM);
                em.draw(g2, currentMap);

                // Overlay tối nhẹ
                g2.setColor(new Color(0, 0, 0, 150));
                g2.fillRect(0, 0, screenWidth, screenHeight);

                // Vẽ pause menu
                pauseMenuUI.draw(g2);
                break;
        }

        g2.dispose();
    }

}