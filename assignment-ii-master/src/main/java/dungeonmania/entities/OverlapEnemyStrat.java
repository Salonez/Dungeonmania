package dungeonmania.entities;

import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.map.GameMap;

public class OverlapEnemyStrat {
    public static void enemyStrat(GameMap map, Enemy entity, Player player) {
        if (entity instanceof Mercenary) {
            if (((Mercenary) entity).isAllied()) return;
        }
        map.beginBattle(player, (Enemy) entity);
    }
}
