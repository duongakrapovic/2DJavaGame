/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tile;
import java.awt.image.BufferedImage;

public class Tile {
    public BufferedImage image; 
    // Image used to render this tile
    
    public boolean collision = false;
    // Whether the player or objects can collide with this tile
}
