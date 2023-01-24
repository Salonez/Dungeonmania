package dungeonmania.goals;

import dungeonmania.Game;

public class ORgoal implements GoalType {

    @Override
    public boolean achieved(Game game, int target, Goal goal1, Goal goal2) {
        return goal1.achieved(game) || goal2.achieved(game);
    }

    @Override
    public String toString(Game game, Goal goal1, Goal goal2) {
        return "(" + goal1.toString(game) + " OR " + goal2.toString(game) + ")";
    }
}
