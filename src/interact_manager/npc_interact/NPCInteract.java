package interact_manager.npc_interact;

import entity.Entity;
import main.GamePanel;
import main.GameState;
import player_manager.Player;
import input_manager.InputController;
import ui.dialogue.DialogueUI;

import java.util.List;

/**
 * NPCInteract.java
 * Handles player–NPC interactions.
 * Uses the NPC list from EntityManager (getNPCs()).
 */
public class NPCInteract {

    private final GamePanel gp;
    private final Player player;
    private final InputController input;

    public NPCInteract(GamePanel gp, Player player, InputController input) {
        this.gp = gp;
        this.player = player;
        this.input = input;
    }

    public void handle(int index) {
        // Invalid NPC index
        if (index == 999) return;

        // When player presses F (pick key)
        if (input.isPicked()) {

            // Get all NPCs in current map
            List<Entity> npcs = gp.em.getNPCs(gp.currentMap);
            if (npcs == null || npcs.isEmpty()) return;

            // Skip if index out of range
            if (index < 0 || index >= npcs.size()) return;

            // Get target NPC
            Entity npc = npcs.get(index);
            if (npc == null) return;

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
}
