/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interact_manager.object_interact;

import interact_manager.object_interact.items.ManaPosionInteraction;
import interact_manager.object_interact.items.KeyInteraction;
import interact_manager.object_interact.items.HealthPosionInteraction;
import interact_manager.object_interact.items.DoorInteraction;
import interact_manager.object_interact.weapon.*;

public class ObjectInteractionFactory {

    public static IObjectInteraction getHandler(String name) {
        if (name == null) return null;

        return switch (name.toLowerCase()) {
            case "key" -> new KeyInteraction();
            case "portal" -> new PortalInteraction();
            case "door" -> new DoorInteraction();
            case "manaposion" -> new ManaPosionInteraction();
            case "healthposion" -> new HealthPosionInteraction();
            case "argonaut hero's sword" -> new SwordInteraction();
            case "leviathan axe" -> new AxeInteraction();
            case "steve's pick" ->  new PickInteraction();
            default -> null;
        };
    }
}

