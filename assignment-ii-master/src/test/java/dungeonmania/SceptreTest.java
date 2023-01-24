package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class SceptreTest {
    // test crafting with key,
    // test crafting with treasure,
    // test crafting with arrows,
    // test bribe
    // test only cetrain amount of ticks.
    @Test
    @Tag("3-1")
    @DisplayName("Test crafting a sceptre with a key")
    public void testCraftingBasic() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildSceptre", "c_BuildablesTest_BuildSceptre");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // collect all the components.
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "key").size());
        assertEquals(1, TestUtils.getInventory(res, "wood").size());

        res = assertDoesNotThrow(() -> dmc.build("sceptre"));

        // no wood and no key left, but has a sceptre and a sunstone in inventory.
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
    }

    @Test
    @Tag("3-2")
    @DisplayName("Test crafting a sceptre with arrows and treasue")
    public void testCraftingComplicated() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildSceptreComplicated", "c_BuildablesTest_BuildSceptre");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // collect all the components.
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(2, TestUtils.getInventory(res, "arrow").size());

        res = assertDoesNotThrow(() -> dmc.build("sceptre"));

        // inventory is changed
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
    }

    @Test
    @Tag("3-2")
    @DisplayName("Mind control for certain ticks")
    public void testSceptreMindControl() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_Sceptre_mercTest", "c_mercTest_move");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        res = assertDoesNotThrow(() -> dmc.build("sceptre"));

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertDoesNotThrow(() -> dmc.tick(Direction.LEFT));
        // neec to check if its bribed.
    }
}
