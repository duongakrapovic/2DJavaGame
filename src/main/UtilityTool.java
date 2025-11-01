/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UtilityTool {
    public BufferedImage scaleImage(BufferedImage original , int width, int height){
        BufferedImage scaledImage = new BufferedImage(width , height , original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        
        return scaledImage;
    }
    public int mapNameToIndex(String mapName){
        switch(mapName){
        case "map0": return 0;
        case "map1": return 1;
        case "map2": return 2;
        case "map3": return 3;// shop 
        default: return 0;
        }
    }
}
