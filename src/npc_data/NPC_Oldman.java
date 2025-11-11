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
        dialogues[0][0] = "Hôm nay cháu tròn 15 tuổi rồi… cũng là ngày đầu tiên được phép bước vào Dungeon.\n";
        dialogues[0][1] = "Ông biết cháu háo hức lắm — muốn tự kiếm tiền, muốn khám phá thế giới ngoài kia.";
        dialogues[0][2] = "Nhưng hãy nhớ, Dungeon không chỉ có kho báu đâu… mà còn đầy rẫy quái vật và hiểm nguy.";
        dialogues[0][3] = "Nhân dịp sinh nhật, ông có món quà cho cháu.\n" +
                "Đây là Rìu Leviathan, vũ khí đã theo ông suốt thời trai trẻ.\n";
        dialogues[0][4] = "Hãy mang nó theo, bảo vệ bản thân… và trở về kể cho ông nghe chuyến phiêu lưu đầu tiên của cháu nhé.";

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
