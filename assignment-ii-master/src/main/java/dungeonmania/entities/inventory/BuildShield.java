package dungeonmania.entities.inventory;

import java.util.List;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class BuildShield {
    public static InventoryItem build(Inventory inv, EntityFactory factory) {
        List<InventoryItem> items = inv.getItems();
        List<Wood> wood = inv.getEntities(Wood.class);
        List<Treasure> treasure = inv.getTreasure();
        List<Key> keys = inv.getEntities(Key.class);
        items.remove(wood.get(0));
        items.remove(wood.get(1));
        if (treasure.size() >= 1) {
            items.remove(treasure.get(0));
        } else {
            items.remove(keys.get(0));
        }
        return factory.buildShield();
    }
}
