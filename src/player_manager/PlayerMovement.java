package player_manager;

import main.GamePanel;

public class PlayerMovement {
    private final Player player;
    private final PlayerInteractor pi;

    public PlayerMovement(Player player, GamePanel gp){
        this.player = player;
        this.pi = new PlayerInteractor(player, gp);
    }

    public int[] calculateMovement(){

        // không bấm gì thì đứng yên
        if (!isMoving()) {
            return new int[]{0, 0};
        }

        int dirX = 0;
        int dirY = 0;

        // xác định hướng
        if (player.input.isUpPressed()) {
            player.direction = "up";
            dirY -= 1;
        }
        if (player.input.isDownPressed()) {
            player.direction = "down";
            dirY += 1;
        }
        if (player.input.isLeftPressed()) {
            player.direction = "left";
            dirX -= 1;
        }
        if (player.input.isRightPressed()) {
            player.direction = "right";
            dirX += 1;
        }

        int deltaMoveX;
        int deltaMoveY;

        // đi chéo
        if (dirX != 0 && dirY != 0) {
            // speed / sqrt(2) rồi làm tròn -> nhanh hơn xíu
            double step = player.actualSpeed / Math.sqrt(2.0);
            int moveDiag = (int) Math.round(step);  // với speed=5 -> 4

            deltaMoveX = dirX * moveDiag;
            deltaMoveY = dirY * moveDiag;
        } else {
            // đi thẳng
            deltaMoveX = dirX * player.actualSpeed;
            deltaMoveY = dirY * player.actualSpeed;
        }

        return new int[]{deltaMoveX, deltaMoveY};
    }

    public boolean isMoving(){
        return player.input.isUpPressed() || player.input.isDownPressed() ||
                player.input.isLeftPressed() || player.input.isRightPressed();
    }

    public void move(int dx, int dy){
        player.collisionOn=false;
        player.collisionXOn=false;

        int nextX = player.worldX + dx;
        int nextY = player.worldY;
        pi.allCheck(nextX, nextY);
        if(!player.collisionXOn && !player.collisionOn)
            player.worldX = nextX;

        player.collisionOn=false;
        player.collisionYOn=false;

        nextX = player.worldX;
        nextY = player.worldY + dy;
        pi.allCheck(nextX, nextY);
        if(!player.collisionYOn && !player.collisionOn)
            player.worldY = nextY;
    }
}