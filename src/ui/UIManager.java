package ui;

import java.awt.Graphics2D;
import java.util.List;
import java.util.ArrayList;

public class UIManager {
    private List<BaseUI> uiList = new ArrayList<>();

    public void add(BaseUI ui){ uiList.add(ui); }

    public void update(){ uiList.forEach(BaseUI::update); }

    public void draw(Graphics2D g2){ uiList.forEach(ui -> ui.draw(g2)); }
}
