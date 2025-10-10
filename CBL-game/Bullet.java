import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

class Bullet implements GameObject {
    double damage;
    Vec2 velocity;
    Vec2 position;
    static final double RADIUS = 0.2;

    Rectangle lastDraw;

    Circle collider = new Circle(new Vec2(0, 0), RADIUS);

    Bullet(double damage, Vec2 position, Vec2 velocity) {
        // System.out.println("a wild bullet has appeared");
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
        // System.out.println("draw bullet");

        var rt = GameRuntime.rt;
        final Vec2 CORNER_OFFSET = new Vec2(RADIUS * 0.5, RADIUS * 0.5);

        if (lastDraw != null) {

            g.clearRect(lastDraw.x, lastDraw.y, lastDraw.width, lastDraw.height);
        }

        Vec2 startPos = rt.map_space_to_screen(position.sub(CORNER_OFFSET));
        Vec2 endPos = rt.map_space_to_screen(position.add(CORNER_OFFSET));
        g.setColor(Color.cyan);
        lastDraw = new Rectangle(
                (int) startPos.x,
                (int) startPos.y,
                (int) endPos.x - (int) startPos.x,
                (int) endPos.y - (int) startPos.y);

        g.fillOval(lastDraw.x, lastDraw.y, lastDraw.width, lastDraw.height);

    }

    @Override
    public void update() {
        // System.out.println("update bullet");
        position = position.add(velocity.mul(GameRuntime.rt.deltaTime));
        GameRuntime.rt.collisionLayers.get("Enemies").collide_with(this.collider);
    }

    @Override
    public void onCollide(GameObject oth) {
        if (oth instanceof Enemy) {
            ((Enemy) oth).damage(damage);
        }
        GameRuntime.rt.remove(this);
    }

    @Override
    public void onDestroy() {

        if (lastDraw != null) {
            var bs = GameRuntime.rt.canvas.getBufferStrategy();
            var g = bs.getDrawGraphics();
            g.clearRect(lastDraw.x, lastDraw.y, lastDraw.width, lastDraw.height);
            g.dispose();
            bs.show();
        }

        System.out.println("bullet destroyed");
    }
}
