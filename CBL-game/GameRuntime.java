import java.awt.Color;
import java.awt.Graphics;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * The main runtime of the game.
 */
public class GameRuntime {

    public Player player;
    // The size of the screen in gamespace;
    protected Vec2 screenSize = new Vec2(16, 9);

    /**
     * maps a vector from game space to scren.
     * 
     * @param v the Vector to be mapped
     * @return the mapped vector
     */
    public Vec2 map_space_to_screen(Vec2 v) {
        var playerPos = player.getPos();
        // System.out.println(playerPos.x);
        // System.out.println(screenSize.x);
        var x = (v.x - playerPos.x) / screenSize.x + 0.5;
        var y = (v.y - playerPos.y) / screenSize.y + 0.5;

        // System.out.println(x);
        // System.out.println(y);
        return new Vec2(x * canvas.getCanvasWidth(), y * canvas.getCanvasHeight());
    }

    public static GameRuntime rt;
    public double deltaTime;
    Instant startTime;
    // the drawing area of the game.

    JFrame frame;
    GameCanvas canvas;

    ArrayList<GameObject> objects;
    HashMap<String, CollisionLayer> collisionLayers;

    GameRuntime() {
        objects = new ArrayList<>();
        frame = new JFrame("Title Pending");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvas = new GameCanvas(objects);
        frame.add(canvas);
        collisionLayers = new HashMap<>();

        frame.setSize(1280, 720);
        canvas.setDimensions(1280, 720);
        frame.setVisible(true);
        canvas.createBufferStrategy(2);
    }

    /**
     * Executes only once at the beginning of the game.
     */
    void setup() {
        startTime = Instant.now();
        player = new Player();
        objects.add(player);

        for (GameObject gameObject : objects) {
            gameObject.setup();
        }

        redraw();
    }

    /**
     * Executes repeatedly.
     */
    void update() {
        Duration tmp = Duration.between(startTime, Instant.now());
        deltaTime = tmp.getSeconds();
        deltaTime += tmp.getNano() * 1e-9;
        startTime = Instant.now();

        for (GameObject gameObject : objects) {
            gameObject.update();
        }
        redraw();
    }

    /**
     * Draws the window from scratch.
     */
    void redraw() {
        canvas.render();
    }

    public static void main(String[] args) {
        GameRuntime.rt = new GameRuntime();
        Input input = new Input();
        rt.canvas.setBackground(Color.gray);

        GameRuntime.rt.setup();

        while (true) {
            GameRuntime.rt.update();
        }

    }
}
