package dungeonmania.entities.enemies;

import java.util.Random;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.util.Position;
import dungeonmania.entities.collectables.Treasure;

public class Assassin extends Mercenary {
    public static final double DEFAULT_ATTACK = 10.0;
    public static final double DEFAULT_HEALTH = 50.0;
    public static final int DEFAULT_BRIBE_AMOUNT = 5;
    // public static final int DEFAULT_BRIBE_RADIUS = 10;
    public static final double DEFAULT_BRIBE_FAIL_RATE = 0.5;

    private double bribeFailRate = Assassin.DEFAULT_BRIBE_FAIL_RATE;

    public Assassin(Position position, double health, double attack,
    int bribeAmount, int bribeRadius, double bribeFailRate) {
        super(position, health, attack, bribeAmount, bribeRadius);
        this.bribeFailRate = bribeFailRate;
    }

    @Override
    public void interact(Player player, Game game) {
        if (player.hasSceptre()) {
            // skip checking if bribe rate failed and just get mind controlled.
            super.interact(player, game);
        }
        if (new Random().nextDouble() >= bribeFailRate) {
            super.interact(player, game);
        } else {
            // Failed bribe and so lose money
            for (int i = 0; i < super.getBribeAmount(); i++) {
                player.use(Treasure.class);
            }
        }
    }
}
