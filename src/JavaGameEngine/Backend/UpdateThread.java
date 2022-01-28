package JavaGameEngine.Backend;

import JavaGameEngine.Components.Component;

import java.util.LinkedList;

public class UpdateThread extends Thread{

    private static LinkedList<Component> objects = new LinkedList<>();
    public static LinkedList<Component> newObjects = new LinkedList<>();
    public static LinkedList<Component> delObjects = new LinkedList<>();

    public void setObjects(LinkedList<Component>  objects) {
        UpdateThread.objects = objects;
    }

    public UpdateThread(LinkedList<Component> o) {
        this.setObjects(o);
    }

    private LinkedList<Component>  UpdateObjects()
    {
        for (Component component : objects) {
            component.update();
        }
        return objects;
    }

    public void Update() {
        ComponentHandler.setObjects(UpdateObjects());
        if(UpdateThread.newObjects.size()>0) {
            for (Component o : UpdateThread.newObjects) {
                ComponentHandler.addObject(o);
            }
        }
        if(UpdateThread.delObjects.size()>0) {
            for (Component o : UpdateThread.delObjects) {
                ComponentHandler.removeObject(o);
            }
        }

        newObjects.clear();
        delObjects.clear();
    }

    @Override
    public void run() {
        super.run();
        ComponentHandler.setObjects(UpdateObjects());

    }
}
