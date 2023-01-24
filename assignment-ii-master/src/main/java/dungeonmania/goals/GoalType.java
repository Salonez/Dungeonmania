package dungeonmania.goals;

import java.io.Serializable;

import dungeonmania.Game;

public interface GoalType extends Serializable {
    public boolean achieved(Game game, int target, Goal goal1, Goal goal2);
    public String toString(Game game, Goal goal1, Goal goal2);
}
