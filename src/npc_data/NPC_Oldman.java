package npc_data;

import ai.movement.WanderMovement;
import entity.Entity;
import main.GamePanel;
import ui.effects.DialogueUI;

import java.awt.Rectangle;

public class NPC_Oldman extends Entity {

    public NPC_Oldman(GamePanel gp, int mapIndex){
        super(gp);
        this.mapIndex = mapIndex;
        name = "oldman";

        width = gp.tileSize;
        height = gp.tileSize;
        getImage();

        collision = true;
        animationON = true;
        actualSpeed = 1;

        solidArea = new Rectangle(3, 18, 42, 30);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // đi lang thang, đổi hướng mỗi ~2s
        setController(new WanderMovement(1, 120));

        // ==== hội thoại riêng cho NPC này ====
        setDialogue();
    }

    private void getImage(){
        up1    = setup("/npc/oldman_up_1" , width , height);
        up2    = setup("/npc/oldman_up_2" , width , height);
        down1  = setup("/npc/oldman_down_1" , width , height);
        down2  = setup("/npc/oldman_down_2" , width , height);
        right1 = setup("/npc/oldman_right_1", width , height);
        right2 = setup("/npc/oldman_right_2" , width , height);
        left1  = setup("/npc/oldman_left_1" , width , height);
        left2  = setup("/npc/oldman_left_2" , width , height);
    }

    // ==== THÊM HỘI THOẠI ====
    public void setDialogue() {
        dialogues[0][0] = "Today, you finally turn 15… which means you're allowed to enter the Dungeon for the first time.\n";
        dialogues[0][1] = "I know you're excited — eager to earn your own money and explore the world out there.";
        dialogues[0][2] = "But remember, the Dungeon isn't just treasures… it's filled with monsters and danger.";
        dialogues[0][3] = "For your birthday, I have a gift for you.\n"
                + "This is the Leviathan Axe, the weapon that accompanied me through my youth.\n";
        dialogues[0][4] = "Take it with you, protect yourself… and come back to tell me all about your very first adventure.";

        dialogues[0][5] = "Now, head outside the house. Your first combat awaits near the entrance.";
        dialogues[0][6] = "And remember this — no matter how tough the journey is, you can always return home to rest and recover your health.";
        dialogues[0][7] = "Go on, my child. Your adventure begins now.";

        dialogues[1][0] = "If you become tired, rest at the water.";
        dialogues[1][1] = "However, the monsters reappear if you rest.\nI don't know why but that's how it works.";
        dialogues[1][2] = "In any case, don't push yourself too hard.";

        dialogues[2][0] = "I wonder how to open that door...";
    }

    // ==== HÀNH ĐỘNG NÓI CHUYỆN ====
    @Override
    public void speak(GamePanel gp) {
        // NPC quay mặt về phía người chơi
        facePlayer();

        // Lấy đúng set hội thoại hiện tại
        String[] currentSet = dialogues[dialogueSet];
        if (currentSet != null) {
            gp.uiManager.get(DialogueUI.class).startDialogue(currentSet);
        }
    }
}
