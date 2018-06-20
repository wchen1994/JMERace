/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;

/**
 *
 * @author wchen
 */
public class FollowCamAppState extends AbstractAppState {
    private boolean enabled = true;
    private Camera cam;
    private Spatial target = null;
    private float distance = 1;
    private Vector3f offset = Vector3f.ZERO;
    private Vector3f pos = null;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        cam = app.getCamera();
    }
    
    @Override
    public void render(RenderManager rm) {
        if (!enabled || target == null)
            return;
        pos = target.getLocalTranslation().add(offset);
        cam.lookAt(pos, Vector3f.UNIT_Y);
        cam.setLocation(
            pos.add(cam.getDirection().negate().multLocal(distance))
        );
    }
    
    public void setEnable(boolean enable){
        super.setEnabled(enable);
        enabled = enable;
    }
    
    public void setTarget(Spatial target){
        this.target = target;
    }
    
    public void setDistance(float distance){
        this.distance = distance;
    }
    
    public void setOffset(Vector3f offset) {
        this.offset = offset;
    }
}
