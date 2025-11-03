package game_data;

/**
 * Stores the currently equipped weapon/armor.
 * (Reserved for expansion; integrated into PlayerData for now)
 */
public class EquipmentData {
    public String weapon;
    public String armor;

    public EquipmentData(String weapon, String armor) {
        this.weapon = weapon;
        this.armor = armor;
    }
}
