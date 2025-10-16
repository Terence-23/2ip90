import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

class Enemy implements GameObject {
    static double max_health = 50;
    Vec2 pos;
    double health = max_health;
    Rectangle lastDraw;
    final Vec2 size = new Vec2(1, 1);
    static final double UPGRADE_CHANCE = 0.3;
    static final Upgrade[] POSSIBLE_UPGRADES = new Upgrade[] {
            new DamageUpgrade(),
            new SpeedUpgrade(),
            new HealUpgrade(),
    };
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
            try {
                var bs = GameRuntime.rt.canvas.getBufferStrategy();
                var g = bs.getDrawGraphics();

                g.clearRect(
                        lastDraw.x - 1,
                        lastDraw.y - 1,
                        lastDraw.width + 2,
                        lastDraw.height + 2);

                g.dispose();
                bs.show();
            } catch (Exception e) {
            }
        }
        var rng = new Random();
        if (rng.nextDouble() < UPGRADE_CHANCE) {
            var upgrade = POSSIBLE_UPGRADES[rng.nextInt(POSSIBLE_UPGRADES.length)];
            GameRuntime.rt.add(new UpgradeObject((Upgrade) (upgrade.clone()), getPos()));

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

    final double speed = 3;
    private static final double minDistance = 1;
    private static final double slowDistance = 3;

    /**
     * Linearly interpolates position between min and max.
     *
     * @param min      minimum position
     * @param max      maximum position
     * @param position interpolation parameter
     * @param iValue   multiplier for the final value
     * @return if position < min return 0, if position > max return iValue else
     *         return linear interpolation between 0 and iValue
     */
    double lerp(double min, double max, double position, double iValue) {
        var correctedPosition = (position - min) / (max - min);
        return Math.clamp(correctedPosition, 0, 1) * iValue;

    }

    void trackPlayer() {
        var player = GameRuntime.rt.player;
        var offset = player.getPos().sub(getPos());
        var distance = offset.length();
        var dir = offset.div(distance);

        var specificSpeed = lerp(minDistance, slowDistance, distance, speed);
        var velocity = dir.mul(specificSpeed);

        this.pos = this.pos.add(velocity.mul(GameRuntime.rt.deltaTime));
    }

    final double damageVal = 10;
    final double attack_range = 3;
    double timeSinceLastDamage = 10;
    double damageInterval = 0.5;

    void attackPlayer() {
        timeSinceLastDamage += GameRuntime.rt.deltaTime;
        var player = GameRuntime.rt.player;
        var offset = player.getPos().sub(getPos());
        var distance = offset.length();
        if (distance <= attack_range && timeSinceLastDamage > damageInterval) {
            player.damage(damageVal);
            timeSinceLastDamage = 0;
        }
    }

    @Override
    public void update() {
        trackPlayer();
        attackPlayer();
    }
}
