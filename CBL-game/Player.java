import java.awt.Color;
import java.awt.Graphics;

class Player implements GameObject {

    Vec2 pos;
    static final double SPEED = 2.;
    static final Color COLOR = Color.blue;
    static final double MAX_HEALTH = 100;
    double health;

    @Override
    public Vec2 getPos() {
        return pos;
    }

    @Override
    public void setup() {
        pos = new Vec2(0, 0);
        health = MAX_HEALTH;
    }

    @Override
    public void draw(Graphics g) {
        var rt = GameRuntime.rt;
        var startCorner = rt.map_space_to_screen(pos.sub(new Vec2(0.5, 0.5)));
        var endCorner = rt.map_space_to_screen(pos.add(new Vec2(0.5, 0.5)));

        var size = endCorner.sub(startCorner);

        g.setColor(COLOR);
        g.fillRect((int) startCorner.x, (int) startCorner.y, (int) size.x, (int) size.y);
        g.drawString("HP: %d".formatted((int) health), 10, 20);

    }

    public void damage(double dmg) {
        health -= dmg;
        if (health <= 0) {
            GameRuntime.rt.gameOver();
        }
    }

    @Override
    public void update() {
        GameRuntime rt = GameRuntime.rt;
        if (Double.isNaN(pos.x)) {
            throw new RuntimeException("pos is nan");
        }
        var movementDirection = new Vec2(0, 0);
        // System.out.println("pos is not nan");
        if (Input.isKeyPressed('w')) {
            movementDirection = movementDirection.add(new Vec2(0, -1));
        }
        if (Input.isKeyPressed('s')) {
            movementDirection = movementDirection.add(new Vec2(0, 1));
        }
        if (Input.isKeyPressed('a')) {
            movementDirection = movementDirection.add(new Vec2(-1, 0));
        }
        if (Input.isKeyPressed('d')) {
            movementDirection = movementDirection.add(new Vec2(1, 0));
        }
        if (movementDirection.x != 0 || movementDirection.y != 0) {
            var deltaPos = movementDirection.unit().mul(SPEED * rt.deltaTime);
            pos.x += deltaPos.x;
            pos.y += deltaPos.y;
        }
        if (Double.isNaN(pos.x)) {
            throw new RuntimeException("pos became nan");
        }
        // System.out.println("Position: %f,%f".formatted(pos.x, pos.y));
    }

}
