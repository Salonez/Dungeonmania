package dungeonmania.entities;

import dungeonmania.map.GameMap;

import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.util.Position;

public class Door extends Entity {
    private boolean open = false;
    private int number;

    public Door(Position position, int number) {
        super(position.asLayer(Entity.DOOR_LAYER));
        this.number = number;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (open || entity instanceof Spider) {
            return true;
        }

        return (entity instanceof Player && hasKey((Player) entity));
    }


    // these functions need to work for sunstones too.
    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (!(entity instanceof Player))
            return;

        Player player = (Player) entity;
        Inventory inventory = player.getInventory();

        InventoryItem key = getKey(player);
        if (key != null) {
            if (key.getClass().equals(Key.class)) {
                inventory.remove(key);
            }
            open();
        }
    }

    private boolean hasKey(Player player) {
        Inventory inventory = player.getInventory();
        Key key = inventory.getFirst(Key.class);
        SunStone stone = inventory.getFirst(SunStone.class);

        if (stone != null && stone.getNumber() == number) return true;
        return (key != null && key.getNumber() == number);
    }

    private InventoryItem getKey(Player player) {
        Inventory inventory = player.getInventory();
        Key key = inventory.getFirst(Key.class);
        SunStone stone = inventory.getFirst(SunStone.class);

        if (stone != null && stone.getNumber() == number) return stone;
        if (key != null && key.getNumber() == number) return key;
        return null;
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
    }
}
