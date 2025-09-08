/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import javax.imageio.ImageIO;
import main.GamePanel;

public class ObjectChest extends SuperObject{
    GamePanel gp;
    public ObjectChest( GamePanel gp){
        name = "chest";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/object/chest.png"));
            this.width = gp.tileSize;
            this.height = gp.tileSize;
            uTool.scaleImage(image , width, height );
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
