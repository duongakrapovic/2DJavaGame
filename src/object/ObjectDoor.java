/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import javax.imageio.ImageIO;
import main.GamePanel;

public class ObjectDoor extends SuperObject{
     GamePanel gp;
    public ObjectDoor( GamePanel gp){
        name = "door";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/object/door.png"));
            this.width = gp.tileSize;
            this.height = gp.tileSize;
            uTool.scaleImage(image , width, height );
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        collision = true;
    } 
}
