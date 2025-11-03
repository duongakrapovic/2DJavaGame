package game_data;

/**
 * Stores information about each item in the player's inventory.
 */
public class InventoryData {
    public String itemName;
    public int quantity;

    public InventoryData(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }
}
