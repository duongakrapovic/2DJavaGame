/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;


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
    public int frameCounter = 0;

    //ENTITY MANAGER
    public EntityManager em;
   
    //UI
    public final UI ui; // will be create after manager 

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
        // initialize the UI  after managers are ready
        ui = new UI(this); 
        
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
    }
    public void paintComponent(Graphics g){
        // this is a standare method to darw on jPanel
        // a built-in java
        super.paintComponent(g); 
        Graphics2D g2 = (Graphics2D)g; //  extends the Graphics class for more tool
        
        //DEBUG
//        long drawStart = 0;
//        drawStart = System.nanoTime();
        
        if(gsm.getState() == GameState.START){
            ui.drawUI(g2);
        }
        
        else if(gsm.getState() == GameState.START || gsm.getState() == GameState.PLAY){
            // DRAW VISIBLE TILES 
            tileM.draw(g2, chunkM);       
            // OBJECT + MONSTER + NPC + PLAYER
            em.draw(g2, currentMap);
            // UI 
            ui.drawUI(g2);
        }
//        long drawEnd = System.nanoTime();
//        long passed = drawEnd - drawStart;
//        System.out.println(passed);
        //Frame Counter
        frameCounter++;
        if (frameCounter >= 1000000) { // tránh tràn số
            frameCounter = 0;
        }
        g2.dispose();
    }
}
