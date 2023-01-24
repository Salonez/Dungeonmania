package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;

public class Sceptre extends Buildable {

    // mind control duration is passed in through the config
    private int duration;

    public Sceptre(int mindControlDuration) {
        super(null);
        duration = mindControlDuration;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            0,
            0,
            0,
            0));
    }

    @Override
    public void use(Game game) {
        return;
    }

    @Override
    public int getDurability() {
        return Integer.MAX_VALUE;
    }

    public int getDuration() {
        return duration;
    }

    public static boolean isBuildable(int wood, int arrows, int treasure, int keys, int sunstone) {
        if (wood >= 1 || arrows >= 2) {
            if (keys >= 1 || treasure >= 1) {
                if (sunstone >= 1) {
                    return true;
                }
            }
        }
        return false;
    }
}
