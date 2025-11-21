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
        double drawInterval = 1000000000/FPS; //  1s = 10^9 nano s
        // darw the screen every 0,016s or else upadte + repaint overload
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(true){
            gp.update();
            // 2 : DARW THE SCREEN WITH UPDATED INFO
            gp.repaint();
            
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
        }
    }
}
