package object_data.weapons;

import main.GamePanel;
import object_data.weapons.Weapon;

import java.awt.*;

public final class Pick extends Weapon {
    public Pick(GamePanel gp) {
        super(gp);
        name = "Pick";
        width  = gp.tileSize;
        height = gp.tileSize;
        collision = false;

        int t = gp.tileSize / 4;
        solidArea = new Rectangle(-t / 2, -t /2, width + t, height + t);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        loadSprite();
    }
    public String spriteKey() { return "pick"; }
    public int atkBoxW(){ return 32; }
    public int atkBoxH(){ return 32; }
    public int windup(){ return 4; }
    public int active(){ return 6; }
    public int recover(){ return 8; }
    public int cooldown(){ return 12; }
    public float atkMultiplier(){ return 0.70f; }
    public int atkFlat(){ return 0; }
}