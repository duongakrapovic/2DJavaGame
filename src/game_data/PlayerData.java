package game_data;

import java.util.List;

/**
 * Serializable data representing the player state.
 */
public class PlayerData {
    public int worldX;
    public int worldY;
    public int health;
    public int maxHealth;
    public String weaponName;
    public int mapIndex;

    public PlayerData(int worldX, int worldY, int health, int maxHealth,
                      String weaponName, int mapIndex) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.health = health;
        this.maxHealth = maxHealth;
        this.weaponName = weaponName;
        this.mapIndex = mapIndex;
    }
}
