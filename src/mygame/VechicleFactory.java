/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wchen
 */
public class VechicleFactory {
    public static Node createSimpleVehicle(Application app){
        Material matBody = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matBody.getAdditionalRenderState().setWireframe(true);
        matBody.setColor("Color", ColorRGBA.Blue);
        
        Material matWheel = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matWheel.getAdditionalRenderState().setWireframe(true);
        matWheel.setColor("Color", ColorRGBA.Green);
        
        final float mass = 400;
        final float chasisXExt = 1.3f;
        final float chasisYExt = 0.5f;
        final float chasisZExt = 2.4f;
        final float chasisYOff = 0.8f;
        Box boxMesh = new Box(chasisXExt, chasisYExt, chasisZExt);
        BoxCollisionShape boxShape = new BoxCollisionShape(new Vector3f(chasisXExt, chasisYExt, chasisZExt));
        Geometry chasis = new Geometry("Chasis", boxMesh);
        chasis.setMaterial(matBody);
        chasis.setLocalTranslation(0, chasisYOff, 0);
        
        final Vector3f wheelDirection = Vector3f.UNIT_Y.negate();
        final Vector3f wheelAxle = Vector3f.UNIT_X.negate();
        final float radius = 0.5f;
        final float restLength = 0.3f;
        final float yOff = 0.8f;
        final float xOff = 1f;
        final float zOff = 2f;
        Cylinder wheelMesh = new Cylinder(16, 16, radius, radius * 0.6f, true);
        
        Geometry wheelFL = new Geometry("WheelFL", wheelMesh);
        wheelFL.setMaterial(matWheel);
        wheelFL.rotate(0, FastMath.HALF_PI, 0);
        Node nWheelFL = new Node("NodeWheelFL");
        nWheelFL.attachChild(wheelFL);
        
        Geometry wheelFR = new Geometry("WheelFR", wheelMesh);
        wheelFR.setMaterial(matWheel);
        wheelFR.rotate(0, FastMath.HALF_PI, 0);
        Node nWheelFR = new Node("NodeWheelFR");
        nWheelFR.attachChild(wheelFR);
        
        Geometry wheelBL = new Geometry("WheelBL", wheelMesh);
        wheelBL.setMaterial(matWheel);
        wheelBL.rotate(0, FastMath.HALF_PI, 0);
        Node nWheelBL = new Node("NodeWheelBL");
        nWheelBL.attachChild(wheelBL);
        
        Geometry wheelBR = new Geometry("WheelBR", wheelMesh);
        wheelBR.setMaterial(matWheel);
        wheelBR.rotate(0, FastMath.HALF_PI, 0);
        Node nWheelBR = new Node("NodeWheelBR");
        nWheelBR.attachChild(wheelBR);
        
        CompoundCollisionShape compShape = new CompoundCollisionShape();
        compShape.addChildShape(boxShape, new Vector3f(0, chasisYOff, 0));
        
        VehicleControl ctlVehicle = new VehicleControl(compShape, mass);
        float stiffness = 60.0f;//200=f1 car
        float compValue = .3f; //(should be lower than damp)
        float dampValue = .4f; // was 0.4
        ctlVehicle.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
        ctlVehicle.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
        ctlVehicle.setSuspensionStiffness(stiffness);
        ctlVehicle.setMaxSuspensionForce(10000.0f);
        ctlVehicle.addWheel(nWheelFL, new Vector3f(-xOff, yOff,  zOff),
                wheelDirection, wheelAxle, restLength, radius, true);
        ctlVehicle.addWheel(nWheelFR, new Vector3f( xOff, yOff,  zOff),
                wheelDirection, wheelAxle, restLength, radius, true);
        ctlVehicle.addWheel(nWheelBL, new Vector3f(-xOff, yOff, -zOff),
                wheelDirection, wheelAxle, restLength, radius, false);
        ctlVehicle.addWheel(nWheelBR, new Vector3f( xOff, yOff, -zOff),
                wheelDirection, wheelAxle, restLength, radius, false);
        
        Node vehicle = new Node("SimpleVehicle");
        vehicle.addControl(ctlVehicle);
        vehicle.attachChild(chasis);
        vehicle.attachChild(nWheelFL);
        vehicle.attachChild(nWheelFR);
        vehicle.attachChild(nWheelBL);
        vehicle.attachChild(nWheelBR);
        vehicle.addControl(ctlVehicle);
        
        return vehicle;
    }
    
