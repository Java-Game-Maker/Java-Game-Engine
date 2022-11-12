package com.javagamemaker.javagameengine.components;

import com.javagamemaker.javagameengine.CollisionEvent;
import com.javagamemaker.javagameengine.JavaGameEngine;
import com.javagamemaker.javagameengine.components.shapes.Rect;
import com.javagamemaker.javagameengine.input.Input;
import com.javagamemaker.javagameengine.msc.Vector2;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * This is the first element in the JavaGameEngine
 * it has a lot of functions as update, render positions scale shape
 */
public class Component {

    protected int layer = 10;
    protected String tag = "";
    protected float angle = 0;
    protected boolean visible = true;
    protected Vector2 position = new Vector2(0,0);
    protected Vector2 parentOffset = new Vector2(0,0);
    protected Vector2 scale = new Vector2(100,100);
    protected LinkedList<Vector2> localVertices = new LinkedList<>();
    protected LinkedList<Vector2> vertices = new LinkedList<>();
    protected LinkedList<Component> children = new LinkedList<>();
    protected Component parent;
    protected Vector2 prevPosition = Vector2.zero;
    protected boolean mouseInside = false;

    protected Vector2 lastPosition;

    public Component(LinkedList<Vector2> localVertices){
        this.localVertices = localVertices;
    }

    public Component(){
        localVertices = new Rect(100,100);

        vertices.add(new Vector2(-50,-50)); // top left
        vertices.add(new Vector2(-50,50)); // bottom left
        vertices.add(new Vector2(50,50)); // bottom right
        vertices.add(new Vector2(50,-50)); // top right

    }

    public Component(Vector2 vector2) {
        setPosition(vector2);
    }

    /**
     * This will return the widest and highest from the vertices
     * @return vector2
     */
    public Vector2 getScale() {
        return new Vector2((float) getPolygon().getBounds().getWidth(), (float) getPolygon().getBounds().getHeight());
    }

    /**
     * @return the layer the component is
     */
    public int getLayer() {
        return layer;
    }

    /**
     * @param layer the layer the component should be in
     */
    public void setLayer(int layer) {
        JavaGameEngine.getSelectedScene().layerList.add(this);
        this.layer = layer;
    }

