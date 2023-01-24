package dungeonmania.entities.enemies;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class StandardMove {
    private static Random randGen = new Random();

    static void standardMove(Game game, Entity entity) {
        Position nextPos;
        GameMap map = game.getMap();
        List<Position> pos = entity.getCardAdjPos();
        pos = pos
            .stream()
            .filter(p -> map.canMoveTo(entity, p)).collect(Collectors.toList());
        if (pos.size() == 0) {
            nextPos = entity.getPosition();
            game.getMap().moveTo(entity, nextPos);
        } else {
            nextPos = pos.get(randGen.nextInt(pos.size()));
            game.getMap().moveTo(entity, nextPos);
        }
    }
}
