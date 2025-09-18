/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import entity.Entity;
import entity.Player;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
    public Sound music = new Sound();
    public Sound se = new Sound();  
    // need seperate class for music and sound effect because music and se 
    // would not play both if they are in 1 class, only process 1 at a time 
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    
    public UI ui = new UI(this);// add ui 
    
    Thread gameThread; // a thread stared -> game keep running till u stop it 
        
    //ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public Entity obj[] = new Entity[10];
    
    // GAME STATE
    public int gameState;
    public int gameStart = 0;
    public int gamePlay = 1;
    public int gamePause = 2;
    
    public GamePanel(){
        
        // set the size of the window
        this.setPreferredSize(new Dimension(screenWidth , screenHeight));
        
        this.setBackground(Color.black);
        // improve redering performent
        this.setDoubleBuffered(true);
        
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    
    public void setupGame(){
        aSetter.setObject();
        player.setDefaultValues();
        
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
        long timer = System.currentTimeMillis();
        int drawcnt = 0;
        while(gameThread != null){
            // as long as this gameThread exists, it repeats the process
            // that is wirten inside of these brackets          
            
            // System.out.println("loop running");
            // 1 : UPDATE INFO - CHARACTER POSITION
            update();
            // 2 : DARW THE SCREEN WITH UPDATED INFO
            repaint();
            
            drawcnt++;// catch 1 frame drew
            
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
            player.update();
            chunkM.updateChunks(player.worldX, player.worldY);
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
        long drawStart = 0;
        drawStart = System.nanoTime();
        
        if(gameState == gameStart){
            ui.drawUI(g2);
        }
        
        else if(gameState == gamePlay){
            tileM.draw(g2, chunkM);// DRAW VISIBLE TILES
        
            // OBJECT
            for(int i = 0 ; i < obj.length; i++){
                if(obj[i] != null){
                    obj[i].drawObject(g2, this);
                }
            }
            // PLAYER
            player.drawPlayer(g2);
        
            // UI 
            ui.drawUI(g2);
        }
        
        else if(gameState == gamePause){
            tileM.draw(g2, chunkM);// DRAW VISIBLE TILES
        
            // OBJECT
            for(int i = 0 ; i < obj.length; i++){
                if(obj[i] != null){
                    obj[i].drawObject(g2, this);
                }
            }
            // PLAYER
            player.drawPlayer(g2);
        
            // UI 
            ui.drawUI(g2);
        }
        
        long drawEnd = System.nanoTime();
        long passed = drawEnd - drawStart;
//        System.out.println(passed);
        
        g2.dispose();
    }
    public void playMusic (int i){
        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic(){
        music.stop();
    }
    public void playSE(int i){// sound effect
        se.setFile(i);
        se.play();
    }
}
