package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.Switch;


public class BouldersGoal implements GoalType {

    @Override
    public boolean achieved(Game game, int target, Goal goal1, Goal goal2) {
        return game.getMap().getEntities(Switch.class).stream().allMatch(s -> s.isActivated());
    }

    @Override
    public String toString(Game game, Goal goal1, Goal goal2) {
        return ":boulders";
    }

}
