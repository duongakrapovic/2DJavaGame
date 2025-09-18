package tile;

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

    public TileManager(GamePanel gp){
        this.gp = gp;

        tile = null; // load tileset, and collision of tiles 
        loadTileset("/maptiles/tileset.png", gp.originalTileSize);
        loadTilesetProperties("/maptiles/tileset.tsx");  
    }
    public void loadTileset(String path, int tileSize) {
        try {
            BufferedImage tileset = ImageIO.read(getClass().getResourceAsStream(path));
            int cols = tileset.getWidth() / tileSize;
            int rows = tileset.getHeight() / tileSize;
            tile = new Tile[cols * rows];

            int index = 0;
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    tile[index] = new Tile();
                    tile[index].image = tileset.getSubimage(
                        x * tileSize, y * tileSize, tileSize, tileSize
                    );
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
            InputStream is = getClass().getResourceAsStream(tsxPath);
            if(is == null){
                System.out.println("TSX file not found!");
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            int tileIndex = -1;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("<tile id=")) {
                    int start = line.indexOf("\"") + 1;
                    int end = line.indexOf("\"", start);
                    tileIndex = Integer.parseInt(line.substring(start, end));
                }
                if (line.contains("<property name=\"collision\"")) {
                    if (tileIndex >= 0 && tileIndex < tile.length) {
                        tile[tileIndex].collision = line.contains("value=\"true\"");
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g2 ,ChunkManager chunkM){
        int playerPosX = gp.player.worldX;
        int playerPosY = gp.player.worldY;

        int screenLeft   = playerPosX - gp.player.screenX;
        int screenTop    = playerPosY - gp.player.screenY;
        int screenRight  = playerPosX + gp.player.screenX + 5*gp.tileSize;
        int screenBottom = playerPosY + gp.player.screenY + 5*gp.tileSize;

        for(Chunk c : chunkM.getActiveChunks()){
            int chunkWorldX = c.chunkX * c.size * gp.tileSize;
            int chunkWorldY = c.chunkY * c.size * gp.tileSize;

            if(chunkWorldX + c.size*gp.tileSize < screenLeft) continue;
            if(chunkWorldX > screenRight) continue;
            if(chunkWorldY + c.size*gp.tileSize < screenTop) continue;
            if(chunkWorldY > screenBottom) continue;

            for(int row=0; row<c.size; row++){
                for(int col=0; col<c.size; col++){
                    int tileNum = c.mapTileNum[row][col]; // take tile num from chunk
                    if (tileNum < 0 || tileNum >= tile.length) continue;

                    int tileWorldX = chunkWorldX + col*gp.tileSize;
                    int tileWorldY = chunkWorldY + row*gp.tileSize;
                    int tileScreenX = tileWorldX - playerPosX + gp.player.screenX;
                    int tileScreenY = tileWorldY - playerPosY + gp.player.screenY;

                    g2.drawImage(tile[tileNum].image, tileScreenX, tileScreenY, gp.tileSize, gp.tileSize, null);
                }
            }

            // draw red outline to recognize each chunk 
            g2.setColor(Color.RED);
            int rectX = chunkWorldX - playerPosX + gp.player.screenX;
            int rectY = chunkWorldY - playerPosY + gp.player.screenY;
            int rectSize = c.size * gp.tileSize;
            g2.drawRect(rectX, rectY, rectSize, rectSize);
        }
    }
}
