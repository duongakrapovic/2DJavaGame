package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/** Nút URM (Unpause/Replay/Menu) dùng sprite 3 trạng thái: normal/hover/pressed. */
public class UrmButton extends PauseButton {

    // Kích thước từng frame trong sprite gốc (urm_buttons.png)
    private static final int SPRITE_SIZE = 56;

    private BufferedImage[] imgs; // 3 frames
    private final int rowIndex;   // 0 = Resume, 1 = Replay, 2 = Menu
    private int index;            // 0 normal, 1 hover, 2 pressed

    public UrmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage atlas = null;
        try (InputStream is = getClass().getResourceAsStream("/ui/urm_buttons.png")) {
            atlas = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgs = new BufferedImage[3];
        for (int i = 0; i < 3; i++) {
            imgs[i] = atlas.getSubimage(i * SPRITE_SIZE, rowIndex * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
        }
    }

    public void update() {
        index = 0;
        if (mouseOver) index = 1;
        if (mousePressed) index = 2;
    }

    public void draw(Graphics g) {
        g.drawImage(imgs[index], x, y, width, height, null);
    }
}
