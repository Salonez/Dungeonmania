package dungeonmania.entities.enemies;

import java.util.HashMap;
import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.TeleportableInterface;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable, TeleportableInterface {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;
    private boolean allied = false;
    private Player ally;
    private boolean stuckToPlayer = false;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
    }

    public boolean isAllied() {
        return allied;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied) return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        return Math.abs(player.getPosition().getX() - getPosition().getX())
        + Math.abs(player.getPosition().getY() - getPosition().getY())
        <= bribeRadius
        && player.getBribeableTreasureCount() >= bribeAmount;
    }

    /**
     * bribe the merc
     */
    private void bribe(Player player) {
        List<Treasure> useable = player.getUseableTreasure();
        for (Treasure t : useable) {
            player.use(t);
        }

        ally = player;
    }

    @Override
    public void interact(Player player, Game game) {
        if (!player.hasSceptre()) {
            bribe(player);
        } else {
            ally = player;
            GameMap map = game.getMap();
            HashMap<Mercenary, Integer> currControlled = map.getMindControlled();
            // add the enemy to the mind controlled map.
            currControlled.put(this, player.getSceptreDuration());
        }
        allied = true;
    }

    public void setEnemy() {
        allied = false;
        ally = null;
        stuckToPlayer = false;
    }

    @Override
    public void move(Game game) {
        Boolean prevStuckToPlayer = stuckToPlayer;
        Position nextPos;
        GameMap map = game.getMap();
        if (stuckToPlayer || allied && this.getCardAdjPos().contains(ally.getPosition())) {
            // Follow ally if already stuck to player or is now cardinally adajcent
            stuckToPlayer = true;
            nextPos = ally.getPreviousDistinctPosition();
            map.moveTo(this, nextPos);
        } else {
            // Move to ally
            nextPos = map.dijkstraPathFind(getPosition(), map.getPlayerPosition(), this);
            map.moveTo(this, nextPos);
            if (allied && this.getCardAdjPos().contains(ally.getPosition()))
                stuckToPlayer = true;
        }
        // Free ally from being stuck if now stuck to player.
        if (prevStuckToPlayer != stuckToPlayer) game.freeAlly(this);
    }

    @Override
    public boolean isInteractable(Player player) {
        return !allied && (canBeBribed(player) || player.hasSceptre());
    }

    public boolean isStuckToPlayer() {
        return stuckToPlayer;
    }
    public int getBribeAmount() {
        return bribeAmount;
    }
}
