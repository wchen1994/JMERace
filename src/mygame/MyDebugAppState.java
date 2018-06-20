/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

/**
 *
 * @author wchen
 */
public class MyDebugAppState extends DebugKeysAppState implements ActionListener {
    
    private InputManager inputManager;
    private BulletAppState bulletAppState;
    private FollowCamAppState followCamAppState;
    private FlyByCamera flyCam;
    private boolean isCameraFollow = false;
    private boolean isColliderShown = true;
    private Main app;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        inputManager = app.getInputManager();
        bulletAppState = app.getStateManager().getState(BulletAppState.class);
        followCamAppState = app.getStateManager().getState(FollowCamAppState.class);
        flyCam = ((FlyCamAppState)(app.getStateManager().getState(FlyCamAppState.class))).getCamera();
        
        initKeys();
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        if (inputManager.hasMapping("DBBut1"))
            inputManager.deleteMapping("DBBut1");
        if (inputManager.hasMapping("DBBut2"))
            inputManager.deleteMapping("DBBut2");
        if (inputManager.hasMapping("DBBut3"))
            inputManager.deleteMapping("DBBut3");
        if (inputManager.hasMapping("DBBut4"))
            inputManager.deleteMapping("DBBut4");
        if (inputManager.hasMapping("DBBut5"))
            inputManager.deleteMapping("DBBut5");
        if (inputManager.hasMapping("DBBut6"))
            inputManager.deleteMapping("DBBut6");
        if (inputManager.hasMapping("DBBut7"))
            inputManager.deleteMapping("DBBut7");
        if (inputManager.hasMapping("DBBut8"))
            inputManager.deleteMapping("DBBut8");
        if (inputManager.hasMapping("DBBut9"))
            inputManager.deleteMapping("DBBut9");
        if (inputManager.hasMapping("DBBut0"))
            inputManager.deleteMapping("DBBut0");
        
        inputManager.removeListener(this);
    }   
    
    private void initKeys(){
        inputManager.addMapping("DBBut1", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("DBBut2", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("DBBut3", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("DBBut4", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("DBBut5", new KeyTrigger(KeyInput.KEY_5));
        inputManager.addMapping("DBBut6", new KeyTrigger(KeyInput.KEY_6));
        inputManager.addMapping("DBBut7", new KeyTrigger(KeyInput.KEY_7));
        inputManager.addMapping("DBBut8", new KeyTrigger(KeyInput.KEY_8));
        inputManager.addMapping("DBBut9", new KeyTrigger(KeyInput.KEY_9));
        inputManager.addMapping("DBBut0", new KeyTrigger(KeyInput.KEY_0));
        
        inputManager.addListener(this, 
                "DBBut1",
                "DBBut2",
                "DBBut3",
                "DBBut4",
                "DBBut5",
                "DBBut6",
                "DBBut7",
                "DBBut8",
                "DBBut9",
                "DBBut0");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        switch(name){
            case "DBBut1":
                if (!isPressed) {
                    isCameraFollow = !isCameraFollow;
                    flyCam.setEnabled(!isCameraFollow);
                    followCamAppState.setEnable(isCameraFollow);
                    if (!isCameraFollow) {
                        inputManager.setCursorVisible(false);
                    }
                }
                break;
            case "DBBut2":
                if (!isPressed) {
                    isColliderShown = !isColliderShown;
                    bulletAppState.setDebugEnabled(isColliderShown);
                }
                break;
            case "DBBut3":
                break;
            case "DBBut4":
                break;
            case "DBBut5":
                break;
            case "DBBut6":
                break;
            case "DBBut7":
                break;
            case "DBBut8":
                break;
            case "DBBut9":
                break;
            case "DBBut0":
                if (!isPressed) {
                    if (inputManager.isCursorVisible()) {
                        inputManager.setCursorVisible(false);
                    } else {
                        inputManager.setCursorVisible(true);
                    }
                }
                break;
        }
    }

}
