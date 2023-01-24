package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceTest {
    @Test
    @Tag("24-1")
    @DisplayName("Test saving of basic game")
    public void testBasicSave() throws Exception {
        // M . . .
        // P . . .
        // . . . .
        // . . . E
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_basic", "c_swampTileTest_default");
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = assertDoesNotThrow(() -> dmc.saveGame("test_basic_save"));
        assertTrue(TestUtils.getEntities(res, "player").get(0).getPosition().equals(new Position(0, 3)));
        assertTrue(TestUtils.getEntities(res, "mercenary").get(0).getPosition().equals(new Position(0, 2)));
        assertTrue(TestUtils.getEntities(res, "exit").get(0).getPosition().equals(new Position(3, 3)));

        res = dmc.tick(Direction.RIGHT);

        assertTrue(TestUtils.getEntities(res, "player").get(0).getPosition().equals(new Position(1, 3)));
        assertTrue(TestUtils.getEntities(res, "mercenary").get(0).getPosition().equals(new Position(1, 2)));

        res = dmc.loadGame("test_basic_save");
        assertTrue(TestUtils.getEntities(res, "player").get(0).getPosition().equals(new Position(0, 3)));
        assertTrue(TestUtils.getEntities(res, "mercenary").get(0).getPosition().equals(new Position(0, 2)));
        assertTrue(TestUtils.getEntities(res, "exit").get(0).getPosition().equals(new Position(3, 3)));
        res = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.getEntities(res, "player").get(0).getPosition().equals(new Position(1, 3)));
        assertTrue(TestUtils.getEntities(res, "mercenary").get(0).getPosition().equals(new Position(1, 2)));
    }

    @Test
    @Tag("24-2")
    @DisplayName("Test saving of potions")
    public void testPotionsSave() throws InvalidActionException, Exception {
        // E M . P I
        // Potion should have duration 2 tick or possibly 1 tick
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_potionSave", "c_persistenceTest_potionSave");
        res = dmc.tick(Direction.RIGHT);
        // use potion
        res = dmc.tick(TestUtils.getFirstItemId(res, "invincibility_potion"));
        res = dmc.saveGame("test_potion");
        // Potion run out and die
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.NONE);
        assertTrue(TestUtils.countEntityOfType(res.getEntities(), "player") == 0);
        res = dmc.loadGame("test_potion");
        // check potion back on and kill mercenary
        res = dmc.tick(Direction.NONE);
        assertTrue(TestUtils.countEntityOfType(res.getEntities(), "mercenary") == 0);
        res = dmc.loadGame("test_potion");
        // check if potion can wear out again and player die in battle
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.NONE);
        assertTrue(TestUtils.countEntityOfType(res.getEntities(), "player") == 0);
    }

    @Test
    @Tag("24-3")
    @DisplayName("Test saving of game with merc on swamp tiles and staff bribed merc")
    public void testMercOnSwampTilesAndStaffBribedMerc() throws Exception {
        // E M S . W T s P
        // stuck on tile for 2
        // save two before merc attacks
        // then check same thing occurs
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_swampTilesAndStaffBribedMerc",
            "c_persistenceTest_swampTilesAndStaffBribedMerc");
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = dmc.tick(Direction.LEFT);

        assertTrue(TestUtils.getEntities(res, "player").get(0).getPosition().equals(new Position(6, 0)));
        assertTrue(TestUtils.getEntities(res, "mercenary").get(0).getPosition().equals(new Position(2, 0)));
        res = dmc.saveGame("slime_save");
        res = dmc.tick(Direction.LEFT);
        assertTrue(TestUtils.getEntities(res, "player").get(0).getPosition().equals(new Position(5, 0)));
        assertTrue(TestUtils.getEntities(res, "mercenary").get(0).getPosition().equals(new Position(2, 0)));
        res = dmc.tick(Direction.LEFT);
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertTrue(TestUtils.getEntities(res, "player").get(0).getPosition().equals(new Position(4, 0)));
        assertTrue(TestUtils.getEntities(res, "mercenary").get(0).getPosition().equals(new Position(3, 0)));
        // E . S M P . . .
        res = dmc.loadGame("slime_save");
        // E . M . W T P .
        assertTrue(TestUtils.getEntities(res, "player").get(0).getPosition().equals(new Position(6, 0)));
        assertTrue(TestUtils.getEntities(res, "mercenary").get(0).getPosition().equals(new Position(2, 0)));
        res = dmc.tick(Direction.LEFT);
        assertTrue(TestUtils.getEntities(res, "player").get(0).getPosition().equals(new Position(5, 0)));
        assertTrue(TestUtils.getEntities(res, "mercenary").get(0).getPosition().equals(new Position(2, 0)));
        res = dmc.tick(Direction.LEFT);
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertTrue(TestUtils.getEntities(res, "player").get(0).getPosition().equals(new Position(4, 0)));
        assertTrue(TestUtils.getEntities(res, "mercenary").get(0).getPosition().equals(new Position(3, 0)));
        // E . S M P . . .
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        res = dmc.saveGame("sceptre_save");
        res = dmc.tick(Direction.NONE);
        res = dmc.tick(Direction.NONE);
        assertTrue(TestUtils.countEntityOfType(res.getEntities(), "mercenary") == 1);
        res = dmc.tick(Direction.NONE);
        assertTrue(TestUtils.countEntityOfType(res.getEntities(), "mercenary") == 0);
        res = dmc.loadGame("sceptre_save");
        res = dmc.tick(Direction.NONE);
        res = dmc.tick(Direction.NONE);
        assertTrue(TestUtils.countEntityOfType(res.getEntities(), "mercenary") == 1);
        res = dmc.tick(Direction.NONE);
        assertTrue(TestUtils.countEntityOfType(res.getEntities(), "mercenary") == 0);
    }
}
