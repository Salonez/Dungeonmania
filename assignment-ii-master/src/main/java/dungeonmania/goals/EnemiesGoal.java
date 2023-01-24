package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Spawner;

public class EnemiesGoal implements GoalType {

    @Override
    public boolean achieved(Game game, int target, Goal goal1, Goal goal2) {
        if (game.countEntities(Spawner.class) == 0) {
            if (game.getEnemiesDestroyed() >= target)
                return true;
        }
        return false;
    }

    @Override
    public String toString(Game game, Goal goal1, Goal goal2) {
        return ":enemies";
    }

}
