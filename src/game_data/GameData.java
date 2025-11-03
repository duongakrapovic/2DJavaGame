package game_data;

import java.util.List;

public class GameData {
    public PlayerData player;
    public List<ObjectData> objects;
    public int mapIndex;
    public String mapPath;

    public GameData(PlayerData player, List<ObjectData> objects, int mapIndex, String mapPath) {
        this.player = player;
        this.objects = objects;
        this.mapIndex = mapIndex;
        this.mapPath = mapPath;
    }
}