    private static Geometry findGeom(Spatial spatial, String name) {
        if (spatial instanceof Node) {
            Node node = (Node) spatial;
            for (int i = 0; i < node.getQuantity(); i++) {
                Spatial child = node.getChild(i);
                Geometry result = findGeom(child, name);
                if (result != null) {
                    return result;
                }
            }
        } else if (spatial instanceof Geometry) {
            Logger.getAnonymousLogger().log(Level.FINE, String.format("Geom found: %s", spatial.getName()));
            if (spatial.getName().startsWith(name)) {
                return (Geometry) spatial;
            }
        }
        return null;
    }
    
    public static Spatial createMuscleCar(Application app){
        float stiffness = 120.0f;
        float compValue = 0.2f; 
        float dampValue = 0.3f;
        final float mass = 400;

        Spatial vehicle = app.getAssetManager().loadModel("Models/MuscleCar/MuscleCar.j3o");
        vehicle.setShadowMode(RenderQueue.ShadowMode.Cast);
        
        Geometry chasis = findGeom(vehicle, "Body");
        BoundingBox box;
        Logger.getAnonymousLogger().log(Level.FINE, String.format("Chasis is %s", chasis));
        
        CollisionShape carHull = CollisionShapeFactory.createDynamicMeshShape(chasis);
        VehicleControl ctlVehicle = new VehicleControl(carHull, mass);
        ctlVehicle.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
        ctlVehicle.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
        ctlVehicle.setSuspensionStiffness(stiffness);
        ctlVehicle.setMaxSuspensionForce(10000);
        vehicle.addControl(ctlVehicle);
        
        Geometry wheel_fr = findGeom(vehicle, "WheelFR");
        wheel_fr.rotate(0, FastMath.PI, 0);
        wheel_fr.center();
        box = (BoundingBox) wheel_fr.getModelBound();
        System.out.println(box.getCenter());
        final float restLength = 0.5f;
        final float wheelRadius = box.getYExtent();
        final Vector3f wheelDirection = new Vector3f(0, -1, 0);
        final Vector3f wheelAxle = new Vector3f(-1, 0, 0);
        final Vector3f vec3Off = Vector3f.UNIT_Y.mult(restLength);
        ctlVehicle.addWheel(wheel_fr.getParent(), box.getCenter().add(vec3Off),
                wheelDirection, wheelAxle, restLength, wheelRadius, true);

        Geometry wheel_fl = findGeom(vehicle, "WheelFL");
        wheel_fl.rotate(0, FastMath.PI, 0);
        wheel_fl.center();
        box = (BoundingBox) wheel_fl.getModelBound();
        ctlVehicle.addWheel(wheel_fl.getParent(), box.getCenter().add(vec3Off),
                wheelDirection, wheelAxle, restLength, wheelRadius, true);

        Geometry wheel_br = findGeom(vehicle, "WheelBR");
        wheel_br.rotate(0, FastMath.PI, 0);
        wheel_br.center();
        box = (BoundingBox) wheel_br.getModelBound();
        ctlVehicle.addWheel(wheel_br.getParent(), box.getCenter().add(vec3Off),
                wheelDirection, wheelAxle, restLength, wheelRadius, false);

        Geometry wheel_bl = findGeom(vehicle, "WheelBL");
        wheel_bl.rotate(0, FastMath.PI, 0);
        wheel_bl.center();
        box = (BoundingBox) wheel_bl.getModelBound();
        ctlVehicle.addWheel(wheel_bl.getParent(), box.getCenter().add(vec3Off),
                wheelDirection, wheelAxle, restLength, wheelRadius, false);

        ctlVehicle.getWheel(0).setFrictionSlip(1);
        ctlVehicle.getWheel(1).setFrictionSlip(1);
        ctlVehicle.getWheel(2).setFrictionSlip(1);
        ctlVehicle.getWheel(3).setFrictionSlip(1);
        
        return vehicle;
    }
}
