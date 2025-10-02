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
        frame.setVisible(true);

    }

    /**
     * Executes only once at the beginning of the game.
     */
    void setup() {
        startTime = Instant.now();

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
        canvas.repaint();
    }

    public static void main(String[] args) {
        GameRuntime.rt = new GameRuntime();

        GameRuntime.rt.setup();

        while (true) {
            System.out.println("while");
            GameRuntime.rt.update();
            GameRuntime.rt.redraw();
        }

    }
}
