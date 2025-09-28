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
import entity.Player;
import tile.ChunkManager;
import tile.TileManager;


public class GamePanel extends JPanel implements Runnable{
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
    
    //FPS
    int FPS = 60;
    
    //SYSTEM
    public TileManager tileM = new TileManager(this);
    public ChunkManager chunkM = new ChunkManager(chunkSize, this);
    public KeyHandler keyH = new KeyHandler(this); 
    
    // OTHERS
    public CollisionChecker cChecker = new CollisionChecker(this);
    public UtilityTool uTool = new UtilityTool();
        
    //ENTITY MANAGER
    public EntityManager em;

    // ENTITY PLAYER
    public Player player = new Player(this, keyH);
   
    //UI
    public UI ui; // will be create after manager 

    //MAP 
    public int numMaps = 3;       // numbers of map 
    public int currentMap = 0;    // current map index
    
    // GAME STATE
    public int gameState;
    public int gameStart = 0;
    public int gamePlay = 1;
    public int gamePause = 2;
    
    // THREAD
    Thread gameThread; 
    
    public GamePanel(){     
        // set the preferred size of the game window
        this.setPreferredSize(new Dimension(screenWidth , screenHeight));
        // set background color for the panel (black screen by default)
        this.setBackground(Color.black);
        // snable double buffering to reduce flickering and improve rendering performance
        this.setDoubleBuffered(true);
        // add the key handler so the panel can receive keyboard inputs
        this.addKeyListener(keyH);
        // make sure the panel can gain focus to properly capture key events
        this.setFocusable(true);
        // initialize all entity managers before UI 
        // so that UI can safely access them if needed
        em = new EntityManager(player, this);
        // initialize the UI  after managers are ready
        ui = new UI(this);
    }
    
    public void setupGame(){
        player.setDefaultValues();
        chunkM.pathMap = "map1";
        gameState = gameStart;
    }
    
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run(){
        
        double drawInterval = 1000000000/FPS; //  1s = 10^9 nano s
        // darw the screen every 0,016s or else upadte + repaint overload
        double nextDrawTime = System.nanoTime() + drawInterval;
        
//        long timer = System.currentTimeMillis();
//        int drawcnt = 0;
        while(gameThread != null){
            // as long as this gameThread exists, it repeats the process
            // that is wirten inside of these brackets          
            
            // System.out.println("loop running");
            // 1 : UPDATE INFO - CHARACTER POSITION
            update();
            // 2 : DARW THE SCREEN WITH UPDATED INFO
            repaint();
            // proof for multithreading
            //System.out.println("GameLoop tick @" + System.nanoTime());
            
//            drawcnt++;// catch 1 frame drew
            
            try{
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000; // from nano to mili s
                
                if(remainingTime < 0 ){
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime );
                
                nextDrawTime += drawInterval;
            }
            catch(InterruptedException e){
               e.printStackTrace();
            }
            
            // check each 1s
//            if(System.currentTimeMillis() - timer >= 1000){// 1s == 1000 mili second
//                System.out.println("FPS : " + drawcnt);
//                drawcnt = 0;
//                timer += 1000;
                // no assign timer as System.currentTimeMillis() in the "if" 
                // can cause wrong time 
            //}
        }
    }
    public void update(){
        if(gameState == gamePlay){
            chunkM.updateChunks(player.worldX, player.worldY);
            currentMap = uTool.mapNameToIndex(chunkM.pathMap);
            em.update(currentMap);
        }
        if(gameState == gamePause){
            //nothing happen 
        }
    }
    public void paintComponent(Graphics g){
        // this is a standare method to darw on jPanel
        // a built-in java
        super.paintComponent(g); 
        Graphics2D g2 = (Graphics2D)g; //  extends the Graphics class for more tool
        
        //DEBUG
//        long drawStart = 0;
//        drawStart = System.nanoTime();
        
        if(gameState == gameStart){
            ui.drawUI(g2);
        }
        
        else if(gameState == gamePlay){
            // DRAW VISIBLE TILES 
            tileM.draw(g2, chunkM);       
            // OBJECT + MONSTER + NPC + PLAYER
            em.draw(g2, currentMap);
            // UI 
            ui.drawUI(g2);
        }
        
        else if(gameState == gamePause){
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
        
        g2.dispose();
    }
}
