package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.util.Position;

public abstract class Spawner extends Entity implements Interactable {

    public Spawner(Position position) {
        super(position);
    }

    public abstract void spawn(Game game);
}
