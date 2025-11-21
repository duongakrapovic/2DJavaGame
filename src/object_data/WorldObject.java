package object_data;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class WorldObject {

    // World & screen
    public int worldX, worldY;
    public int width, height;
    public final int screenX;
    public final int screenY;

    // Sprite/static image
    public BufferedImage staticImage;
    public String name;

    // Collision
    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;

    // Map
    public int mapIndex = 0;

    public int value = 0;
    protected final GamePanel gp;

    public WorldObject(GamePanel gp) {
        this.gp = gp;
        this.screenX = gp.screenWidth/2 - (gp.tileSize/2);
        this.screenY = gp.screenHeight/2 - (gp.tileSize/2);
    }

    public void update() {}
    public void draw(Graphics2D g2) {
        if (staticImage != null) {
            g2.drawImage(staticImage, screenX, screenY, null);
        }
    }

    protected BufferedImage setup(String path, int w, int h) {
        try (InputStream is = getClass().getResourceAsStream(path + ".png")) {
            if (is == null) return null;
            BufferedImage src = ImageIO.read(is);
            BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = dst.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(src, 0, 0, w, h, null);
            g.dispose();
            return dst;
        } catch (Exception e) {
            System.err.println("[WorldObject.setup] Load fail: " + path + " -> " + e.getMessage());
            return null;
        }
    }

    public BufferedImage getRenderImage() {
        return staticImage;
    }

}
