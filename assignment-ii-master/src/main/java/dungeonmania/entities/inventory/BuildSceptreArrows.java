package dungeonmania.entities.inventory;

import java.util.List;

import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;

public class BuildSceptreArrows {
    /*
     * returns true if the build removal was sucessfull.
     */
    public static InventoryItem stratArrows(Inventory inv, EntityFactory factory) {
        List<InventoryItem> items = inv.getItems();
        List<Arrow> arrow = inv.getEntities(Arrow.class);
        List<Treasure> treasure = inv.getTreasure();
        List<Key> keys = inv.getEntities(Key.class);
        List<SunStone> sunStones = inv.getEntities(SunStone.class);

        items.remove(arrow.get(0));
        items.remove(sunStones.get(0));
        items.remove(arrow.get(1));
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
