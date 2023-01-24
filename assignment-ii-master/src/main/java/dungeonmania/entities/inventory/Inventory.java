package dungeonmania.entities.inventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class Inventory implements Serializable {
    private List<InventoryItem> items = new ArrayList<>();
    private List<Wood> wood = getEntities(Wood.class);

    public List<InventoryItem> getItems() {
        return items;
    }

    public boolean add(InventoryItem item) {
        items.add(item);
        return true;
    }

    public void remove(InventoryItem item) {
        items.remove(item);
    }

    public List<String> getBuildables() {

        int wood = count(Wood.class);
        int arrows = count(Arrow.class);
        int treasure = count(Treasure.class);
        int keys = count(Key.class);
        int sunstone = count(SunStone.class);
        int sword = count(Sword.class);
        List<String> result = new ArrayList<>();

        if (wood >= 1 && arrows >= 3) {
            result.add("bow");
        }
        if (wood >= 2 && (treasure >= 1 || keys >= 1)) {
            result.add("shield");
        }
        if (Sceptre.isBuildable(wood, arrows, treasure, keys, sunstone)) {
            result.add("sceptre");
        }
        if (sword >= 1 && sunstone >= 1) {
            result.add("midnight_armour");
        }

        return result;
    }

    public InventoryItem checkBuildCriteria(
        Player p, String item, EntityFactory factory) {
        if (!getBuildables().contains(item)) return null;
        wood = getEntities(Wood.class);
        switch (item) {
        case "bow":
            return BuildBow.build(this, factory);
        case "shield":
            return BuildShield.build(this, factory);
        case "sceptre":
            if (wood.size() >= 1) return BuildSceptreWood.stratWood(this, factory);
            return BuildSceptreArrows.stratArrows(this, factory);
        case "midnight_armour":
            return BuildMidnightArmour.build(this, factory);
        default:
            return null;
        }
    }

    public <T extends InventoryItem> T getFirst(Class<T> itemType) {
        for (InventoryItem item : items)
            if (itemType.isInstance(item)) return itemType.cast(item);
        return null;
    }

    public <T extends InventoryItem> int count(Class<T> itemType) {
        int count = 0;
        for (InventoryItem item : items)
            if (itemType.isInstance(item)) count++;
        return count;
    }

    public Entity getEntity(String itemUsedId) {
        for (InventoryItem item : items)
            if (((Entity) item).getId().equals(itemUsedId)) return (Entity) item;
        return null;
    }

    public List<Entity> getEntities() {
        return items.stream().map(Entity.class::cast).collect(Collectors.toList());
    }

    public <T> List<T> getEntities(Class<T> clz) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }

    public boolean hasWeapon() {
        return getFirst(Sword.class) != null || getFirst(Bow.class) != null;
    }

    public boolean hasSceptre() {
        return getFirst(Sceptre.class) != null;
    }

    public Sceptre getSceptre() {
        return getFirst(Sceptre.class);
    }

    public BattleItem getWeapon() {
        BattleItem weapon = getFirst(Sword.class);
        if (weapon == null)
            return getFirst(Bow.class);
        return weapon;
    }

    public List<Treasure> getTreasure() {
        List<Treasure> treasureList = getEntities(Treasure.class);
        return treasureList.stream().filter(t -> t.getIsUseable()).collect(Collectors.toList());
    }
}
