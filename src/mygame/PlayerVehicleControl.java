/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.VehicleControl;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.util.clone.Cloner;
import com.jme3.util.clone.JmeCloneable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wchen
 */

public class PlayerVehicleControl implements Control, ActionListener {
    private final static float STEERING_MULTIPLIER = 1f;
    private final static float ACCELERATION_MULTIPLIER = 1000;
    private final static float BREAK_MULTIPLIER = 10000.0f;
    
    private final static float MAX_STEERING = 0.5f;
    private final static float MAX_ACCELERATION = 500f;
    private final static float MAX_BREAK = 10000f;

    protected Spatial spatial;
    protected VehicleControl control;
    protected InputManager manager;
    protected float steering = 0;
    protected float acceleration = 0;
    protected float breakPower = 0;
    protected boolean keyInputed = false;
    protected boolean joyInputed = false;
    
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private boolean but1;
    private boolean enabled = true;
    

    PlayerVehicleControl(InputManager manager){
        this.manager = manager;
        setButUpKeys();
    }

    private void setButUpKeys(){
        manager.addMapping("ButLeft", new KeyTrigger(KeyInput.KEY_A));
        manager.addMapping("ButRight", new KeyTrigger(KeyInput.KEY_D));
        manager.addMapping("ButUp", new KeyTrigger(KeyInput.KEY_W));
        manager.addMapping("ButDown", new KeyTrigger(KeyInput.KEY_S));
        manager.addMapping("But1", new KeyTrigger(KeyInput.KEY_SPACE));
        manager.addListener(this, "ButLeft", "ButRight", "ButUp", "ButDown", "But1");
    }
    
    private float rest2Zero(float in, float amt){
        if (in > amt)
            return in - amt;
        else if (in < -amt)
            return in + amt;
        else
            return 0;
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        PlayerVehicleControl ctl = new PlayerVehicleControl(manager);
        ctl.setSpatial(spatial);
        return ctl;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
        this.control = spatial.getControl(VehicleControl.class);
        if(control == null)
            Logger.getAnonymousLogger().log(Level.SEVERE, "Couldn't find VehicleControl. Make sure the VehicleControl is added before");
    }

    @Override
    public void update(float tpf) {
        if(!enabled)
            return;
        
        if (left) {
            steering += STEERING_MULTIPLIER * tpf;
        }
        if (right) {
            steering -= STEERING_MULTIPLIER * tpf;
        }
        if (!left && !right) {
            steering = rest2Zero(steering, STEERING_MULTIPLIER * tpf);
        }

        if (up) {
            acceleration += ACCELERATION_MULTIPLIER * tpf;
        }
        if (down) {
            acceleration -= ACCELERATION_MULTIPLIER * tpf;
        }
        if (!up && !down) {
            acceleration = rest2Zero(acceleration, ACCELERATION_MULTIPLIER * tpf);
        }

        if (but1) {
            breakPower += BREAK_MULTIPLIER * tpf;
        } else {
            breakPower = rest2Zero(breakPower, BREAK_MULTIPLIER * tpf);
        }

        steering = FastMath.clamp(steering, -MAX_STEERING, MAX_STEERING);
        acceleration = FastMath.clamp(acceleration, -MAX_ACCELERATION, MAX_ACCELERATION);
        breakPower = FastMath.clamp(breakPower, 0, MAX_BREAK);
        
        control.steer(steering);
        control.accelerate(acceleration);
        control.brake(breakPower);
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
        
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed){
            switch(name){
                case "ButLeft":
                    left = true;
                    break;
                case "ButRight":
                    right = true;
                    break;
                case "ButUp":
                    up = true;
                    break;
                case "ButDown":
                    down = true;
                    break;
                case "But1":
                    but1 = true;
                    break;
            }
        } else {
            switch(name){
                case "ButLeft":
                    left = false;
                    break;
                case "ButRight":
                    right = false;
                    break;
                case "ButUp":
                    up = false;
                    break;
                case "ButDown":
                    down = false;
                    break;
                case "But1":
                    but1 = false;
                    break;
            }
        }
    }
    
    public void setEnabled(boolean enable){
        enabled = enable;
    }
}