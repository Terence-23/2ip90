import java.awt.Color;
import java.awt.Graphics;

class DrawingTest implements GameObject {
    Vec2 pos = new Vec2(0, 0);
    Vec2 velocity = new Vec2(30, 15);

    @Override
    public void update() {
        System.out.println("Old pos: %f, %f".formatted(pos.x, pos.y));
        pos = pos.add(velocity.mul(GameRuntime.rt.deltaTime));
        System.out.println("New pos: %f, %f".formatted(pos.x, pos.y));
    }

    @Override
    public void draw(Graphics g) {
        System.out.println("redraw");
        g.setColor(Color.BLACK);
        g.fillOval((int) pos.x, (int) pos.y, 20, 30);
    }

    @Override
    public Vec2 getPos() {
        return pos;
    }

    @Override
    public void setup() {
    }

    public static void main(String[] args) throws Exception {
        GameRuntime.rt = new GameRuntime();
        GameRuntime.rt.canvas.setBackground(Color.red);

        GameRuntime.rt.objects.add(new DrawingTest());

        GameRuntime.rt.setup();

        while (true) {
            GameRuntime.rt.update();
            Thread.sleep(30);
        }
    }
}
