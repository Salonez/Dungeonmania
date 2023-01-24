package dungeonmania.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.Map.Entry;
import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.Portal;
import dungeonmania.entities.SwampTile;
import dungeonmania.entities.Switch;
import dungeonmania.entities.LogicEntities.LightBulb;
import dungeonmania.entities.LogicEntities.LogicalEntity;
import dungeonmania.entities.LogicEntities.SwitchDoor;
import dungeonmania.entities.LogicEntities.Wire;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.enemies.ZombieToastSpawner;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class GameMap implements Serializable {
    private Game game;
    private Map<Position, GraphNode> nodes = new HashMap<>();
    private Player player;
    private HashMap<Entity, Integer> stuckEntities = new HashMap<>();
    private HashMap<Mercenary, Integer> mindControlled = new HashMap<>();

    /**
     * Initialise the game map
     * 1. pair up portals
     */
    public void init() {
        initPairPortals();
        initRegisterMovables();
        initRegisterSpawners();
        initRegisterBombsAndSwitches();
        initStuckEntities();
    }

    private void initStuckEntities() {
        List<SwampTile> swampTiles = getEntities(SwampTile.class);
        List<Entity> entities = getEntities();
        for (SwampTile s : swampTiles) {
            for (Entity e : entities) {
                if (e.getPosition().equals(s.getPosition()) && !s.getId().equals(e.getId())) {
                    System.out.println(e.getPosition());
                    System.out.println(s.getPosition());
                    stuckEntities.put(e, s.getMovementFactor() - 1);
                }
            }
        }
    }

    private void initRegisterBombsAndSwitches() {
        List<Bomb> bombs = getEntities(Bomb.class);
        List<Switch> switchs = getEntities(Switch.class);
        for (Bomb b: bombs) {
            for (Switch s: switchs) {
                if (Position.isAdjacent(b.getPosition(), s.getPosition())) {
                    b.subscribe(s);
                    s.subscribe(b);
                }
            }
        }
    }

    // Pair up portals if there's any
    private void initPairPortals() {
        Map<String, Portal> portalsMap = new HashMap<>();
        nodes.forEach((k, v) -> {
            v.getEntities()
                    .stream()
                    .filter(Portal.class::isInstance)
                    .map(Portal.class::cast)
                    .forEach(portal -> {
                        String color = portal.getColor();
                        if (portalsMap.containsKey(color)) {
                            portal.bind(portalsMap.get(color));
                        } else {
                            portalsMap.put(color, portal);
                        }
                    });
        });
    }

    private void initRegisterMovables() {
        List<Enemy> enemies = getEntities(Enemy.class);
        enemies.forEach(e -> {
            game.register((Runnable & Serializable) () -> e.move(game), Game.AI_MOVEMENT, e.getId());
        });
    }

    private void initRegisterSpawners() {
        List<ZombieToastSpawner> zts = getEntities(ZombieToastSpawner.class);
        zts.forEach(e -> {
            game.register((Runnable & Serializable) () -> e.spawn(game), Game.AI_MOVEMENT, e.getId());
        });
        game.register((Runnable & Serializable) () -> game.getEntityFactory().spawnSpider(game),
        Game.AI_MOVEMENT, "zombieToastSpawner");
    }

    public void moveTo(Entity entity, Position position) {
        if (!canMoveTo(entity, position)) return;

        triggerMovingAwayEvent(entity);
        removeNode(entity);
        entity.setPosition(position);
        addEntity(entity);
        triggerOverlapEvent(entity);
    }

    public void moveTo(Entity entity, Direction direction) {
        if (!canMoveTo(entity, Position.translateBy(entity.getPosition(), direction))) return;
        triggerMovingAwayEvent(entity);
        removeNode(entity);
        entity.translate(direction);
        addEntity(entity);
        triggerOverlapEvent(entity);
    }

    private void triggerMovingAwayEvent(Entity entity) {
        List<Runnable> callbacks = new ArrayList<>();
        getEntities(entity.getPosition()).forEach(e -> {
            if (e != entity)
            callbacks.add((Runnable & Serializable) () -> e.onMovedAway(this, entity));
        });
        callbacks.forEach(callback -> {
            callback.run();
        });
    }

    private void triggerOverlapEvent(Entity entity) {
        List<Runnable> overlapCallbacks = new ArrayList<>();
        getEntities(entity.getPosition()).forEach(e -> {
            if (e != entity) {
                if (e instanceof InventoryItem) {
                    overlapCallbacks.add((Runnable & Serializable) () ->
                    entity.onOverlap(this, e));
                } else {
                    overlapCallbacks.add((Runnable & Serializable) () ->
                    e.onOverlap(this, entity));
                }
            }
        });
        overlapCallbacks.forEach(callback -> {
            callback.run();
        });
    }

    public boolean canMoveTo(Entity entity, Position position) {
        if (stuckEntities.containsKey(entity)) return false;
        return !nodes.containsKey(position) || nodes.get(position).canMoveOnto(this, entity);
    }


    public Position dijkstraPathFind(Position src, Position dest, Entity entity) {
        // if inputs are invalid, don't move
        if (!nodes.containsKey(src) || !nodes.containsKey(dest))
        return src;

        Map<Position, Integer> dist = new HashMap<>();
        Map<Position, Position> prev = new HashMap<>();
        Map<Position, Boolean> visited = new HashMap<>();

        prev.put(src, null);
        dist.put(src, 0);

        PriorityQueue<Position> q = new PriorityQueue<>((x, y) ->
            Integer.compare(dist.getOrDefault(x, Integer.MAX_VALUE), dist.getOrDefault(y, Integer.MAX_VALUE)));
        q.add(src);

        while (!q.isEmpty()) {
            Position curr = q.poll();
            if (curr.equals(dest) || dist.get(curr) > 200) break;
            // check portal
            if (nodes.containsKey(curr) && nodes.get(curr).getEntities().stream().anyMatch(Portal.class::isInstance)) {
                Portal portal = nodes.get(curr).getEntities()
                    .stream()
                    .filter(Portal.class::isInstance).map(Portal.class::cast)
                    .collect(Collectors.toList())
                    .get(0);
                List<Position> teleportDest = portal.getDestPositions(this, entity);
                teleportDest.stream()
                .filter(p -> !visited.containsKey(p))
                .forEach(p -> {
                    dist.put(p, dist.get(curr));
                    prev.put(p, prev.get(curr));
                    q.add(p);
                });
                continue;
            }
            visited.put(curr, true);
            List<Position> neighbours = curr.getCardinallyAdjacentPositions()
            .stream()
            .filter(p -> !visited.containsKey(p))
            .filter(p -> !nodes.containsKey(p) || nodes.get(p).canMoveOnto(this, entity))
            .collect(Collectors.toList());

            neighbours.forEach(n -> {
                int newDist = dist.get(curr) + (nodes.containsKey(n) ? nodes.get(n).getWeight() : 1);
                if (newDist < dist.getOrDefault(n, Integer.MAX_VALUE)) {
                    q.remove(n);
                    dist.put(n, newDist);
                    prev.put(n, curr);
                    q.add(n);
                }
            });
        }
        Position ret = dest;
        if (prev.get(ret) == null || ret.equals(src)) return src;
        while (!prev.get(ret).equals(src)) {
            ret = prev.get(ret);
        }
        return ret;
    }

    public void removeNode(Entity entity) {
        Position p = entity.getPosition();
        if (nodes.containsKey(p)) {
            nodes.get(p).removeEntity(entity);
            if (nodes.get(p).size() == 0) {
                nodes.remove(p);
            }
        }
    }

    public void destroyEntity(Entity entity) {
        removeNode(entity);
        entity.onDestroy(this);
    }

    public void addEntity(Entity entity) {
        addNode(new GraphNode(entity));
    }

    public void addNode(GraphNode node) {
        Position p = node.getPosition();

        if (!nodes.containsKey(p))
        nodes.put(p, node);
        else {
            GraphNode curr = nodes.get(p);
            curr.mergeNode(node);
            nodes.put(p, curr);
        }
    }

    public Entity getEntity(String id) {
        Entity res = null;
        for (Map.Entry<Position, GraphNode> entry : nodes.entrySet()) {
            List<Entity> es = entry.getValue().getEntities()
            .stream()
            .filter(e -> e.getId().equals(id))
            .collect(Collectors.toList());
            if (es != null && es.size() > 0) {
                res = es.get(0);
                break;
            }
        }
        return res;
    }

    public List<Entity> getEntities(Position p) {
        GraphNode node = nodes.get(p);
        return (node != null) ? node.getEntities() : new ArrayList<>();
    }

    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();
        nodes.forEach((k, v) -> entities.addAll(v.getEntities()));
        return entities;
    }

    public <T extends Entity> List<T> getEntities(Class<T> type) {
        return getEntities().stream().filter(type::isInstance).map(type::cast).collect(Collectors.toList());
    }

    public <T extends Entity> long countEntities(Class<T> type) {
        return getEntities().stream().filter(type::isInstance).count();
    }

    public Player getPlayer() {
        return player;
    }

    public Position getPlayerPosition() {
        return player.getPosition();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void beginBattle(Player player, Enemy enemy) {
        game.battle(player, enemy);
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void updateStuckEntities() {
        // Updates the entities stuck on swamp tiles. If it is found that the entity
        // has been on the tile for enough ticks, then they are removed from the
        // the hash map to be allowed to move again.
        Iterator<Entry<Entity, Integer>> iterator = stuckEntities.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<Entity, Integer> entry = iterator.next();
            entry.setValue(entry.getValue() - 1);
            if (entry.getValue() < 0) iterator.remove();
        }
    }

    public void freeAlly(Entity entity) {
        stuckEntities.remove(entity);
    }

    public HashMap<Entity, Integer> getStuckEntities() {
        return stuckEntities;
    }

    public void setStuckEntities(HashMap<Entity, Integer> stuckEntities) {
        this.stuckEntities = stuckEntities;
    }

    public void updateMindControlledEnemies() {
        Iterator<Entry<Mercenary, Integer>> iterator = mindControlled.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<Mercenary, Integer> controlled = iterator.next();
            controlled.setValue(controlled.getValue() - 1);
            // if thier time is up remove them from the list and set them to non allied.
            if (controlled.getValue() < 0) {
                controlled.getKey().setEnemy();
                iterator.remove();
            }
        }
    }

    public void updateArmour() {
        List<Player> player = getEntities(Player.class);
        if (player.size() > 0) {
            Player curr = player.get(0);
            curr.updateArmour(this);
        }
    }

    public HashMap<Mercenary, Integer> getMindControlled() {
        return mindControlled;
    }

    public void setMindControlled(HashMap<Mercenary, Integer> controlled) {
        this.mindControlled = controlled;
    }

    // get all the adjacent entities.
    public List<Entity> getCardAdjEntities(Position p) {
        List<Entity> entities = new ArrayList<>();
        for (Position curr : p.getCardinallyAdjacentPositions()) {
            entities.addAll(getEntities(curr));
        }
        return entities;
    }

    public List<Wire> getCardAdjWires(Position p) {
        List<Wire> wires = new ArrayList<>();
        for (Entity ent : getCardAdjEntities(p)) {
            if (ent instanceof Wire) wires.add((Wire) ent);
        }
        return wires;
    }
    // using this list check if they give power.
    // if they give power swap state.
    // update once per tick.
    // for all entitities if it has power set its state to on and if it doesnt set its state too off.
    public void updateLogics() {
        // check strat.
        List<LogicalEntity> ents = getEntities(LogicalEntity.class);
        for (LogicalEntity curr : ents) {
            if (curr instanceof LightBulb || curr instanceof SwitchDoor) {
                String strat = curr.getStrat();
                switch (strat) {
                    case "AND":
                        ANDstrat.updateLogics(this, curr);
                        break;
                    case "OR":
                        ORstrat.updateLogics(this, curr);
                        break;
                    case "XOR":
                        XORstrat.updateLogics(this, curr);
                        break;
                    case "CO_AND":
                        COANDStrat.updateLogics(this, curr);
                        break;
                    default:
                        break;
                }
            } else {
                // wires just need 1 source of connected power.
                ORstrat.updateLogics(this, curr);
            }
        }
    }

    public void setTicks(Entity entity) {
        if (entity instanceof Wire) {
            ((Wire) entity).setTickStarted(game.getTick());
        }
    }
}
