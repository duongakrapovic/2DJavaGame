/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import javax.imageio.ImageIO;
import main.GamePanel;

public class ObjectPortal extends SuperObject {
    GamePanel gp;
    public ObjectPortal(GamePanel gp){
        name = "portal";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/object/portal.png"));
            this.width = gp.tileSize;
            this.height = gp.tileSize;
            uTool.scaleImage(image , width, height );
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
