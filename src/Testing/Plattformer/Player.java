package Testing.Plattformer;

import JGame.Display.CalcThread;
import JGame.Msc.Input.Input;
import JGame.Msc.Input.Keys;
import JGame.Msc.Vector2;
import JGame.Objects.Components.Collision.SquareCollider;
import JGame.Objects.Components.Component;
import JGame.Objects.Components.Physics.PhysicsBody;
import JGame.Objects.Components.GameObject;

public class Player extends GameObject {

    private float speed =2;
    private float gravity=0;
    private float jump=4;
    private boolean isGrounded = false;
    private boolean jumping=false;

    private float shootTimer = 10;

    public Player(Vector2 vector2) {
        super(vector2);
        setScale(new Vector2(100,100));
        addComponent(new PhysicsBody());
        addComponent(new SquareCollider());
        setShape(Component.square);
        //getShape().setPosition(getPosition());
        //getShape().setScale(new Vector2(50,50));
    }
    private void jump() {

    }
    private void shoot()
    {
        float angle = (float) this.LookAt(Input.getMousePosition());
        //System.out.println(angle);
        long start = System.nanoTime();

        CalcThread.newObjects.add(new Bullet(getPosition().add(Vector2.right.multiply(getScale().getX())),Vector2.right));

        long end = System.nanoTime();
        //System.out.println((end-start)/1000000);

    }

    private float x = 1;
    private float y = 1;
    private void movement()
    {
        if(Input.isKeyDown(Keys.W))
        {
            if(y>=2)
            {
                JGame.Objects.Components.Component.square.setScale(new Vector2(2,2));
                y=0;
            }
            y+=0.01f;
            System.out.println(y);
            // Component.square.scaleTest(new Vector2(x,y));
           //JGame.Objects.Components.Component.square.setPosition(Component.square.getPosition().add(new Vector2(x,0)));

        }
        if(Input.isKeyDown(Keys.D))
        {
            x+=1;
            //Component.square.scaleTest(new Vector2(x,y));
           // JGame.Objects.Components.Component.square.setPosition(new Vector2(300,300));

            setDirection(Vector2.right);
            setDirection(movePosition(getPosition().add(getDirection().multiply(speed))));
        }
        if(Input.isKeyDown(Keys.A))
        {
            setDirection(Vector2.left);
            setDirection(movePosition(getPosition().add(getDirection().multiply(speed))));

        }
        if(Input.isKeyDown(Keys.SPACE))
        {if(shootTimer<0)
        {
            shoot();
            shootTimer=10;

        }
            shootTimer-=1;
            //setDirection(Vector2.up);
        }
        if(Input.isMouseDown(1))
        {
        }
    }
    @Override
    public void Update() {
        super.Update();
        movement();
        //System.out.println("Update player");
    }
}
