package game_data;

import java.util.List;

public class PlayerData {
    public int worldX;
    public int worldY;
    public int health;
    public int maxHealth;
    public String weaponName;
    public int mapIndex;
    public int exp ;
    public int level ;
    public PlayerData(int worldX, int worldY, int health, int maxHealth,
                      String weaponName, int mapIndex, int exp , int level) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.health = health;
        this.maxHealth = maxHealth;
        this.weaponName = weaponName;
        this.mapIndex = mapIndex;
        this.exp = exp;
        this.level = level;
    }
}
