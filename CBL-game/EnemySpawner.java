import java.awt.Graphics;
import java.util.Random;

class EnemySpawner implements Runnable, GameObject {

    static final double interval = 2;
    double time_since_last = 0;

    static final double minDistance = 12;
    static final double maxDistance = 16;
    static final int groupSize = 3;
    Random rng = new Random();

    @Override
    public void run() {
        var angle = rng.nextDouble() * Math.TAU;
        var distance = rng.nextDouble(minDistance, maxDistance);
        // Generate a vector pointing at the desired angle from x axis,
        // then make it the required length and offset it by player position
        // because that's our frame of reference
        var centerGroup = new Vec2(Math.cos(angle), Math.sin(angle))
                .mul(distance)
                .add(GameRuntime.rt.player.getPos());

        var stepAngle = Math.TAU / groupSize;
        for (int i = 0; i < groupSize; ++i) {
            // make enemies in a circle aropund centerGroup;
            var pos = new Vec2(Math.cos(stepAngle * i), Math.sin(stepAngle * i)).add(centerGroup);
            var enemy = new Enemy(pos);
            GameRuntime.rt.add(enemy);
        }
    }

    @Override
    public Vec2 getPos() {
        return new Vec2(0, 0);
    }

    @Override
    public void setup() {
    }

    @Override
    public void draw(Graphics g) {
    }

    @Override
    public void update() {
        time_since_last += GameRuntime.rt.deltaTime;
        while (time_since_last > interval) {
            run();
            time_since_last -= interval;
        }
    }

}
