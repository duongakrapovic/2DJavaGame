package tile;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.GamePanel;

public class ChunkManager {
    private final int chunkSize;
    private final Map<String, Chunk> chunks;
    private final GamePanel gp;
    private ExecutorService loader = Executors.newSingleThreadExecutor(); // private thread for load chunk

    public String pathMap = "map1"; 

    public ChunkManager(int chunkSize , GamePanel gp){
        this.chunkSize = chunkSize;
        this.chunks = new HashMap<>();
        this.gp = gp;
    }

    private String chunkKey(int x, int y){
        return x + "_" + y;
    }

    /** Load chunk from file (run in background) */
    private Chunk loadChunkFromFile(int chunkX, int chunkY, String pathMap){
        Chunk c = new Chunk(chunkX, chunkY, chunkSize);
        try {
            String path = "/" + pathMap + "/chunk" + chunkX + "_" + chunkY + ".tmx";
            InputStream is = getClass().getResourceAsStream(path);
            if (is == null) {
                // System.out.println("Không tìm thấy file: " + path);
                return null;
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
                    c.mapTileNum[row][col] = (num == 0) ? 0 : num - 1; 
                    idx++;
                }
            }
            return c;

        } catch(Exception e){ 
            e.printStackTrace(); 
            return null;
        }
    }

    /** Load chunk asynchronous */
    public void loadChunkAsync(int chunkX, int chunkY , String pathMap){
        String key = chunkKey(chunkX, chunkY);
        synchronized (chunks) {
            if(chunks.containsKey(key)) return; // đã có thì bỏ qua
        }

        loader.submit(() -> {
            // proof for multithreading
            //System.out.println("Loading chunk " + key + " @" + System.nanoTime());
            
            Chunk c = loadChunkFromFile(chunkX, chunkY, pathMap);
            if(c != null){
                synchronized (chunks) {
                    chunks.put(key, c);
                }
            }
        });
    }

    /** clear chunks that is too far */
    private void unloadFarChunks(int left, int right, int top, int bottom){
        synchronized (chunks) {
            chunks.entrySet().removeIf(e -> {
                int cx = e.getValue().chunkX;
                int cy = e.getValue().chunkY;
                return cx < left - 1 || cx > right + 1 || cy < top - 1 || cy > bottom + 1;
            });
        }
    }

    /** Update chunks around player */
    public void updateChunks(int playerWorldX, int playerWorldY){
        int buffer = gp.tileSize * (chunkSize / 2);

        int screenLeft   = playerWorldX - gp.em.getPlayer().screenX - buffer;
        int screenRight  = playerWorldX + gp.em.getPlayer().screenX + buffer;
        int screenTop    = playerWorldY - gp.em.getPlayer().screenY - buffer;
        int screenBottom = playerWorldY + gp.em.getPlayer().screenY + buffer;

        int chunkLeft   = screenLeft / (chunkSize * gp.tileSize);
        int chunkRight  = screenRight / (chunkSize * gp.tileSize);
        int chunkTop    = screenTop / (chunkSize * gp.tileSize);
        int chunkBottom = screenBottom / (chunkSize * gp.tileSize);

        for(int cx = chunkLeft; cx <= chunkRight; cx++){
            for(int cy = chunkTop; cy <= chunkBottom; cy++){
                if (cx < 0 || cy < 0 || cx >= gp.chunkSize || cy >= gp.chunkSize) {
                    continue; // ngoài map thì bỏ qua
                }
                loadChunkAsync(cx, cy , pathMap);
            }
        }
        unloadFarChunks(chunkLeft, chunkRight, chunkTop, chunkBottom);
        //System.out.println("complete update chunk");
    }

    /** clear all chunks */
    public void clearChunks(){
        synchronized (chunks) {
            chunks.clear();
        }
    }

    /** get active chunks  */
    public Iterable<Chunk> getActiveChunks(){
        synchronized (chunks) {
            return new HashMap<>(chunks).values(); // trả về bản copy để tránh ConcurrentModificationException
        }
    }

    /** end chunk's thread if end game */
    public void shutdown(){
        loader.shutdownNow();
    }
}
