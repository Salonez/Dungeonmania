package dungeonmania.entities.LogicEntities;

import dungeonmania.util.Position;

public class Wire extends LogicalEntity {

    private boolean state;
    private int tickstarted;

    public Wire(Position position) {
        super(position);
        tickstarted = 0;
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

    public int getTickStarted() {
        return tickstarted;
    }

    public void setTickStarted(int tick) {
        tickstarted = tick;
    }

    @Override
    public String getStrat() {
        return null;
    }

}
