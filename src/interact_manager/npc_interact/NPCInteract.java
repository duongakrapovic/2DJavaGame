package interact_manager.npc_interact;

import entity.Entity;
import main.GamePanel;
import main.GameState;
import player_manager.Player;
import input_manager.InputController;
import ui.effects.DialogueUI;
import ui.effects.MessageUI;   // <<< thêm import này

import java.util.List;

public class NPCInteract {

    private final GamePanel gp;
    private final Player player;
    private final InputController input;
    private final MessageUI msgUI;   // <<< thêm field

    public NPCInteract(GamePanel gp, Player player, InputController input) {
        this.gp = gp;
        this.player = player;
        this.input = input;
        this.msgUI = gp.uiManager.get(MessageUI.class);  // <<< lấy sẵn
    }

    public void handle(int index) {
        // Invalid NPC index
        if (index == 999) return;

        // Lấy list NPC ở map hiện tại
        List<Entity> npcs = gp.em.getNPCs(gp.currentMap);
        if (npcs == null || npcs.isEmpty()) return;
        if (index < 0 || index >= npcs.size()) return;

        // NPC mà player đang đứng sát
        Entity npc = npcs.get(index);
        if (npc == null) return;

        // Nếu đang ở trạng thái PLAY mới xử lý
        if (gp.gsm.getState() != GameState.PLAY) return;

        // ========== HINT KHI LẠI GẦN ÔNG NỘI ==========
        // Chỉ hiện khi CHƯA bấm F
        if (!input.isPicked()) {
            if (msgUI != null && "OldMan".equals(npc.name)) {  // name của NPC là OldMan
                msgUI.showTouchMessage(
                        "Press F to talk to your grandpa.",
                        npc,
                        gp
                );
            }
            // chưa bấm F thì chỉ hint, không mở thoại
            return;
        }

        // ========== ĐOẠN DƯỚI GIỮ Y NGUYÊN NHƯ CŨ ==========
        // When player presses F (pick key)
        // If dialogue box is already open, ignore
        DialogueUI dialogue = gp.uiManager.get(DialogueUI.class);
        if (dialogue != null && dialogue.isActive()) return;
        if (gp.gsm.getState() != GameState.PLAY) return;

        // Prevent spamming the F key
        if (!player.isInteracting()) {
            npc.facePlayer();
            npc.speak(gp);
            player.setInteracting(true);
        } else {
            // Reset when player releases F
            player.setInteracting(false);
        }
    }
}
