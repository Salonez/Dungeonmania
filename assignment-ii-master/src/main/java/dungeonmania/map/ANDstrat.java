package dungeonmania.map;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.entities.LogicEntities.LogicalEntity;

public class ANDstrat {
    // check if its currently an and stat.
    public static void updateLogics(GameMap map, LogicalEntity curr) {
        List<Entity> possibles = map.getCardAdjEntities(curr.getPosition())
                                        .stream()
                                        .filter(b ->
                                        (b.getClass().equals(LogicalEntity.class)
                                        || b.getClass().equals(Switch.class)))
                                        .collect(Collectors.toList());
        List<Entity> powered = map.getCardAdjEntities(curr.getPosition())
                                        .stream()
                                        .filter(b -> b.isPowered())
                                        .collect(Collectors.toList());
        if (powered.size() >= 2 && powered.size() == possibles.size()) {
            curr.setStateOn();
            map.setTicks(curr);
        } else {
            curr.setStateOff();
        }
    }
}
