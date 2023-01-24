package dungeonmania.entities.inventory;

import java.util.List;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Wood;

public class BuildBow {
    public static InventoryItem build(Inventory inv, EntityFactory factory) {
        List<InventoryItem> items = inv.getItems();
        List<Wood> wood = inv.getEntities(Wood.class);
        List<Arrow> arrows = inv.getEntities(Arrow.class);
        items.remove(wood.get(0));
        items.remove(arrows.get(0));
        items.remove(arrows.get(1));
        items.remove(arrows.get(2));
        return factory.buildBow();
    }
}
