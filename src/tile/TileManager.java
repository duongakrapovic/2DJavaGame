/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tile;
// MANAGE TILE IMAGE AND COLLISION
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];
    
    public TileManager(GamePanel gp){
        this.gp = gp;
        
        tile = new Tile[50]; // include the image 
        mapTileNum = new int [gp.maxWorldRow][gp.maxWorldCol];// num on the txt
        
        // load tiles
        loadTileset("/maptiles/tileset.png", gp.originalTileSize);
        //CHECK COLLISION   
        loadTilesetProperties("/maps/tileset.tsx");     
        
        System.out.println("==== TILE COLLISION STATUS ====");
        for(int i=0; i<tile.length; i++){
            System.out.println("Tile " + i + ": collision=" + tile[i].collision);
        }
    }
    
    public void loadTileset(String path, int tileSize) {
        try {
            // Load the tileset image from the given path
            BufferedImage tileset = ImageIO.read(getClass().getResourceAsStream(path));
            // Calculate how many columns and rows of tiles exist in the tileset
            int cols = tileset.getWidth() / tileSize;
            int rows = tileset.getHeight() / tileSize;
            // Create an array to store all tiles
            tile = new Tile[cols * rows];

            int index = 0;
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    // Create a new Tile object for each sub-image
                    tile[index] = new Tile();
                    // getSubImage is a method to cut tiles from an
                    // image that combine all tiles 
                    tile[index].image = tileset.getSubimage(
                        x * tileSize, y * tileSize, tileSize, tileSize
                    );
                    // By default, all tiles are not collision
                    tile[index].collision = false; 
                    index++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadTilesetProperties(String tsxPath) {
        try {
            // Open the .tsx file that contains tile properties
            InputStream is = getClass().getResourceAsStream(tsxPath);
            if(is == null){
                System.out.println("TSX file not found!");
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            int tileIndex = -1; // Store the current tile index being read
            while ((line = br.readLine()) != null) {
                line = line.trim();

                // tìm <tile id="X">
                if (line.startsWith("<tile id=")) {
                    int start = line.indexOf("\"") + 1;
                    int end = line.indexOf("\"", start);
                    tileIndex = Integer.parseInt(line.substring(start, end));
                }

                 // Look for <tile id="X"> to identify which tile properties belong to
                if (line.contains("<property name=\"collision\"")) {
                    if (tileIndex >= 0 && tileIndex < tile.length) {
                        if (line.contains("value=\"true\"")) {
                            tile[tileIndex].collision = true;
                        } 
                        else {
                        tile[tileIndex].collision = false;
                        }
                    }
                }
            }
            br.close();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     public void draw(Graphics2D g2 ,ChunkManager chunkM){
        /*g2.drawImage(tile[0].image, 0 , 0 , gp.tileSize, gp.tileSize, null);
        draw for a tile unit, this is not effective 
        using a tmx file to manage is a popular method
        however, reading the entire tmx file and rendering appropriate tiles 
        or reading the entire txt file and rendering all tiles is not efficient
        we will caculate the start ans end tile. next , we only read and render the tiles 
        that player can see on the screen
       */
        // player position on true map chunk
        int playerPosX = gp.player.worldX;
        int playerPosY = gp.player.worldY;
        //calculate the position of start and end tile
        /*true posX minus a half screen posx = min left. divide tileSize we get 
        the most left tile have to render. however , we minus 1 tileSize at last 
        so that the animtion can be smooth. when we move 1 tile to the left, the 
        code has already pre render 1 tile ahead if player move to the left
        */
        /*   SCREEN         c
            ---------------------------------
            |              |                |
            |              |  gp.           |
            |              |  player.       |
            |              |  screenY       |
            |              |                |
            |              |                |
         a  |-------------- m               |  b
            | gp.player.screenX             |
            |                               |
            |                               |
            |                               |
            |                               |
            |                               |
            ---------------------------------
                            d
        */ 
        // find boundary
        int screenLeft = playerPosX - gp.player.screenX/2;
        int screenTop  = playerPosY - gp.player.screenY/2;
        int screenRight= playerPosX + gp.player.screenX/2;
        int screenBottom= playerPosY + gp.player.screenY/2;
        
        for(Chunk c : chunkM.getActiveChunks()){
            int chunkWorldX = c.chunkX * c.size * gp.tileSize;
            int chunkWorldY = c.chunkY * c.size * gp.tileSize;

            // if chunk out of screen → skip
            if(chunkWorldX + c.size*gp.tileSize < screenLeft) continue;
            if(chunkWorldX > screenRight) continue;
            if(chunkWorldY + c.size*gp.tileSize < screenTop) continue;
            if(chunkWorldY > screenBottom) continue;

            // draw tile in chunk 
            for(int row=0; row<c.size; row++){
                for(int col=0; col<c.size; col++){
                    int tileNum = c.mapTileNum[row][col];// return to number that represen the image tile
                    int tileWorldX = chunkWorldX + col*gp.tileSize;
                    int tileWorldY = chunkWorldY + row*gp.tileSize;

                    int tileScreenX = tileWorldX - playerPosX + gp.player.screenX;
                    int tileScreenY = tileWorldY - playerPosY + gp.player.screenY;

                    g2.drawImage(tile[tileNum].image, tileScreenX, tileScreenY, gp.tileSize, gp.tileSize, null);
                }
            }
            // DRAW OUT LINE OF CHUNK 
            g2.setColor(Color.RED);
            int rectX = chunkWorldX - playerPosX + gp.player.screenX;
            int rectY = chunkWorldY - playerPosY + gp.player.screenY;
            int rectSize = c.size * gp.tileSize;
            g2.drawRect(rectX, rectY, rectSize, rectSize);
            
            
        }
    }
}    
    
    
         