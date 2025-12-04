package com.rust.exfil.takebradley.controller;

import com.rust.exfil.takebradley.model.inventory.Inventory;
import com.rust.exfil.takebradley.model.inventory.Stash;
import com.rust.exfil.takebradley.view.StartScreenRenderer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class StartScreenInputHandler {
    
    private final StartScreenRenderer renderer;
    private final Stash stash;
    private final Inventory loadout;
    private final Runnable onStartRaid;
    
    public StartScreenInputHandler(StartScreenRenderer renderer, Stash stash, 
                                   Inventory loadout, Runnable onStartRaid) {
        this.renderer = renderer;
        this.stash = stash;
        this.loadout = loadout;
        this.onStartRaid = onStartRaid;
    }
    
    
    public void handleKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        
        switch (code) {
            // navigate through stash and loadout
            case W:
            case UP:
                renderer.selectUp(stash);
                break;
                
            case S:
            case DOWN:
                renderer.selectDown(stash);
                break;
                
            case A:
            case LEFT:
                renderer.selectLeft(stash, loadout);
                break;
                
            case D:
            case RIGHT:
                renderer.selectRight(stash, loadout);
                break;
                
            // switch focus between stash and loadout
            case TAB:
                renderer.switchFocus();
                break;
                
            case E:
                renderer.transferSelectedItem(stash, loadout);
                break;
                
            case ENTER:
                // only start raid if button is focused
                if (renderer.isButtonFocused() && onStartRaid != null) {
                    onStartRaid.run();
                }
                break;
                
            default:
                break;
        }
    }
    
    public void handleKeyReleased(KeyEvent event) {
        // no key release handling needed for start screen
    }
}
