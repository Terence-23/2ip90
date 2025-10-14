import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

class Enemy implements GameObject {
    static double max_health = 50;
    Vec2 pos;
    double health = max_health;
    Rectangle lastDraw;
    final Vec2 size = new Vec2(1, 1);
    Collider col = new Quad(size.mul(-0.5), new Vec2(size.x, 0), new Vec2(0, size.y), this);

    Enemy(Vec2 pos) {
        this.pos = pos;
    }

    @Override
    public void onCollide(GameObject oth) {
        System.out.println("enemy ouch");
    }

    @Override
    public void onDestroy() {
        var layer = GameRuntime.rt.collisionLayers.get("Enemies");
        if (layer != null) {
            layer.remove(this.col);
        }
        if (lastDraw != null) {
            var bs = GameRuntime.rt.canvas.getBufferStrategy();
            var g = bs.getDrawGraphics();

            g.clearRect(lastDraw.x - 1, lastDraw.y - 1, lastDraw.width + 2, lastDraw.height + 2);

            g.dispose();
            bs.show();
        }
    }

    void damage(double damage) {
        health -= damage;
        if (health <= 0) {
            System.out.println("enemy dead");
            GameRuntime.rt.remove(this);
        }
        System.out.println("OUCH!!");
    }

    @Override
    public Vec2 getPos() {
        return pos;
    }

    @Override
    public void setup() {
        var rt = GameRuntime.rt;
        rt.collisionLayers.get("Enemies").add(this.col);
    }

    @Override
    public void draw(Graphics g) {
        var rt = GameRuntime.rt;
        final var CORNER_OFFSET = size.mul(0.5);
        if (lastDraw != null) {
            g.clearRect(lastDraw.x - 1, lastDraw.y - 1, lastDraw.width + 2, lastDraw.height + 2);
        }

        Vec2 startPos = rt.map_space_to_screen(pos.sub(CORNER_OFFSET));
        Vec2 endPos = rt.map_space_to_screen(pos.add(CORNER_OFFSET));
        lastDraw = new Rectangle(
                (int) startPos.x,
                (int) startPos.y,
                (int) endPos.x - (int) startPos.x,
                (int) endPos.y - (int) startPos.y);
        g.setColor(Color.red);
        g.fillRect(lastDraw.x, lastDraw.y, lastDraw.width, lastDraw.height);
        g.setColor(Color.black);
        g.drawRect(lastDraw.x, lastDraw.y, lastDraw.width, lastDraw.height);
    }

    @Override
    public void update() {
    }
}
