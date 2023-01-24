package dungeonmania;

import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.response.models.EntityResponse;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemiesGoalTest {
    @Test
    @Tag("Task2AI")
    @DisplayName("PlayerDestroys2EnemiesAndASpawner")
    public void testPlayerDestroys2EnemiesAndASpawner() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalEnemies_enemiesAndSpawner", "c_goalEnemies_enemiesAndSpawner");
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // move move down and collect the sword.
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));

        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("1-1")
    @DisplayName("BasicEnemyTestWithGivenFiles")
    public void testBasicEnemy() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        DungeonResponse res1 = TestUtils.genericSpiderSequence(dmc, "c_basicGoalsTest_enemy");

        List<EntityResponse> entities1 = res1.getEntities();
        assertTrue(TestUtils.countEntityOfType(entities1, "spider") == 0);

        res1 = dmc.tick(Direction.LEFT);
        res1 = dmc.tick(Direction.LEFT);
        res1 = dmc.tick(Direction.UP);

        assertEquals("", TestUtils.getGoals(res1));
    }
}
