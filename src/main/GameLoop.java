/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Asus
 */
public class GameLoop implements Runnable{
    private GamePanel gp;
    private final int FPS = 60; // mặc định, không cần truyền từ ngoài

    public GameLoop(GamePanel gp){
        this.gp = gp;
    }
    
    @Override
    public void run(){
        //System.out.println("game loop start");
        double drawInterval = 1000000000/FPS; //  1s = 10^9 nano s
        // darw the screen every 0,016s or else upadte + repaint overload
        double nextDrawTime = System.nanoTime() + drawInterval;
        
//        long timer = System.currentTimeMillis();
//        int drawcnt = 0;
        while(true){
            // as long as this gameThread exists, it repeats the process
            // that is wirten inside of these brackets          
            
            // System.out.println("loop running");
            // 1 : UPDATE INFO - CHARACTER POSITION
            //System.out.println("gp.update start");
            gp.update();
            // 2 : DARW THE SCREEN WITH UPDATED INFO
            gp.repaint();
            //System.out.println("end repaint");
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
}
