import java.awt.Color;
import java.awt.Graphics;

class Bullet implements GameObject {
    double damage;
    Vec2 velocity;
    Vec2 position;
    static final double RADIUS = 0.2;

    Circle collider = new Circle(new Vec2(0, 0), RADIUS);

    Bullet(double damage, Vec2 position, Vec2 velocity) {

        this.damage = damage;
        this.position = position;
        this.velocity = velocity;

    }

    @Override
    public Vec2 getPos() {
        return position;
    }

    @Override
    public void setup() {
    }

    @Override
    public void draw(Graphics g) {

        final Vec2 CORNER_OFFSET = new Vec2(RADIUS * 0.5, RADIUS * 0.5);

        Vec2 startPos = position.sub(CORNER_OFFSET);
        Vec2 endPos = position.add(CORNER_OFFSET);
        g.setColor(Color.cyan);

        g.fillOval((int) startPos.x, (int) startPos.y, (int) endPos.x - (int) startPos.x,
                (int) endPos.y - (int) startPos.y);

    }

    @Override
    public void update() {
        position = position.add(velocity.mul(GameRuntime.rt.deltaTime));
    }

    @Override
    public void onCollide(GameObject oth) {
        if (oth instanceof Enemy) {
            ((Enemy) oth).damage(damage);
        }
    }
}
