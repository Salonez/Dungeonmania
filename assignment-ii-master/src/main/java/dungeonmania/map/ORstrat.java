package dungeonmania.map;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.LogicEntities.LogicalEntity;

public class ORstrat {
    public static void updateLogics(GameMap map, LogicalEntity curr) {
        List<Entity> powered = map.getCardAdjEntities(curr.getPosition())
                                        .stream()
                                        .filter(b -> b.isPowered())
                                        .collect(Collectors.toList());
        if (powered.size() > 0) {
            curr.setStateOn();
            map.setTicks(curr);
        } else {
            curr.setStateOff();
        }
    }
}
