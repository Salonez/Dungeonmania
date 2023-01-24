package dungeonmania;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlliesMoveTest {

    @Test
    @Tag("20-1")
    @DisplayName("Test mercenary will move to closest square on from top left")
    public void testMercMoveFromTopLeft() {
        // M W . . . .
        // . W . . . .
        // . . . . . .
        // . . P T . .
        // . . . . . .

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercTest_move", "c_mercTest_move");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        for (int i = 0; i < 3; i++) {
            res = dmc.tick(Direction.NONE);
        }
        assertEquals(new Position(3, 2), getMercPos(res));
    }

    @Test
    @Tag("20-3")
    @DisplayName("Test ally already cardinally adjacent")
    public void testAllyAlreadyCardinallyAdjacentPlayerHasMoved() {
        // . . . W W W
        // . P T . M W
        // . . . W W W
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercTest_cardinallyAdjacent", "c_mercTest_cardinallyAdjacent");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        assertEquals(new Position(1, 1), getMercPos(res));
    }

    @Test
    @Tag("20-4")
    @DisplayName("Test ally follow")
    public void testAllyFollow() {
        // . 5 4 W W W
        // 1 T 3 . M W
        // . . . W W W
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercTest_allyFollow", "c_mercTest_allyFollow");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        assertEquals(new Position(2, 1), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getMercPos(res));
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(2, 1), getMercPos(res));
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 0), getMercPos(res));

    }

    @Test
    @Tag("20-5")
    @DisplayName("Test player inbetween previous position and ally")
    public void testPlayerInbetweenPreviousPositionAndAlly() {
        // -10 1 2 3 4
        // . . . . W W W
        // P T . . . M W
        // . . . . W W W
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercTest_playerInbetween", "c_mercTest_allyFollow");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(2, 1), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(0, 1), getMercPos(res));
    }

    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }

}
