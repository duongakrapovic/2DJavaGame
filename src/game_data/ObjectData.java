package game_data;

/**
 * Serializable data for objects and monsters in the world.
 */
public class ObjectData {
    public String type;
    public int worldX, worldY;
    public boolean active;

    public ObjectData(String type, int worldX, int worldY, boolean active) {
        this.type = type;
        this.worldX = worldX;
        this.worldY = worldY;
        this.active = active;
    }
}
