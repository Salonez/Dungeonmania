package dungeonmania.map;

import java.util.List;

import dungeonmania.entities.LogicEntities.LogicalEntity;
import dungeonmania.entities.LogicEntities.Wire;

public class COANDStrat {
    public static void updateLogics(GameMap map, LogicalEntity curr) {
        List<Wire> poweredWires = map.getCardAdjWires(curr.getPosition());
        int count = 0;
        if (poweredWires.size() < 1) return;
        int check = poweredWires.get(0).getTickStarted();
        for (Wire wire : poweredWires) {
            if (wire.getTickStarted() == check) count++;
        }
        if (count >= 2) {
            curr.setStateOn();
            map.setTicks(curr);
        } else {
            curr.setStateOff();
        }
    }
}
