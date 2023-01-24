package dungeonmania.entities.LogicEntities;

import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public abstract class LogicalEntity extends Entity {

    public LogicalEntity(Position position) {
        super(position);
    }

    public abstract boolean getState();
    public abstract void setStateOn();
    public abstract void setStateOff();
    public abstract String getStrat();

}
