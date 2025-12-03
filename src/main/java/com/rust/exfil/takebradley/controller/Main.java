package com.rust.exfil.takebradley.controller;

import com.rust.exfil.takebradley.model.entity.EntityFactory;
import com.rust.exfil.takebradley.model.entity.EntityType;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.inventory.Inventory;
import com.rust.exfil.takebradley.model.inventory.Stash;
import com.rust.exfil.takebradley.systems.serialization.StashDeserializer;
import com.rust.exfil.takebradley.systems.serialization.StashSerializer;
import com.rust.exfil.takebradley.view.GameRenderer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    
    private GameController gameController;
    private StartScreenController startScreenController;
    private GameRenderer gameRenderer;
    private InputHandler inputHandler;
    
    // Player stash and loadout
    private Player player;
    private Stash stash;
    private Inventory loadout;
    
    // Canvas
    private Canvas canvas;
    
    @Override
    public void start(Stage primaryStage) {
        // create canvas for rendering
        canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // initialize player
        player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Player", 0, 0);
        
        // load player stash
        stash = StashDeserializer.deserialize("saves/player_stash.json", player);
        player.setStash(stash);
        
        loadout = player.getInventory();
        
        // initialize start screen controller
        startScreenController = new StartScreenController(stash, loadout, this::startRaid);
        startScreenController.initialize(canvas);
        
        // create scene
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // attach start screen input handlers to scene
        scene.setOnKeyPressed(startScreenController.getInputHandler()::handleKeyPressed);
        scene.setOnKeyReleased(startScreenController.getInputHandler()::handleKeyReleased);
        
        // set up stage
        primaryStage.setTitle("Take Bradley - Extraction Shooter");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        // start the program to start screen
        startScreenController.start();
    }
     
    private void startRaid() {
        // stop start screen controller
        startScreenController.stop();
        
        // serialize stash before starting raid
        String stashFilePath = "saves/player_stash.json";
        boolean saved = StashSerializer.serialize(stash, stashFilePath);
        
        if (saved) {
            System.out.println("Stash saved to " + stashFilePath);
        } else {
            System.err.println("Failed to save stash before raid");
        }
        
        // initialize game with player loadout
        gameController = GameInitializer.initializeGame();
        
        // Get game player and set the persistent stash on them
        Player gamePlayer = gameController.getGameWorld().getPlayer();
        gamePlayer.setStash(stash); // Use the same stash reference
        
        // Pass stash reference to game controller for serialization
        gameController.setPlayerStash(stash);
        
        // transfer loadout items to the game world player inventory
        startScreenController.transferLoadoutToPlayer(gamePlayer);
        
        // initialize game renderer
        gameRenderer = new GameRenderer();
        gameRenderer.initialize(canvas);
        
        // set up raid input handler
        inputHandler = new InputHandler(
            gamePlayer,
            gameController.getGameWorld(),
            gameController.getExfilController()
        );
        inputHandler.setLootUIRenderer(gameRenderer.getLootUIRenderer());
        inputHandler.setGameRenderer(gameRenderer);
        inputHandler.setOnReturnToStart(this::returnToStartScreen);
        
        // switch input handlers to raid handler
        canvas.getScene().setOnKeyPressed(inputHandler::handleKeyPressed);
        canvas.getScene().setOnKeyReleased(inputHandler::handleKeyReleased);
        
        gameController.setGameRenderer(gameRenderer);
        gameController.setInputHandler(inputHandler);
        gameRenderer.setInputHandler(inputHandler);
        gameController.startRaid();
    }
    
    private void returnToStartScreen() {  
        // stop game controller
        if (gameController != null) {
            gameController.endRaid();
        }
        
        // reload stash from disk to include extracted items
        stash = StashDeserializer.deserialize("saves/player_stash.json", player);
        player.setStash(stash);
        
        // reset loadout
        loadout.removeAllItems();
        
        // recreate start screen controller with fresh stash and loadout
        startScreenController = new StartScreenController(stash, loadout, this::startRaid);
        startScreenController.initialize(canvas);
        
        // switch input handlers to start handler
        canvas.getScene().setOnKeyPressed(startScreenController.getInputHandler()::handleKeyPressed);
        canvas.getScene().setOnKeyReleased(startScreenController.getInputHandler()::handleKeyReleased);
        
        startScreenController.start();
    }
    
    @Override
    public void stop() {
        // clean up on app close
        if (startScreenController != null) {
            startScreenController.stop();
        }
        if (gameController != null) {
            gameController.endRaid();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
