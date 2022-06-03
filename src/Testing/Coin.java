package Testing;

import javagameengine.components.colliders.SquareCollider;
import javagameengine.components.GameObject;
import javagameengine.components.sprites.Sprite;
import javagameengine.msc.Vector2;

import java.awt.*;

public class Coin extends GameObject {

    public Vector2 startPos = Vector2.zero;
    public Coin() {

        Sprite sprite = new Sprite();

        sprite.loadAnimation(new Rectangle[]{new Rectangle(0,3*250,230,250)},"/spritesheet.png");
        sprite.setTimer(20);

        setScale(new Vector2(40,40));
        addChild(sprite);
        SquareCollider b = new SquareCollider();

        b.setTrigger(true);
        b.setVisible(false);
        addChild(b);

        setTag("Coin");
        setRotation(new Vector2(0,-1));
        setPosition(new Vector2(100,420));
        startPos=new Vector2(100,420);
        maxUp = startPos.getY()-10;
        mindown = startPos.getY()+25;
    }
    public Coin(Vector2 pos) {

        Sprite sprite = new Sprite();

        sprite.loadAnimation(new Rectangle[]{new Rectangle(0,3*250,230,250)},"/spritesheet.png");
        sprite.setTimer(20);

        setScale(new Vector2(40,40));
        addChild(sprite);
        SquareCollider b = new SquareCollider();

        b.setTrigger(true);
        b.setVisible(false);
        addChild(b);

        setTag("Coin");
        setRotation(new Vector2(0,-1));
        setPosition(pos);
        startPos=pos;
        maxUp = startPos.getY()-10;
        mindown = startPos.getY()+25;
    }
    public float maxUp;
    public float mindown;

    @Override
    public void update() {
        super.update();
        maxUp = startPos.getY()-10;
        mindown = startPos.getY()+25;

        if(getPosition().getY()<maxUp){
            setRotation(Vector2.down);
        }
        if(getPosition().getY()>mindown){
            setRotation(Vector2.up);
        }
        setPosition(getPosition().add(getRotation().multiply(0.2f)));

    }
}
