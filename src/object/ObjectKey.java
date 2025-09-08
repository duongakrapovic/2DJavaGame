/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import javax.imageio.ImageIO;
import main.GamePanel;

public class ObjectKey extends SuperObject {
    GamePanel gp;
    public ObjectKey(GamePanel gp){
        name = "key";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/object/key.png"));
            this.width = gp.tileSize * 4/5 - 5;
            this.height = gp.tileSize * 4/5 - 5;
            uTool.scaleImage(image , width, height );
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
