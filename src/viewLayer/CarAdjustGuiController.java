/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewLayer;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wchen
 */
public class CarAdjustGuiController /*extends BaseAppState*/ implements ScreenController {
    
    private Nifty nifty;
    private Element slider;
    
    /**
     * custom methods
     */
    public void startGame(String nextScreen) {
        Logger.getAnonymousLogger().log(Level.INFO, "startGame"+nextScreen);
    }

    public void quitGame() {
//    getApplication().stop();
        Logger.getAnonymousLogger().log(Level.INFO, "quitGame");
    }

    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.slider = screen.findElementById("sliderMass");
        Logger.getAnonymousLogger().log(Level.INFO, slider.toString());
    }

    @Override
    public void onStartScreen() {        
//        control(new ControlBuilder("theButton", "button") {
//            {
//                parameter("label", "OK");
//            }
//        });
        Logger.getAnonymousLogger().log(Level.INFO, "scnStarted");
    }

    @Override
    public void onEndScreen() {
        
        Logger.getAnonymousLogger().log(Level.INFO, "scnEnded");
    }

//    @Override
//    protected void initialize(Application app) {
//        
//        Logger.getAnonymousLogger().log(Level.INFO, "appInited");
//    }
//
//    @Override
//    protected void cleanup(Application app) {
//        
//        Logger.getAnonymousLogger().log(Level.INFO, "appClean");
//    }
//
//    @Override
//    protected void onEnable() {
//        Logger.getAnonymousLogger().log(Level.INFO, "appEnable");
//    }
//
//    @Override
//    protected void onDisable() {
//        Logger.getAnonymousLogger().log(Level.INFO, "appDisable");
//    }
}
