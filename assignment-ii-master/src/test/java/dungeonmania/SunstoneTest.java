package dungeonmania;

import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SunstoneTest {
    @Test
    @Tag("2-2")
    @DisplayName("Test player can pickup sunstone")
    public void pickUpSunstone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunstoneOnLeft_sunstone", "c_sunstoneOnLeft_sunstone");

        assertEquals(1, TestUtils.getEntities(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // pick up sunstone
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getEntities(res, "sun_stone").size());

    }

    @Test
    @Tag("2-22")
    @DisplayName("Test player can pickup sunstone and then open a door")
    public void pickUpSunstoneThenOpenDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStone_openDoor", "c_sunstoneOnLeft_sunstone");

        assertEquals(1, TestUtils.getEntities(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // pick up sunstone
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getEntities(res, "sun_stone").size());

        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();

        res = dmc.tick(Direction.RIGHT);

        // Stopping at the door becasue it cant be opened.
        // needs the to work like a key.
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }
}
