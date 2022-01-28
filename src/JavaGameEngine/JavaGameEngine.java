package JavaGameEngine;
import JavaGameEngine.Backend.ComponentHandler;
import JavaGameEngine.Backend.GameWorld;
import JavaGameEngine.Backend.UpdateThread;
import JavaGameEngine.Components.GameObject;
import JavaGameEngine.msc.Debug;
import JavaGameEngine.msc.Vector2;
import Testing.Player;
import com.sun.org.glassfish.gmbal.GmbalException;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

public class JavaGameEngine {

    public static final int DELAY = 16;
    public static GameWorld GAMEWORLD = new GameWorld();
    static JFrame frame;
    public JavaGameEngine()
    {

    }
    public static void init()
    {
        frame = new JFrame();
        frame.setSize(600,600);
        frame.setTitle("Java Game Engine");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        GAMEWORLD.setBackground(new Color(44, 157, 228));
    }

    /**
     * this it the method to start the game engine
     * do every setup thing before calling start
     * @param frame the frame you want to render in (null for default frame)
     */
    public static void start(JFrame frame)
    {
        if(frame==null)
            frame = JavaGameEngine.frame;

        frame.setVisible(true);
        frame.add(GAMEWORLD);
        GAMEWORLD.setFocusable(true);

        UpdateThread calcThread = new UpdateThread(ComponentHandler.getObjects());
        calcThread.start();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                calcThread.Update();
            }
        }, DELAY,DELAY);
        Timer timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {

                GAMEWORLD.repaint();
                Toolkit.getDefaultToolkit().sync(); // so it does not lag on linux
            }
        }, DELAY,DELAY);

    }
}