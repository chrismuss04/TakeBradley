package com.rust.exfil.takebradley.controller;

import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.inventory.Inventory;
import com.rust.exfil.takebradley.model.inventory.Stash;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.view.StartScreenRenderer;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;

public class StartScreenController {
    private final Stash stash;
    private final Inventory loadout;
    private final StartScreenRenderer renderer;
    private final StartScreenInputHandler inputHandler;
    private AnimationTimer renderLoop;
    private Canvas canvas;
    
    public StartScreenController(Stash stash, Inventory loadout, Runnable onStartRaid) {
        this.stash = stash;
        this.loadout = loadout;
        this.renderer = new StartScreenRenderer();
        this.renderer.reset(stash);
        this.inputHandler = new StartScreenInputHandler(renderer, stash, loadout, onStartRaid);
    }
    
    public void initialize(Canvas canvas) {
        this.canvas = canvas;
    }
    
    public void start() {
        if (renderLoop != null) {
            renderLoop.stop();
        }
                
        renderLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (canvas != null) {
                    renderer.renderStartScreen(
                        canvas.getGraphicsContext2D(),
                        stash,
                        loadout
                    );
                }
            }
        };
        
        renderLoop.start();
    }
    
    public void stop() {
        if (renderLoop != null) {
            renderLoop.stop();
            renderLoop = null;
        }
    }
    
    public StartScreenInputHandler getInputHandler() {
        return inputHandler;
    }
    
    public StartScreenRenderer getRenderer() {
        return renderer;
    }
    
    public void transferLoadoutToPlayer(Player gamePlayer) {
        // remove all items from loadout and add to game player's inventory
        java.util.List<LootItem> loadoutItems = loadout.removeAllItems();
        for (LootItem item : loadoutItems) {
            gamePlayer.getInventory().addItem(item);
        }
        System.out.println("Transferred " + loadoutItems.size() + " items from loadout to raid inventory");
    }
}
