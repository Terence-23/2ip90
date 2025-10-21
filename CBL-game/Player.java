import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
// Added these imports

class Player implements GameObject {

    Vec2 pos;
    double bulletDamage = 10;
    double bulletSpeed = 10;
    double bulletInterval = 0.1;
    double timeSinceLastBullet = bulletInterval;
    double SPEED = 4.;
    static final Color COLOR = Color.blue;
    static final double MAX_HEALTH = 100;
    double health;
    boolean setup = false;
    Rectangle lastDraw;
    String lastHPString;

    Animation animLeft, animRight;
    Animation idleLeft, idleRight;
    Animation shootLeft, shootRight;
    Animation deadLeft, deadRight;
    Animation currentAnim;
    int frameWidth = 128;// 32;
    int frameHeight = 128;// 32; // these

    enum PlayerState {
        IDLE, WALK, SHOOT, DEAD
    }

    PlayerState currentState = PlayerState.IDLE;
    boolean facingRight = true;

    void loadAnimations() {
        try {
            // animUp = new Animation(loadFrames("res/walk_up.png"), 150);
            // animDown = new Animation(loadFrames("res/walk_down.png"), 150);
            animLeft = new Animation(loadFrames("walk_left.png"), 150);
            animRight = new Animation(loadFrames("walk_right.png"), 150);
            currentAnim = animRight; // default looks right
            idleLeft = new Animation(loadFrames("idle_left.png"), 300);
            idleRight = new Animation(loadFrames("idle_right.png"), 300);

            shootLeft = new Animation(loadFrames("shoot_left.png"), 100);
            shootRight = new Animation(loadFrames("shoot_right.png"), 100);

            deadLeft = new Animation(loadFrames("dead_left.png"), 500);
            deadRight = new Animation(loadFrames("dead_right.png"), 500);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    BufferedImage[] loadFrames(String path) throws IOException {
        BufferedImage sheet = ImageIO.read(new File(path));
        int frameCount = sheet.getWidth() / frameWidth;
        BufferedImage[] frames = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = sheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
        }

        // test save the first image and sheet
        // try {
        // ImageIO.write(sheet, "png", new File("testsheet" + path));
        // ImageIO.write(frames[0], "png", new File("test" + path));
        //
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        return frames;
    }

    @Override
    public Vec2 getPos() {
        return pos;
    }

    @Override
    public void setup() {
        pos = new Vec2(0, 0);
        health = MAX_HEALTH;
        setup = true;
        loadAnimations(); // animations
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

        // g.setColor(COLOR);
        // g.fillRect(rect.x, rect.y, rect.width, rect.height);
        BufferedImage frame = currentAnim.getCurrentFrame();
        g.drawImage(frame, rect.x, rect.y, rect.width, rect.height, null);

        var metrics = g.getFontMetrics();

        if (lastHPString != null) {
            g.clearRect(9, 19 - metrics.getAscent(), metrics.stringWidth(lastHPString) + 2, metrics.getHeight() + 2);
        }

        lastHPString = "HP: %d DMG: %d".formatted((int) health, (int) bulletDamage);

        g.drawString(lastHPString, 10, 20);

    }

    public void damage(double dmg) {
        health -= dmg;
        if (health <= 0 && currentState != PlayerState.DEAD) {
            SwingUtilities.invokeLater(() -> {
                currentState = PlayerState.DEAD;
                currentAnim = facingRight ? deadRight : deadLeft;
                GameRuntime.rt.gameOver();
            });

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
            facingRight = false;
        }
        if (Input.isKeyPressed('d')) {
            movementDirection = movementDirection.add(new Vec2(1, 0));
            facingRight = true;
        }
        // if (movementDirection.x != 0 || movementDirection.y != 0) {
        // var deltaPos = movementDirection.unit().mul(SPEED * rt.deltaTime);
        // pos.x += deltaPos.x;
        // pos.y += deltaPos.y;
        // }
        if (movementDirection.x != 0 || movementDirection.y != 0) {
            var deltaPos = movementDirection.unit().mul(SPEED * rt.deltaTime);
            pos = pos.add(deltaPos);
            // moved = true; commented this one
            currentState = PlayerState.WALK;
            // anim
            // Determine direction for animation
            if (Math.abs(movementDirection.x) > Math.abs(movementDirection.y)) {
                currentAnim = movementDirection.x > 0 ? animRight : animLeft;
            }
            // } else {
            // currentAnim = movementDirection.y > 0 ? animDown : animUp;
            // }

        } else {
            currentState = PlayerState.IDLE;
        }

        if (Input.isMousePressed(1) && timeSinceLastBullet > bulletInterval) {
            shootBullet();
            timeSinceLastBullet = 0;
            currentState = PlayerState.SHOOT;
        }

        timeSinceLastBullet += rt.deltaTime;

        switch (currentState) {
            case WALK:
                currentAnim = facingRight ? animRight : animLeft;
                break;
            case IDLE:
                currentAnim = facingRight ? idleRight : idleLeft;
                break;
            case SHOOT:
                currentAnim = facingRight ? shootRight : shootLeft;
                break;
            case DEAD:
                currentAnim = facingRight ? deadRight : deadLeft;
                break;
        }

        if (currentAnim != null) {
            currentAnim.update();
        }

        if (Double.isNaN(pos.x)) {
            throw new RuntimeException("pos became nan");
        }
        // if (Input.isMousePressed(1) && timeSinceLastBullet > bulletInterval) {
        // shootBullet();
        // timeSinceLastBullet = 0;
        // }
        // timeSinceLastBullet += rt.deltaTime;
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
