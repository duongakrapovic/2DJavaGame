/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;
    Interact iR;
    
    public final int screenX;
    public final int screenY;
    
    public int hasKey = 0;// indicate how many key the player currently has 
    int speedTimer = 0; // count down time for speed buff
    int standCounter = 0;// return stand image
    
    public Player(GamePanel gp , KeyHandler keyH){
        super(gp);
        this.gp = gp;
        this.keyH = keyH;
        this.iR = new Interact(gp, keyH, this);
        
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);
        
        solidArea = new Rectangle();
        solidArea.x = 11;
        solidArea.y = 16;
        solidAreaDefaultX =  solidArea.x;
        solidAreaDefaultY =  solidArea.y;
        solidArea.width = 25;
        solidArea.height = 25;
        
        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues(){
        // central of the chunk 
        worldX = gp.tileSize * gp.chunkSize / 2; // chunkSize = số tile trong 1 chunk
        worldY = gp.tileSize * gp.chunkSize / 2;
        defaultSpeed = 15;
        buffSpeed = 4;
        actualSpeed = defaultSpeed;
        direction = "down";
    }
    
    private void getPlayerImage(){
        
        up1 = setup("/boy_up_1");
        up2 = setup("boy_up_2");
        down1 = setup("boy_down_1");
        down2 = setup("boy_down_2");
        left1 = setup("boy_left_1");
        left2 = setup("boy_left_2");
        right1 = setup("boy_right_1");
        right2 = setup("boy_right_2");  
    }
    private BufferedImage setup(String imageName){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/player/"+ imageName +".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
    
    @Override
    public void update(){
        // in java , the top left corner is x = 0 and y = 0;
        // x valuaes increase to the right
        // y valuaes increase as they go down
        int deltaMoveX = 0;
        int deltaMoveY = 0;
        if(keyH.upPressed == true || keyH.downPressed == true ||
                keyH.leftPressed == true || keyH.rightPressed == true){
            if(keyH.upPressed == true){//w
                direction = "up";
                deltaMoveY -= actualSpeed; 
            }
            if(keyH.downPressed == true){//s
                direction = "down";
                deltaMoveY += actualSpeed;
            }
            if(keyH.leftPressed == true){//a
                direction = "left"; 
                deltaMoveX -= actualSpeed;
            }
            if(keyH.rightPressed == true){//d
                direction = "right";
                deltaMoveX += actualSpeed;
            }
        /* spriteCounter++ every time u press a key;
        if spriteCounter++ every frame, the character will change
        motion nonstop.
        we count till 12 so that after 10 key press , the image get changed.
        the spriteNum is for controlling the image for that frame
        exp: when we move , we step right leg first and then left leg
        */
            
            // animation transform
            spriteCounter++;
            if(spriteCounter > 8){
                if(spriteNum == 1){
                    spriteNum = 2;
                }
                else if(spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
                }
           }
        else{
            standCounter++;
            if(standCounter > 15){
                standCounter = 0;
                spriteNum = 1;
            }
        }
        /* there is a problem that when player go diagonally , the speed will be
        speed * sqrt(2). therefore, we have to normalize movement vector,
        the true position of charactor should be lower than before, 
        */
        double length = Math.sqrt(deltaMoveX * deltaMoveX + deltaMoveY * deltaMoveY);
        if (length != 0) {
            deltaMoveX = (int) Math.round(deltaMoveX / length * actualSpeed);
            deltaMoveY = (int) Math.round(deltaMoveY / length * actualSpeed);
        }

        /* 
        there is a problem , If the position touches the boundary, it is impossible 
        to press 2 keys to move diagonally and the result is to move in a 
        normal direction. therefore, valriable collisionOn is for static object and tiles.
        if we use that val for both coordinates x and y , we can not move in the most 
        possible direction. I use 2 val called collisionXOn collisionYOn for the movement
        of player so i can calculate the posible future move. 
        
        -> we have to predict the path that the player can move.
        */ 
        // collide with X
        collisionOn = false;
        collisionXOn = false;
        
        
        int nextX = worldX + deltaMoveX; // posible move X
        int nextY = worldY; // posible move Y
        gp.cChecker.checkTile(this, nextX, nextY);
        int objectIndex = gp.cChecker.checkObject(this, true, nextX, nextY);
        iR.InteractObject(objectIndex);
        if (!collisionXOn && !collisionOn) {
            worldX = nextX;
            //System.out.print(worldX / 16 + " ");
        }
        
        
        //collide with Y
        collisionOn = false;
        collisionYOn = false;
        
        nextX = worldX;
        nextY = worldY + deltaMoveY;
        gp.cChecker.checkTile(this, nextX, nextY);
        objectIndex = gp.cChecker.checkObject(this, true, nextX, nextY);
        iR.InteractObject(objectIndex);
        if (!collisionYOn && !collisionOn) {
            worldY = nextY;
            //System.out.println(worldY / 16);
        }
        //iR.InteractObject(objectIndex); // check player interact with object 
    
        // RESET VAL
        keyH.pickPressOnce = false;
        // COUNTER FOR SPEED
        if(speedTimer > 0){
            speedTimer--;
        }
        if(speedTimer == 0){
            actualSpeed = defaultSpeed; 
        }
    }
    
    public void drawPlayer(Graphics2D g2){
//      g2.setColor(Color.white);// color to draw objects      
//      g2.fillRect(x , y, gp.tileSize , gp.tileSize);

        BufferedImage image = null;
        switch(direction){
        case "up":
            if(spriteNum == 1) image = up1;
            if(spriteNum == 2) image = up2;   
            break;
        case "down":
            if(spriteNum == 1) image = down1;
            if(spriteNum == 2) image = down2;
            break;
        case "left":
            if(spriteNum == 1) image = left1;
            if(spriteNum == 2) image = left2;
            break;
        case "right":
            if(spriteNum == 1) image = right1;
            if(spriteNum == 2) image = right2;
            break;
        }
        g2.drawImage(image, screenX, screenY, null);
        g2.setColor(Color.red);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }       
}
