package tile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ChunkManager {
    private TileManager tileM;
    private int chunkSize;
    private Map<String, Chunk> chunks;
    private int warningDistance = 6; // number of tiles from edge to load next chunk

    public ChunkManager(TileManager tileM, int chunkSize){
        // Reference to TileManager (manages all tiles in the game)
        this.tileM = tileM;
        // Size of each chunk (number of tiles per side)
        this.chunkSize = chunkSize;
        // Store all loaded chunks, keyed by "chunkX_chunkY"
        this.chunks = new HashMap<>();
    }

    private String chunkKey(int x, int y){
        // Generate a unique key for each chunk based on its coordinates
        return x + "_" + y;
        // identify chunk file 
    }

    public void loadChunk(int chunkX, int chunkY){
        String key = chunkKey(chunkX, chunkY);
        // Do not reload if the chunk is already loaded
        if(chunks.containsKey(key)) return;

        Chunk c = new Chunk(chunkX, chunkY, chunkSize);

        // Load chunk from a TMX file (CSV encoding)
        try {
            String path = "/maps/chunk" + chunkX + "_" + chunkY + ".tmx";
            InputStream is = getClass().getResourceAsStream(path);
            if(is == null) {
                System.out.println("Chunk not found: " + path);
                return;
            }
            // Read the TMX file content into a single string
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder xml = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) xml.append(line.trim());
            br.close();
            
            // Extract CSV data from <data encoding="csv"> ... </data>
            String content = xml.toString();
            int start = content.indexOf("<data encoding=\"csv\">") + "<data encoding=\"csv\">".length();
            int end = content.indexOf("</data>");
            String csv = content.substring(start, end).trim();
            String[] numbers = csv.split(",");
            
            // Fill the chunk map and global map
            int idx = 0;
            for(int row=0; row<chunkSize; row++){
                for(int col=0; col<chunkSize; col++){
                    int num = Integer.parseInt(numbers[idx].trim());
                    // Inside the chunk map
                    c.mapTileNum[row][col] =  (num == 0) ? 0 : num - 1;
                    // Also update TileManager’s global map
                    tileM.mapTileNum[row + chunkY*chunkSize][col + chunkX*chunkSize] = num - 1; // nếu muốn trùng với tile index

                    idx++;
                }
            }
            // Store the chunk in the active chunk list
            chunks.put(key, c);
            // method ".put" add a pair (key, value) in hashmap
            // key = "chunkX_chunkY"
            // c = loaded object 
            System.out.println("Loaded chunk: " + key);

        } catch(Exception e){ e.printStackTrace(); }
    }

    private void unloadFarChunks(int currentChunkX, int currentChunkY, int distance){
        // Remove all chunks that are farther than "distance" from current chunk
        chunks.entrySet().removeIf(e ->
            /*entrySet() return a set type Map.Entry<K, V>  
            an Entry = 1 pair (key, value)
            key = string , value = Chunk  
                
            removeIf = delete all element when if -> true     
            */ 
                
            Math.abs(e.getValue().chunkX - currentChunkX) > distance ||         
            Math.abs(e.getValue().chunkY - currentChunkY) > distance
            /*e.getValue().chunkX = get pos x of chunk from entry
                */    
        );
    }

    public void updateChunks(int playerWorldX, int playerWorldY){
        // Convert player position in pixels → tile coordinates
        int playerTileX = playerWorldX / tileM.gp.tileSize;
        int playerTileY = playerWorldY / tileM.gp.tileSize;

         // Find current chunk coordinates
        int currentChunkX = playerTileX / chunkSize;
        int currentChunkY = playerTileY / chunkSize;
        
        // Player’s offset inside the current chunk
        int offsetX = playerTileX % chunkSize;
        int offsetY = playerTileY % chunkSize;

        // Always load the current chunk
        loadChunk(currentChunkX, currentChunkY);

        // Near chunk borders → load adjacent chunks
        if(offsetX >= chunkSize - warningDistance) loadChunk(currentChunkX + 1, currentChunkY);
        if(offsetX < warningDistance) loadChunk(currentChunkX - 1, currentChunkY);
        if(offsetY >= chunkSize - warningDistance) loadChunk(currentChunkX, currentChunkY + 1);
        if(offsetY < warningDistance) loadChunk(currentChunkX, currentChunkY - 1);

        // Near chunk corners → load diagonal chunks
        if(offsetX >= chunkSize - warningDistance && offsetY >= warningDistance)
            loadChunk(currentChunkX + 1, currentChunkY + 1);
        if(offsetX < warningDistance && offsetY >= warningDistance)
            loadChunk(currentChunkX - 1, currentChunkY + 1);
        if(offsetX >= chunkSize - warningDistance && offsetY < warningDistance)
            loadChunk(currentChunkX + 1, currentChunkY - 1);
        if(offsetX < warningDistance && offsetY < warningDistance)
            loadChunk(currentChunkX - 1, currentChunkY - 1);

        // Unload chunks that are too far (distance > 1)
        unloadFarChunks(currentChunkX, currentChunkY, 1);
    }

    public Iterable<Chunk> getActiveChunks(){
        // Return all currently loaded chunks
        return chunks.values();
    }
}
