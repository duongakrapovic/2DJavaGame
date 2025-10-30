package utilz;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class LoadSave {

    public static final String PAUSE_BACKGROUND = "pause_background.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";

    /**
     * Giữ nguyên phong cách bản gốc: dùng try–finally và is.close().
     * Nhưng mở rộng thêm để tìm ảnh trong /ui/, /monster/, /object/, /player/, ...
     */
    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;

        // Các đường dẫn có thể chứa ảnh
        String[] searchPaths = {
                "/ui/" + fileName,
                "/object/" + fileName,
                "/player/" + fileName,
                "/" + fileName
        };

        InputStream is = null;

        for (String path : searchPaths) {
            try {
                is = LoadSave.class.getResourceAsStream(path);
                if (is != null) {
                    img = ImageIO.read(is);
                    return img;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }
}
