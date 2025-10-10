import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

class Player implements GameObject {

    Vec2 pos;
    double bulletDamage = 10;
    double bulletSpeed = 10;
    double bulletInterval = 0.1;
    double timeSinceLastBullet = bulletInterval;
    static final double SPEED = 2.;
    static final Color COLOR = Color.blue;
    static final double MAX_HEALTH = 100;
    double health;
    boolean setup = false;
    Rectangle lastDraw;

    @Override
    public Vec2 getPos() {
        return pos;
    }

    @Override
    public void setup() {
        pos = new Vec2(0, 0);
        health = MAX_HEALTH;
        setup = true;
    }

    @Override
    public void draw(Graphics g) {
        var rt = GameRuntime.rt;
        var startCorner = rt.map_space_to_screen(pos.sub(new Vec2(0.5, 0.5)));
        var endCorner = rt.map_space_to_screen(pos.add(new Vec2(0.5, 0.5)));

        var size = endCorner.sub(startCorner);

        if (lastDraw != null) {
            g.clearRect(lastDraw.x, lastDraw.y, lastDraw.width, lastDraw.height);
        }
        Rectangle rect = new Rectangle(
                (int) startCorner.x,
                (int) startCorner.y,
                (int) size.x,
                (int) size.y);
        lastDraw = rect;

        g.setColor(COLOR);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
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
        if (!setup) {
            return;
        }
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
        if (Input.isMousePressed(1) && timeSinceLastBullet > bulletInterval) {
            shootBullet();
            timeSinceLastBullet = 0;
        }
        timeSinceLastBullet += rt.deltaTime;
        // System.out.println("Position: %f,%f".formatted(pos.x, pos.y));
    }

    void shootBullet() {
        var rt = GameRuntime.rt;

        var mousePos = Input.getMousePos();

        var bounds = rt.canvas.getBounds();
        var centerx = bounds.x + bounds.width * 0.5;
        var centery = bounds.y + bounds.height * 0.5;
        var dir = new Vec2(mousePos.x - centerx, mousePos.y - centery);
        if (dir.x == 0 && dir.y == 0) {
            dir = new Vec2(1, 0);
        }
        var velocity = dir.unit().mul(bulletSpeed);

        Bullet bullet = new Bullet(bulletDamage, pos, velocity);
        rt.add(bullet);
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                rt.remove(bullet);
            }

        }, 3000);
    }

}
