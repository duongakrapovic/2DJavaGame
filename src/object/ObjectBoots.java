/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import javax.imageio.ImageIO;
import main.GamePanel;

public class ObjectBoots extends SuperObject{
    GamePanel gp;
    public ObjectBoots( GamePanel gp){
        name = "boots";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/object/boots.png"));
            this.width = gp.tileSize - 10;
            this.height = gp.tileSize - 10;
            uTool.scaleImage(image , width, height );
            
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
}
