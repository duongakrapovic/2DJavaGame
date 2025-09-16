package tile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import main.GamePanel;

public class ChunkManager {
    final private int chunkSize;
    final private Map<String, Chunk> chunks;
    final private GamePanel gp;
    
    public ChunkManager(int chunkSize , GamePanel gp){
        this.chunkSize = chunkSize;
        this.chunks = new HashMap<>();
        this.gp = gp;
    }

    private String chunkKey(int x, int y){
        return x + "_" + y;
    }

    private void loadChunk(int chunkX, int chunkY){
        String key = chunkKey(chunkX, chunkY);
        if(chunks.containsKey(key)) return;

        Chunk c = new Chunk(chunkX, chunkY, chunkSize);

        try {
            String path = "/maps/chunk" + chunkX + "_" + chunkY + ".tmx";
            InputStream is = getClass().getResourceAsStream(path);
            if(is == null) {
                System.out.println("Chunk not found: " + path);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder xml = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) xml.append(line.trim());
            br.close();
            
            String content = xml.toString();
            int start = content.indexOf("<data encoding=\"csv\">") + "<data encoding=\"csv\">".length();
            int end = content.indexOf("</data>");
            String csv = content.substring(start, end).trim();
            String[] numbers = csv.split(",");
            
            int idx = 0;
            for(int row=0; row<chunkSize; row++){
                for(int col=0; col<chunkSize; col++){
                    int num = Integer.parseInt(numbers[idx].trim());
                    c.mapTileNum[row][col] = (num == 0) ? 0 : num - 1; // 🟢 chỉ lưu trong chunk
                    idx++;
                }
            }
            chunks.put(key, c);
            System.out.println("Loaded chunk: " + key);

        } catch(Exception e){ e.printStackTrace(); }
    }

    private void unloadFarChunks(int left, int right, int top, int bottom){
        chunks.entrySet().removeIf(e -> {
            int cx = e.getValue().chunkX;
            int cy = e.getValue().chunkY;
            return cx < left - 1 || cx > right + 1 || cy < top - 1 || cy > bottom + 1;
        });
    }

    public void updateChunks(int playerWorldX, int playerWorldY){
        int buffer = gp.tileSize * (chunkSize / 2);

        int screenLeft   = playerWorldX - gp.player.screenX - buffer;
        int screenRight  = playerWorldX + gp.player.screenX + buffer;
        int screenTop    = playerWorldY - gp.player.screenY - buffer;
        int screenBottom = playerWorldY + gp.player.screenY + buffer;

        int chunkLeft   = screenLeft / (chunkSize * gp.tileSize);
        int chunkRight  = screenRight / (chunkSize * gp.tileSize);
        int chunkTop    = screenTop / (chunkSize * gp.tileSize);
        int chunkBottom = screenBottom / (chunkSize * gp.tileSize);

        for(int cx = chunkLeft; cx <= chunkRight; cx++){
            for(int cy = chunkTop; cy <= chunkBottom; cy++){
                loadChunk(cx, cy);
            }
        }
        unloadFarChunks(chunkLeft, chunkRight, chunkTop, chunkBottom);
    }

    public Iterable<Chunk> getActiveChunks(){
        return chunks.values();
    }
}
