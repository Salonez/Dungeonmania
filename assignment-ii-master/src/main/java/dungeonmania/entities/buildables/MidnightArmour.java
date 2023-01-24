package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;

public class MidnightArmour extends Buildable {

    private double defence;
    private double attack;

    private boolean buffapplied = false;

    public MidnightArmour(double defence, double attack) {
        super(null);
        this.defence = defence;
        this.attack = attack;
    }

    // when there are zombies apply the buff
    // when there are no zomvies remove the buff.
    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        if (!buffapplied) {
            return BattleStatistics.applyBuff(origin, new BattleStatistics(
                0,
                0,
                0,
                1,
                1));
        } else {
            return BattleStatistics.applyBuff(origin, new BattleStatistics(
                0,
                attack,
                defence,
                1,
                1));
        }
    }

    public void setBuffApplied() {
        buffapplied = !buffapplied;
    }

    @Override
    public void use(Game game) {
        return;
    }

    @Override
    public int getDurability() {
        return Integer.MAX_VALUE;
    }

}
