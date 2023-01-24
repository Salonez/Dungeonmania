package dungeonmania;

import dungeonmania.mvp.TestUtils;
import java.util.List;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.response.models.EntityResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssassinTest {
    @Test
    @Tag("21-1")
    @DisplayName("Test failed bribe")
    public void testAssassinFailedBribe() {
        // . . . . . .
        // W W W W W W
        // W P T . A W
        // W W W W W W
        // . . . . . .

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assTest_failedBribe", "c_assTest_failedBribe");

        String assId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // failed bribe
        res = assertDoesNotThrow(() -> dmc.interact(assId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        List<EntityResponse> entities1 = res.getEntities();
        assertTrue(TestUtils.countEntityOfType(entities1, "player") == 0);
        assertTrue(TestUtils.countEntityOfType(entities1, "assassin") == 1);
    }

    @Test
    @Tag("21-2")
    @DisplayName("Test successful bribe")
    public void testAssassinSuccessfulBribe() {
        // . . . . . .
        // W W W W W W
        // W P T . A W
        // W W W W W W
        // . . . . . .

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assTest_failedBribe", "c_assTest_successfulBribe");

        String assId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // Successful bribe
        res = assertDoesNotThrow(() -> dmc.interact(assId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        List<EntityResponse> entities1 = res.getEntities();
        assertTrue(TestUtils.countEntityOfType(entities1, "player") == 1);
        assertTrue(TestUtils.countEntityOfType(entities1, "assassin") == 1);
    }
}
