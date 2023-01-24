package dungeonmania.entities.LogicEntities;

import dungeonmania.util.Position;

public class LightBulb extends LogicalEntity {

    private boolean state;
    private String strat;

    public LightBulb(Position position, String strat) {
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
    public String getStrat() {
        return strat;
    }

}
