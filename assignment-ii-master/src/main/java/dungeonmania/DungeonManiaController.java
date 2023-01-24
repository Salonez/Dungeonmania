package dungeonmania;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ResponseBuilder;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class DungeonManiaController {
    private Game game = null;

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        if (!dungeons().contains(dungeonName)) {
            throw new IllegalArgumentException(dungeonName + " is not a dungeon that exists");
        }

        if (!configs().contains(configName)) {
            throw new IllegalArgumentException(configName + " is not a configuration that exists");
        }

        try {
            GameBuilder builder = new GameBuilder();
            game = builder.setConfigName(configName).setDungeonName(dungeonName).buildGame();
            return ResponseBuilder.getDungeonResponse(game);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        return null;
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        return ResponseBuilder.getDungeonResponse(game.tick(itemUsedId));
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        return ResponseBuilder.getDungeonResponse(game.tick(movementDirection));
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        List<String> validBuildables = List.of("bow", "shield", "midnight_armour", "sceptre");
        if (!validBuildables.contains(buildable)) {
            throw new IllegalArgumentException("Only bow, shield, midnight_armour and sceptre can be built");
        }

        return ResponseBuilder.getDungeonResponse(game.build(buildable));
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        return ResponseBuilder.getDungeonResponse(game.interact(entityId));
    }

    /**
     * /game/save
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        try {
            // Save game
            File f = new File("src/main/java/dungeonmania/saved_games", name);
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(game);
            oos.close();
            // Save JSONObject
            JSONObject config = game.getEntityFactory().getConfig();
            String savedConfig = new Gson().toJson(config);
            String configName = "c_" + name;
            File f1 = new File("src/main/java/dungeonmania/saved_configs", configName);
            fos = new FileOutputStream(f1);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(savedConfig);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseBuilder.getDungeonResponse(game);
    }

    /**
     * /game/load
     * @throws ClassNotFoundException
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        try {
            // Load game
            File f = new File("src/main/java/dungeonmania/saved_games", name);
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Game loadedGame = (Game) ois.readObject();
            ois.close();
            game = loadedGame;
            // Load JSONObject for entity factory
            String configName = "c_" + name;
            File f1 = new File("src/main/java/dungeonmania/saved_configs", configName);
            fis = new FileInputStream(f1);
            ois = new ObjectInputStream(fis);
            String stringConfig = (String) ois.readObject();
            JSONObject loadedConfig = new Gson().fromJson(stringConfig, JSONObject.class);
            loadedGame.getEntityFactory().setConfig(loadedConfig);
            ois.close();
            return ResponseBuilder.getDungeonResponse(loadedGame);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        File directoryPath = new File("src/main/java/dungeonmania/saved_games");
        ArrayList<String> savedGames = new ArrayList<String>(Arrays.asList(directoryPath.list()));
        return savedGames;
    }

    /**
     * /game/new/generate
     */
    public DungeonResponse generateDungeon(
            int xStart, int yStart, int xEnd, int yEnd, String configName) throws IllegalArgumentException {
        return null;
    }

    /**
     * /game/rewind
     */
    public DungeonResponse rewind(int ticks) throws IllegalArgumentException {
        return null;
    }

}
