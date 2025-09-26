package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ObjectHeart extends Entity {
    public BufferedImage image1;
    public BufferedImage image2;
    public BufferedImage image3;
    public ObjectHeart(GamePanel gp){
        super(gp);
        name = "boots";
        width = gp.tileSize * 3 / 5;
        height = gp.tileSize * 3 / 5;
        image1 = setup("/object/heart_full" , width, height);
        image3 = setup("/object/heart_half" , width, height);
        image3 = setup("/object/heart_blank" , width, height);
        collision = false;

        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
