package dungeonmania;

import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.response.models.EntityResponse;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogicTest {
    @Test
    @Tag("Logic")
    @DisplayName("LightBulb")
    public void testLightBulbFunctionality() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_lightBulb_spawn", "c_goalEnemies_enemiesAndSpawner");

        // check the light bulb is spawned in the map.
        List<EntityResponse> entities = res.getEntities();
        assertTrue(TestUtils.countEntityOfType(entities, "light_bulb_off") == 1);

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);

        List<EntityResponse> entities2 = res.getEntities();
        assertTrue(TestUtils.countEntityOfType(entities2, "light_bulb_on") == 1);

        //make a map with a swich next to the lightbulb and push a boulder on the switch then check for lightbulb on.

    }

    @Test
    @Tag("Logic")
    @DisplayName("Wire")
    public void testWireToLight() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_wire_spawn", "c_goalEnemies_enemiesAndSpawner");

        // test walk over
        // test using to activate.

        List<EntityResponse> entities = res.getEntities();
        assertTrue(TestUtils.countEntityOfType(entities, "wire") == 1);

        res = dmc.tick(Direction.DOWN);
    }

    @Test
    @Tag("Logic")
    @DisplayName("Switch Doors")
    public void testOpenSwitchdoors() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_switchDoor_switch", "c_goalEnemies_enemiesAndSpawner");

        List<EntityResponse> entities = res.getEntities();
        assertTrue(TestUtils.countEntityOfType(entities, "switch_door") == 1);

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        Position check1 = TestUtils.getPlayerPos(res);
        res = dmc.tick(Direction.RIGHT);
        Position check2 = TestUtils.getPlayerPos(res);

        assertEquals(check1, check2);

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        Position check3 = TestUtils.getPlayerPos(res);

        // check walks through door.
        assertNotEquals(check2, check3);
    }
}