    /**
     * this methods will be called when the game starts and gamewindow has inilized
     *
     */
    public void start(){
        for(Component c : children) c.start();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return true if it is visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible should be visable or not
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Vector2 getPrevPosition() {
        return prevPosition;
    }

    public void setPrevPosition(Vector2 prevPosition) {
        this.prevPosition = prevPosition;
    }

    public Vector2 getParentOffset() {
        return parentOffset;
    }

    /**
     * @param parentOffset the offset a child has to its parent
     */
    public void setParentOffset(Vector2 parentOffset) {
        if(getParent()!=null){
            this.parentOffset = parentOffset;

        }else{
            this.parentOffset = parentOffset;
        }
        for(Component c : getChildren()){
            c.setParentOffset(parentOffset);
        }
        updateVertices();
    }

    /**
     *
     * @param scale new scale
     */
    public void setScale(Vector2 scale) {
        Vector2 d = scale.divide(getScale());
        LinkedList<Vector2> newVertices =new LinkedList<>();
        for(Vector2 vertex : localVertices){
            Vector2 newV = vertex.multiply(d);
            newVertices.add(newV);
        }
        localVertices = newVertices;
        updateVertices();
    }

    public Vector2 getPosition() {
        return position;
    }

    /**
     * if component is a child it will add its parent offset to the position
     * it will update all its childrens positions aswell
     * @param position new position not translate
     */
    public void setPosition(Vector2 position) {
        //this.lastPosition = this.position;

        if(getParent()!=null){
            this.position = position.add(parentOffset).add(rotOffset);

        }else{
            prevPosition = this.position;
            this.position = position;
        }
        for(Component c : getChildren()){
            c.setPosition(position);
        }
        updateVertices();

    }

    public LinkedList<Component> getChildren() {
        return children;
    }

    public void setChildren(LinkedList<Component> children) {
        this.children = children;
    }

    public Component getParent() {
        return parent;
    }

    public void setParent(Component parent) {
        this.parent = parent;
    }

    public LinkedList<Vector2> getVertices() {
        return vertices;
    }

    public LinkedList<Vector2> getLocalVertices() {
        return localVertices;
    }

    public void setLocalVertices(LinkedList<Vector2> localVertices) {
        this.localVertices = localVertices;
    }

    public void setVertices(LinkedList<Vector2> vertices) {
        this.vertices = vertices;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return angle;
    }

    /**
     * update all shape points based on position
     */
    public void updateVertices(){
        LinkedList<Vector2> ver = new LinkedList<>();
        for(Vector2 vertex : localVertices){
            ver.add(vertex.add(position.subtract(rotOffset)));
        }
        vertices = ver;
    }

    /**
     * Use this function to add children. Do not add to the list itself
     * @param component the new children
     */
    public void add(Component component){
        component.setParent(this);
        component.setPosition(getPosition());
        children.add(component);
    }

    /**
     *
     * @return the first component in the tree
     */
    public Component getFirstParent(){
        if(getParent()!=null){
            return getParent().getFirstParent();
        }
        return this;
    }

    /**
     * updates every millisecond
     */
    public void updateMili(){
    }

    /**
     * This method updates all the values to the component
     */
    public void update(){
        Point p = new Point((int) Input.getMousePosition().getX(), (int) Input.getMousePosition().getY());
                /*
                    if mouse is inside, and we have not been we call mouse entered and we say it is entered
                    if mouse is inside, and we have been we don't call mouse entered
                    if mouse is not inside, and we are previously we call mouse left
                 */
        Component prev = JavaGameEngine.getSelectedScene().hasA;
        if(getPolygon().contains(p) && (prev == null || (prev == this) || getLayer() > prev.getLayer() )  ){
            if(isMouseInside()){
                onMouseInside();
            } else{
                onMouseEntered();
            }
            JavaGameEngine.getSelectedScene().hasA = this;

        } else if(isMouseInside()){
            setMouseInside(false);
            onMouseLeft();
            JavaGameEngine.getSelectedScene().hasA = null;
        }

        for(Component child : children){
            child.update();
        }
    }

    /**
     * @return polygon based on components vertices
     */
    public Polygon getPolygon(){
        int[] x = new int[vertices.size()];
        int[] y = new int[vertices.size()];
        int i = 0;

        for(Vector2 point : vertices){
            x[i] = (int) point.getX();
            y[i] = (int) point.getY();
            i++;
        }

        return new Polygon(x,y,vertices.size());
    }

    /**
     *
     * @param type the specified type of the children to be returned
     * @return if type is (new PhysicsBody()) it will return the children that is a physicsBody as LinkedList<Component>
     */
    public LinkedList<Component> getChildren(Component type){
        LinkedList<Component> children = new LinkedList<>();
        for (Component child : this.children){
            if(child.getClass() == type.getClass()){
                children.add(child);
            }
        }
        return children;
    }

    /**
     *
     * @param type the specified type of the children to be returned
     * @return if type is (new PhysicsBody()) it will return the all children and all children's children that is a physicsBody as LinkedList<Component>
     */
    public LinkedList<Component> getAllChildren(Component type){
        LinkedList<Component> children = new LinkedList<>();
        for (Component child : this.children){
            children.addAll(child.getAllChildren(type));
            if(child.getClass() == type.getClass()){
                children.add(child);
            }
        }

        return children;
    }

    /**
     * @param type the specified type of the children to be returned
     * @return if type is (new PhysicsBody()) it will return the first child that is a physicsBody as Component
     */
    public Component getChild(Component type) {

        for (Component child : this.children){
            if(child.getClass() == type.getClass()){
                return child;
            }
        }
        return null;
    }

    public Vector2 getBodyPosition(){
        return new Vector2(getPolygon().getBounds().x, getPolygon().getBounds().y);
    }

    /**
     * class when a collision from a collider is triggered
     * @param collisionEvent information about the collision
     */
    public void onCollisionEnter(CollisionEvent collisionEvent){
        if(getParent()!=null) getParent().onCollisionEnter(collisionEvent);

    }

    /**
     * rotate to specified angle
     * @param angle angle to rotate to
     * @param pivot the povit to rotate around
     */
    public void rotateTo(float angle, Vector2 pivot){
        rotate(angle-this.angle,pivot);
    }

    /**
     * rotates the local vertices to by the angle
     * @param angle the angle to rate the vertices with
     */
    public void rotate(float angle){

        this.angle += angle * JavaGameEngine.deltaTime;

        double radians = Math.toRadians(angle * JavaGameEngine.deltaTime); // turns to radians from angle
        LinkedList<Vector2> vertices1 = new LinkedList<>(); // new vertices
        for (int i = 0; i <localVertices.size(); i++){
            Vector2 vertex = localVertices.get(i);
            // matrix rotation
            float[] matrix = {
                    (float) (vertex.getX() * Math.cos(radians) - vertex.getY() * Math.sin(radians)),
                    (float) (vertex.getX() * Math.sin(radians) + vertex.getY() * Math.cos(radians)) };

            vertices1.add(new Vector2(matrix[0],matrix[1]));
        }
        // set the localvertice to our new rotated matrix


        for(Component child : children){
            child.rotate((float) (angle*JavaGameEngine.deltaTime),child.parentOffset.multiply(-1));
        }

        this.localVertices = vertices1;
    }

    Vector2 rotOffset = Vector2.zero;

    /**
     * rotates the localc vertices to by the angle
     * @param angle the angle to rate the vertice with
     */
    public void rotate(float angle,Vector2 pivot){
        //Vector2 pivot1 = new Vector2(-50,0);
        this.angle += angle;

        double radians = Math.toRadians(angle); // turns to radians from angle
        LinkedList<Vector2> vertices1 = new LinkedList<>(); // new vertices

        for (int i = 0; i <localVertices.size(); i++){
            Vector2 vertex = localVertices.get(i).subtract(pivot);

            float[] matrix = {
                    (float) (vertex.getX() * Math.cos(radians) - vertex.getY() * Math.sin(radians)),
                    (float) (vertex.getX() * Math.sin(radians) + vertex.getY() * Math.cos(radians)) };

            vertices1.add(new Vector2(matrix[0],matrix[1]).add(pivot));
        }
        rotateChildren(angle, pivot);
        this.localVertices = vertices1;
    }

    /**
     * rotate children with an angle and around a poivot
     * @param angle angle to rotate
     * @param pivot rotate around
     */
    public void rotateChildren(float angle,Vector2 pivot){
        for(Component child : children){
            child.rotate(angle,pivot);
        }

    }

    /**
     * @return true if mouse is inside
     */
    public boolean isMouseInside() {
        return mouseInside;
    }

    /**
     * sets if the mouse is inside
     * @param mouseInside the value of mouse is inside
     */
    public void setMouseInside(boolean mouseInside) {
        this.mouseInside = mouseInside;
        for(Component c : children){

        }
    }

    /**
     * Triggers when mouse is inside
     */
    public void onMouseInside(){
    }

    /**
     * triggers when mouse enters component
     */
    public void onMouseEntered(){
        this.mouseInside = true;
    }

    /**
     * triggers when mouse left component
     */
    public void onMouseLeft(){
    }

    /**
     * removes component from parent or from scene
     */
    public void destroy(){
        if(getParent()==null)
            JavaGameEngine.getSelectedScene().destroy(this);
        else{
            getParent().children.remove(this);
        }
    }

    /**
     * Renders the component
     * @param g what graphics to render to
     */
    public void render(Graphics2D g){
        List<Component> list = getChildren();

        for (Component child : list){
            child.render(g);
        }

    }

    /**
     * triggers then a collider is a trigger and  collides with another trigger
     * @param collisionEvent holds information about the collision
     */
    protected void onTriggerEnter(CollisionEvent collisionEvent) {
        if(getParent()!=null) getParent().onTriggerEnter(collisionEvent);
    }

    /**
     * updates every second
     */
    public void updateSecond(){
    }

    /**
     * save the component to a file (serlizeable) linked list
     */
    public void save(){
    }

    public void load(){

    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "{position : "+position.toString()+",\n" +
                "scale:"+getScale().toString()+",\n" +
                "children: ["+getChildren()+"]}";
    }
}