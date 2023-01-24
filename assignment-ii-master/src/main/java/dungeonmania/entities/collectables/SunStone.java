package dungeonmania.entities.collectables;

import dungeonmania.util.Position;

public class SunStone extends Treasure implements KeyFunctionality {
    private int keyNumber;

    public SunStone(Position position, int number) {
        super(position);
        keyNumber = number;
    }

    @Override
    public boolean getIsUseable() {
        return false;
    }

    @Override
    public int getNumber() {
        return keyNumber;
    }
}
