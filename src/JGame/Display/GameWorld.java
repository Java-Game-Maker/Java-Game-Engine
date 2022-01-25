package JGame.Display;

import JGame.Main;
import JGame.Msc.Input.Input;
import JGame.Msc.ObjectHandler;
import JGame.Msc.Vector2;
import JGame.Objects.Components.*;
import JGame.Objects.Components.Collision.CircleCollider;
import JGame.Objects.Components.Collision.Collider;
import JGame.Objects.Components.Collision.SquareCollider;
import JGame.Objects.Components.Component;
import JGame.Objects.Components.Visual.Shape_s;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class GameWorld extends JPanel {

    public static LinkedList<GameObject> newObjects = new LinkedList<>();
    public static LinkedList<GameObject> delObjects = new LinkedList<>();

    private static ArrayList<JComponent> jComponents = new ArrayList<>();
    Shape_s s = new Shape_s();
    public GameWorld() {

       // s.scale(40,40);
        s.setPosition(new Vector2(300,300));

        for(JComponent c : jComponents) {
            add(c);
        }
        setBackground(Color.GREEN);
        MouseAdapter mouseAdapter =  new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Input.mouseButtonDown(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                Input.mouseButtonUp(e);
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                Input.setMousePosition(new Vector2((float) e.getPoint().getX(), (float) e.getPoint().getY()));
            }
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                Input.setMousePosition(new Vector2((float) e.getPoint().getX(), (float) e.getPoint().getY()));
            }
        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                Input.keyDown(e);
            }
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                Input.keyUp(e);
            }
        });
    }
    private void checkCollisions() {
        for(GameObject ob1 : ObjectHandler.getObjects()) {
            for(GameObject ob2 : ObjectHandler.getObjects()) {
                if(ob1!=(ob2)) {
                    for(Collider c1 : ob1.getComponents(new SquareCollider())) {
                        for(Collider c2 : ob2.getComponents(new SquareCollider())) {
                            c1.collided(c2);
                        }
                    }
                }
            }
        }
    }
    /**Check if a Jcomponent has been removed in the Jcomponents list and if so removes it in the panel**/
    public void UpdateSwingComponents() {
        for(java.awt.Component c : getComponents())
        {
            if(!jComponents.contains(c))
            {
                remove(c);
            }
        }
    }
    public void Update() {
        UpdateSwingComponents();

        repaint();
        Toolkit.getDefaultToolkit().sync();
        long start = System.nanoTime();
        long end = System.nanoTime();
       // System.out.println((end-start)/100000);
    }
    float r = 0;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Toolkit.getDefaultToolkit().sync();

        g.drawPolygon(s.getPolygon());
        if (r>=6000){
            r=0;
           // s.rotate(45);
        }
        r++;
            //s.position.add(1);
       // g.drawPolygon(Component.square);
        //g.drawRect((int) Component.square.center.getX(), (int) Component.square.center.getY(),2,2);
        if(false)
        {

            for (GameObject ob : ObjectHandler.getObjects()) {
                if (ob.Display() != null) {
                    System.out.println("img");
                    g.drawImage((Image) ob.Display(), (int) ob.getSpritePosition().getX(), (int) ob.getSpritePosition().getY(), null);
                } else {
                    System.out.println(ob.getShape().getX(0));
                    g.fillPolygon(ob.getShape());
                }

            }
            DrawColliders(g);
        }
    }
    private void DrawColliders(Graphics g)
    {

        Iterator<GameObject> iterator = ObjectHandler.getObjects().iterator();

        while (iterator.hasNext()){
            GameObject ob = iterator.next();
            class Point{
                float x;
                float y;
            }


            for(Collider c : ob.getComponents(new SquareCollider()))
            {

                   g.setColor(Color.GREEN);

                if(c instanceof CircleCollider &&c.isVisible())
                {
                    g.drawOval((int) (c.getPosition().getX()-(c.getScale().getX()/2)), (int) (c.getPosition().getY()-(c.getScale().getY()/2)), (int) c.getScale().getX(), (int) c.getScale().getY());
                }
                else if(c instanceof SquareCollider &&c.isVisible())
                {
                    g.drawRect((int) (c.getPosition().getX()-(c.getScale().getX()/2)), (int) (c.getPosition().getY()-(c.getScale().getY()/2)), (int) c.getScale().getX(), (int) c.getScale().getY());
                }
                g.setColor(Color.black);
            }
        }
    }

}
