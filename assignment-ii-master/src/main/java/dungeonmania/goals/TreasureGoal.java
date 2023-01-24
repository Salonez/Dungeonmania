package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.collectables.Treasure;

public class TreasureGoal implements GoalType {

    @Override
    public boolean achieved(Game game, int target, Goal goal1, Goal goal2) {
        return game.getInitialTreasureCount() - game.getMap().getEntities(Treasure.class).size() >= target;
    }

    @Override
    public String toString(Game game, Goal goal1, Goal goal2) {
        return ":treasure";
    }
}
