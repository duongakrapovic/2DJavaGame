package ui.effects;

import main.GamePanel;
import main.GameState;
import ui.base.BaseUI;
import java.awt.*;

public class DialogueUI extends BaseUI {

    private String[] lines;
    private int index = 0, charIndex = 0, frame = 0;
    private final int SPEED = 2;
    private final StringBuilder text = new StringBuilder();
    private boolean finished = false;
    private boolean prevPressed = false; // chống spam phím

    public DialogueUI(GamePanel gp) { super(gp); }

    public void startDialogue(String[] src) {
        this.lines = (src == null || src.length == 0) ? new String[]{""} : src;
        this.index = 0;
        this.charIndex = 0;
        this.frame = 0;
        this.finished = false;
        this.prevPressed = false;
        text.setLength(0);
        gp.gsm.setState(GameState.DIALOGUE);
    }

    public boolean isActive() {
        return gp.gsm.getState() == GameState.DIALOGUE && lines != null && index < lines.length;
    }

    public void nextPage() {
        if (lines == null) return;
        String line = (lines[index] == null) ? "" : lines[index];
        if (!finished) {
            // Hoàn tất dòng hiện tại
            text.setLength(0);
            text.append(line);
            charIndex = line.length();
            finished = true;
        } else {
            nextLine();
        }
    }

    @Override
    public void update() {
        if (lines == null || index >= lines.length) return;

        String line = lines[index] == null ? "" : lines[index];

        // Hiệu ứng gõ từng ký tự
        if (!finished && ++frame >= SPEED) {
            if (charIndex < line.length()) text.append(line.charAt(charIndex++));
            else finished = true;
            frame = 0;
        }

        // Lấy input của player
        var p = gp.em.getPlayer();
        if (p == null || p.input == null) return;
        boolean pressed = p.input.isTalkPressed();

        // Xử lý khi nhấn E một lần (chống spam)
        if (pressed && !prevPressed) {
            if (!finished) {
                // Bỏ qua hiệu ứng, hiển thị toàn bộ
                text.setLength(0);
                text.append(line);
                charIndex = line.length();
                finished = true;
            } else {
                nextLine(); // gọi hàm chuẩn thay vì tự ++index
            }
        }

        // Reset chống spam (giống enterPressed=false của RyiSnow)
        prevPressed = pressed;
    }

    private void nextLine() {
        index++;
        if (lines != null && index < lines.length && lines[index] != null) {
            // Reset trạng thái để in dòng mới
            text.setLength(0);
            charIndex = 0;
            frame = 0;
            finished = false;
        } else {
            endDialogue();
        }
    }

    private void endDialogue() {
        text.setLength(0);
        lines = null;
        finished = false;
        index = charIndex = 0;

        // Chuyển về PLAY, nhưng delay 1 frame để không bị NPCInteract đọc lại phím
        gp.gsm.setState(GameState.PLAY);

        // Reset input E sau khi out
        var p = gp.em.getPlayer();
        if (p != null && p.input != null) {
            p.input.resetTalkKey(); // bạn sẽ thêm hàm này vào InputController
        }

        prevPressed = false;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!isActive()) return;

        int x = gp.tileSize;
        int y = gp.screenHeight - gp.tileSize * 4;
        int w = gp.screenWidth - 2 * x;
        int h = gp.tileSize * 3;

        // Nền hộp thoại
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(x, y, w, h, 20, 20);

        // Viền
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, w, h, 20, 20);

        // Text hội thoại
        g2.setFont(new Font("Arial", Font.PLAIN, 22));
        g2.setColor(Color.white);
        drawMultiline(g2, text.toString(), x + gp.tileSize / 2, y + gp.tileSize, w - gp.tileSize);

        // ====== Hint "[E] to continue" ======
        if (finished) { // chỉ hiện khi đã in xong dòng hiện tại
            String hint;

            // nếu còn dòng tiếp theo -> continue, nếu là dòng cuối -> close
            boolean hasMore = (lines != null && index < lines.length - 1);
            if (hasMore) {
                hint = "[E] to continue";
            } else {
                hint = "[E] to close";
            }

            FontMetrics fm = g2.getFontMetrics();
            int hintWidth = fm.stringWidth(hint);

            int paddingX = gp.tileSize / 2;
            int paddingY = gp.tileSize / 3;

            int hintX = x + w - paddingX - hintWidth;
            int hintY = y + h - paddingY;

            g2.setColor(new Color(255, 255, 255, 210)); // trắng hơi mờ
            g2.drawString(hint, hintX, hintY);
        }
    }


    private void drawMultiline(Graphics2D g2, String s, int x, int y, int width) {
        FontMetrics fm = g2.getFontMetrics();
        int lh = fm.getHeight();
        StringBuilder line = new StringBuilder();
        for (String word : s.split(" ")) {
            if (fm.stringWidth(line + word) < width)
                line.append(word).append(" ");
            else {
                g2.drawString(line.toString(), x, y);
                line = new StringBuilder(word + " ");
                y += lh;
            }
        }
        g2.drawString(line.toString(), x, y);
    }

    @Override
    public boolean shouldRenderIn(GameState st) {
        return st == GameState.DIALOGUE;
    }

    @Override
    public boolean shouldUpdate() {
        return gp.gsm.getState() == GameState.DIALOGUE;
    }
}
