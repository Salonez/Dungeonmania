package dungeonmania.entities.inventory;

import java.util.List;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;

public class BuildMidnightArmour {
    public static InventoryItem build(Inventory inv, EntityFactory factory) {
        List<InventoryItem> items = inv.getItems();
        List<SunStone> sunStones = inv.getEntities(SunStone.class);
        List<Sword> sword = inv.getEntities(Sword.class);
        items.remove(sunStones.get(0));
        items.remove(sword.get(0));
        return factory.buildMidnightArmour();
    }
}
