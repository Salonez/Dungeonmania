package dungeonmania.entities.LogicEntities;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwitchDoor extends LogicalEntity {

    private boolean state;
    private String strat;

    public SwitchDoor(Position position, String strat) {
        super(position);
        state = false;
        this.strat = strat;
    }

    @Override
    public boolean getState() {
        return state;
    }

    @Override
    public void setStateOn() {
        state = true;
    }

    @Override
    public void setStateOff() {
        state = false;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (state || entity instanceof Spider) {
            return true;
        }

        return (entity instanceof Player && state);
    }

    @Override
    public String getStrat() {
        return strat;
    }
}
