package dungeonmania.entities;

import java.util.HashMap;

import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwampTile extends Entity {
    public static final int DEFAULT_MOVEMENT_FACTOR = 1;

    private int movementFactor = SwampTile.DEFAULT_MOVEMENT_FACTOR;

    public SwampTile(Position position, int movementFactor) {
        super(position.asLayer(Entity.FLOOR_LAYER));
        this.movementFactor = movementFactor;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (!(entity instanceof Player)) {
            if (entity instanceof Mercenary) {
                Mercenary merc = (Mercenary) entity;
                if (merc.isStuckToPlayer()) return;
            }
            // trap entity
            HashMap<Entity, Integer> updatedStuckEntities = map.getStuckEntities();
            updatedStuckEntities.put(entity, movementFactor);
            map.setStuckEntities(updatedStuckEntities);
        }
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        return;
    }

    @Override
    public void onDestroy(GameMap gameMap) {
        return;
    }

    public int getMovementFactor() {
        return movementFactor;
    }

}
