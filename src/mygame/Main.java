package mygame;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppState;
import com.jme3.audio.AudioListenerState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ScreenBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import viewLayer.CarAdjustGuiController;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public Main(AppState... initialStates ) {
        super(initialStates);
    }
    
    public static void main(String[] args) {
        Main app = new Main(new StatsAppState(), 
                new FlyCamAppState(), 
                new AudioListenerState(), 
                new FollowCamAppState(),
                new MyDebugAppState()
        );
        app.start();
    }

    private static BulletAppState bulletAppState;
    private Spatial vehicle;
    private Nifty nifty;
    
    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(5);
        viewPort.setBackgroundColor(ColorRGBA.DarkGray);
        rootNode.addLight(new DirectionalLight(new Vector3f(0.3f,-1,0.2f)));
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        
//        Box b = new Box(125, 1, 125);
//        Geometry floor = new Geometry("Box", b);
//        floor.setLocalTranslation(0, -5, 0);
//        floor.setMaterial(mat);
//        floor.addControl(new RigidBodyControl(new MeshCollisionShape(floor.getMesh()), 0));
//        bulletAppState.getPhysicsSpace().add(floor.getControl(RigidBodyControl.class));
//        rootNode.attachChild(floor);
        
        Spatial track = assetManager.loadModel("Models/RaceTrack/RaceTrack.j3o");
        track.scale(4);
        CollisionShape trackShape = CollisionShapeFactory.createMeshShape(track);
        RigidBodyControl trackCtl = new RigidBodyControl(trackShape, 0);
        track.addControl(trackCtl);
        bulletAppState.getPhysicsSpace().add(trackCtl);
        rootNode.attachChild(track);
        
//        vehicle = VechicleFactory.createSimpleVehicle(this);
//        rootNode.attachChild(vehicle);
//        PlayerVehicleControl player = new PlayerVehicleControl(inputManager);
//        vehicle.addControl(player);
//        bulletAppState.getPhysicsSpace().add(vehicle.getControl(VehicleControl.class));
        
        vehicle = VechicleFactory.createMuscleCar(this);
        rootNode.attachChild(vehicle);
        vehicle.addControl(new PlayerVehicleControl(inputManager));
        VehicleControl ctl = vehicle.getControl(VehicleControl.class);
        ctl.setPhysicsLocation(new Vector3f(-10.9f, 4.1f, 5.3f).multLocal(4));
        bulletAppState.getPhysicsSpace().add(ctl);
        
        
//        Spatial vehicle = assetManager.loadModel("Models/MuscleCar/MuscleCar.j3o");
//        CollisionShape shape = CollisionShapeFactory.createDynamicMeshShape(vehicle);
//        RigidBodyControl ctl = new RigidBodyControl(shape, 0);
//        vehicle.addControl(ctl);
//        bulletAppState.getPhysicsSpace().add(ctl);

//        Spatial root = vehicle.clone();
//        root.rotate(FastMath.HALF_PI,0,0);
//        traversalGeom(vehicle, root, 1);
//        rootNode.attachChild(root);

//        traversalGeom(vehicle, vehicle);
//        rootNode.attachChild(vehicle);
        
        FollowCamAppState state = this.getStateManager().getState(FollowCamAppState.class);
        state.setTarget(vehicle);
        state.setEnable(false);
        state.setDistance(4);
        state.setOffset(Vector3f.UNIT_Y);
        
        NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(
                assetManager,
                inputManager,
                audioRenderer,
                viewPort);
        Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE);
        Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.SEVERE);
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/CarAdjustGui.xml", "start", new CarAdjustGuiController());
        guiViewPort.addProcessor(niftyDisplay);
    }
    
    private static void traversalGeom(Spatial spatial, Spatial root, int lv) {
        Logger.getAnonymousLogger().log(Level.INFO, String.format("lv%d is %s", lv, spatial));
        if (spatial instanceof Node) {
            Node node = (Node) spatial;
            for (int i = 0; i < node.getQuantity(); i++) {
                Spatial child = node.getChild(i);
                traversalGeom(child, root, lv+1);
            }
        } else if (spatial instanceof Geometry) {
            CollisionShape shape = CollisionShapeFactory.createDynamicMeshShape(spatial.getParent());
            RigidBodyControl ctl = new RigidBodyControl(shape, 0);
            root.addControl(ctl);
            bulletAppState.getPhysicsSpace().add(ctl);
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        
    }
}
