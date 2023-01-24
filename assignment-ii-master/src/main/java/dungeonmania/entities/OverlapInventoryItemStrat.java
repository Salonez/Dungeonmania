package dungeonmania.entities;

import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.map.GameMap;

public class OverlapInventoryItemStrat {
    public static void inventoryStrat(GameMap map, Entity entity, Player player, Inventory inventory) {
        if (entity instanceof Bomb) {
            Bomb b = (Bomb) entity;
            if (b.getState() != Bomb.State.SPAWNED) return;
            b.unsub();
            b.setState(Bomb.State.INVENTORY);
        }
        if (entity instanceof Key && inventory.count(Key.class) == 1) {
            // Can't hold more than one key.
            return;
        }
        player.pickUp(entity);
        map.destroyEntity(entity);
    }
}
