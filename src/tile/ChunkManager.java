package tile;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.GamePanel;

/**
 * ChunkManager.java
 * Handles loading, unloading, and managing chunks (sections of the tile map) around the player.
 * Supports asynchronous loading to avoid blocking the main game thread.
 */
public class ChunkManager {
    // Size of each chunk (number of tiles per side)
    private final int chunkSize;
    // Map storing loaded chunks using "chunkX_chunkY" as key
    private final Map<String, Chunk> chunks;
    // Reference to the main game panel
    private final GamePanel gp;
    // Single-thread executor for background chunk loading
    private ExecutorService loader = Executors.newSingleThreadExecutor(); // private thread for load chunk
    // Current map path
    public String pathMap = "map0"; 

    public ChunkManager(int chunkSize , GamePanel gp){
        this.chunkSize = chunkSize;
        this.chunks = new HashMap<>();
        this.gp = gp;
    }
    // Generate a unique key for chunk coordinates
    private String chunkKey(int x, int y){
        return x + "_" + y;
    }

    /**
     * Load a chunk from file (runs in background thread)
     * @param chunkX chunk X index
     * @param chunkY chunk Y index
     * @param pathMap map folder
     * @return loaded Chunk or null if file not found/error
     */
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

    /**
     * Load chunk asynchronously (non-blocking)
     */
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

    /**
     * Unload chunks that are far outside the visible area
     */
    private void unloadFarChunks(int left, int right, int top, int bottom){
        synchronized (chunks) {
            chunks.entrySet().removeIf(e -> {
                int cx = e.getValue().chunkX;
                int cy = e.getValue().chunkY;
                return cx < left - 1 || cx > right + 1 || cy < top - 1 || cy > bottom + 1;
            });
        }
    }

    /**
     * Update chunks around the player: load visible chunks and unload distant chunks
     * @param playerWorldX player's world X coordinate
     * @param playerWorldY player's world Y coordinate
     */
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

     /** Get currently active chunks (returns a copy to avoid concurrent modification) */
    public Iterable<Chunk> getActiveChunks(){
        synchronized (chunks) {
            return new HashMap<>(chunks).values(); 
        }
    }
    /**
     * Force load a new map immediately (clears old chunks)
     */
    public void loadMap(String mapName) {
        clearChunks();                // xoá toàn bộ chunk cũ
        this.pathMap = mapName;       // cập nhật đường dẫn map mới
    }
    /** Shutdown the background loader thread */
    public void shutdown(){
        loader.shutdownNow();
    }
    public void loadAllChunksSync() {
        synchronized (chunks) {
            chunks.clear();
        }

        for (int cx = 0; cx < gp.chunkSize; cx++) {
            for (int cy = 0; cy < gp.chunkSize; cy++) {
                // Giới hạn biên map nếu cần
                if (cx < 0 || cy < 0 || cx >= gp.chunkSize || cy >= gp.chunkSize) continue;

                Chunk c = loadChunkFromFile(cx, cy, pathMap);
                if (c != null) {
                    synchronized (chunks) {
                        chunks.put(chunkKey(cx, cy), c);
                    }
                }
            }
        }
    }

}
