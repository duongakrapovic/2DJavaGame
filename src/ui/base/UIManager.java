package ui.base;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import main.GameState;

public class UIManager {
    private final List<BaseUI> uiList = new ArrayList<>();

    public void add(BaseUI ui) { uiList.add(ui); }

    public void update(GameState state) {
        for (BaseUI ui : uiList) {
            if (ui.shouldRenderIn(state)) {
                ui.update();
            }
        }
    }

    public void draw(Graphics2D g2, GameState state) {
        for (BaseUI ui : uiList) {
            if (ui.shouldRenderIn(state) && ui.shouldDraw()) {
                ui.draw(g2);
            }
        }
    }

    public <T extends BaseUI> T get(Class<T> type) {
        for (BaseUI ui : uiList) {
            if (type.isInstance(ui))
                return type.cast(ui);
        }
        return null;
    }
}
