package object_data.weapons;

import main.GamePanel;

import java.awt.*;

public final class Sword extends Weapon {

    public Sword(GamePanel gp ,int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;
        
        name = "Argonaut hero's sword";
        width  = gp.tileSize;
        height = gp.tileSize;
        collision = false;

        int t = gp.tileSize / 4;
        solidArea = new Rectangle(-t / 2, -t /2, width + t, height + t);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        loadSprite();
    }

    @Override public String spriteKey() { return "sword"; }
    @Override public int atkBoxW() { return 36; }
    @Override public int atkBoxH() { return 36; }
    @Override public int windup() { return 5; }
    @Override public int active() { return 8; }
    @Override public int recover() { return 10; }
    @Override public int cooldown() { return 14; }
    @Override public float atkMultiplier() { return 1.0f; }
    @Override public int atkFlat() { return 0; }
}
