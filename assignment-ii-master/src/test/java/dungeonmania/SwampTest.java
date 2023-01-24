package dungeonmania;

import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class SwampTest {
    @Test
    @Tag("23-1")
    @DisplayName("Test swamp slows entity")
    public void testEntityMoveOntoSwamp() {
        // W W W W W W
        // W M S . P W
        // . W W W W E

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTest_entity", "c_swampTileTest_default");

        res = dmc.tick(Direction.NONE);
        assertEquals(new Position(2, 1), getMercPos(res));

        for (int i = 0; i < 3; i++) {
            res = dmc.tick(Direction.NONE);
            assertEquals(new Position(2, 1), getMercPos(res));
        }
        res = dmc.tick(Direction.NONE);
        assertEquals(new Position(3, 1), getMercPos(res));
    }

    @Test
    @Tag("23-2")
    @DisplayName("Test entity spawn on swamp tile")
    public void testEntitySpawnOnSwampTile() {
        // W W W W W W
        // W M . . P W
        // . W W W W E

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTest_entitySpawnOnTop", "c_swampTileTest_default");

        for (int i = 0; i < 3; i++) {
            res = dmc.tick(Direction.NONE);
            assertEquals(new Position(1, 1), getMercPos(res));
        }
        res = dmc.tick(Direction.NONE);
        assert (getMercPos(res).equals(new Position(2, 1)));

    }

    @Test
    @Tag("23-3")
    @DisplayName("Test player and cardianally adjacent ally move without getting stuck")
    public void testPlayerandAllyOnSlime() {
        // W W W W W W W W
        // M . C P S . . .
        // W W W W W W W E

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTest_playerAndAlly", "c_swampTileTest_default");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // Pick up and bribe mercenary
        res = dmc.tick(Direction.LEFT);
        res = assertDoesNotThrow(() -> dmc.interact(mercId));



        for (int i = 0; i < 4; i++) {
            res = dmc.tick(Direction.RIGHT);
        }

        assertEquals(new Position(6, 1), TestUtils.getEntities(res, "player").get(0).getPosition());
        assertEquals(new Position(5, 1), getMercPos(res));
    }

    @Test
    @Tag("23-4")
    @DisplayName("Test cardianally adjacent ally move gets unstuck")
    public void testCardinalAllyGetsUnstuck() {
        // W W W W W W W W
        // M S C P S . . .
        // W W W W W W W E

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTest_allyUnstuck", "c_swampTileTest_allyUnstuck");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // Check the mercenary is stuck
        res = dmc.tick(Direction.NONE);
        res = dmc.tick(Direction.NONE);
        assertEquals(new Position(1, 1), getMercPos(res));

        // Go bribe
        res = dmc.tick(Direction.LEFT);
        res = assertDoesNotThrow(() -> dmc.interact(mercId));

        // Check he is unstuck
        res = dmc.tick(Direction.RIGHT);

        assertEquals(new Position(3, 1), TestUtils.getEntities(res, "player").get(0).getPosition());
        assertEquals(new Position(2, 1), getMercPos(res));
    }

    @Test
    @Tag("23-5")
    @DisplayName("Test player can grab item from tile and kill mercenary on top")
    public void testGrabItemAndKillOnTop() {
        // M S/I P

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTest_grabItem", "c_swampTileTest_allyUnstuck");

        res = dmc.tick(Direction.NONE);
        res = dmc.tick(Direction.NONE);
        assertTrue(TestUtils.countEntityOfType(res.getEntities(), "mercenary") == 1);

        res = dmc.tick(Direction.LEFT);

        assertEquals(new Position(1, 0), TestUtils.getEntities(res, "player").get(0).getPosition());
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertTrue(TestUtils.countEntityOfType(res.getEntities(), "mercenary") == 0);
    }

    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }

}
