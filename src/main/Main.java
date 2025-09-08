/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import javax.swing.JFrame;
 
public class Main {

    public static void main(String[] args) {
        JFrame window = new JFrame(); //  make new window 
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// click "X" then program end
        window.setResizable(false);// window size is constant
        window.setTitle("2D indie");// name for the window tab
        
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        // gamepanel show the content , jfram is a container
        window.pack(); // cause this window to be sized to fit the preferred size
        // and layout of its subcomponent(=GamePanel)
        
        window.setLocationRelativeTo(null);// place window in the middle of the screen
        window.setVisible(true);// view the window on screen
        
        gamePanel.setupGame();
        gamePanel.startGameThread();
    } 
}
