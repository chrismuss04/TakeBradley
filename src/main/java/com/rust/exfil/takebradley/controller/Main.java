package com.rust.exfil.takebradley.controller;

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
    private GameRenderer gameRenderer;
    private InputHandler inputHandler;
    
    @Override
    public void start(Stage primaryStage) {
        // initialize game
        gameController = GameInitializer.initializeGame();
        
        // create canvas for rendering
        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // initialize game renderer
        gameRenderer = new GameRenderer();
        gameRenderer.initialize(canvas);
        
        // set up input handler
        inputHandler = new InputHandler(gameController.getGameWorld().getPlayer());
        
        // create scene
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // attach input handlers to scene
        scene.setOnKeyPressed(inputHandler::handleKeyPressed);
        scene.setOnKeyReleased(inputHandler::handleKeyReleased);
        
        // set up stage
        primaryStage.setTitle("Take Bradley - Extraction Shooter");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        // pass renderer and input handler to game controller and start the game
        gameController.setGameRenderer(gameRenderer);
        gameController.setInputHandler(inputHandler);
        gameController.startRaid();
    }
    
    @Override
    public void stop() {
        // clean up on app close
        if (gameController != null) {
            gameController.endRaid();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
