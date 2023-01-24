package dungeonmania.entities.inventory;

import java.util.List;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class BuildSceptreWood {
    /*
     * returns true if the build removal was sucessfull.
     */
    public static InventoryItem stratWood(Inventory inv, EntityFactory factory) {
        List<InventoryItem> items = inv.getItems();
        List<Wood> wood = inv.getEntities(Wood.class);
        List<Treasure> treasure = inv.getTreasure();
        List<Key> keys = inv.getEntities(Key.class);
        List<SunStone> sunStones = inv.getEntities(SunStone.class);

        items.remove(wood.get(0));
        items.remove(sunStones.get(0));
        if (sunStones.size() >= 2) {
            return factory.buildSceptre();
        } else if (treasure.size() >= 1) {
            items.remove(treasure.get(0));
        } else {
            items.remove(keys.get(0));
        }

        return factory.buildSceptre();
    }
}
