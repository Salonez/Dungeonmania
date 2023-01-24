package dungeonmania;

import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class MidnightArmourTests {

    @Test
    @Tag("3-1")
    @DisplayName("Build Armour")
    public void testCraftingArmour() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_buildTest_armour", "c_BuildablesTest_BuildSceptre");

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        // collect all the components.
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));

        // no wood and no key left, but has a sceptre and a sunstone in inventory.
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());
    }

    @Test
    @Tag("3-1")
    @DisplayName("test damage")
    public void testIncreasedAttack() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_armourBuff_zombie", "c_armourBuff_zombie");

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        // collect all the components.
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        List<EntityResponse> entities = res.getEntities();

        // attack bonus is 10, base is 3
        // mercenary has 13 health.
        assertEquals(0, TestUtils.countEntityOfType(entities, "mercenary"));

    }

}
