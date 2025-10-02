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
    Graphics g;

    ArrayList<GameObject> objects;
    HashMap<String, CollisionLayer> collisionLayers;

    GameRuntime() {
        objects = new ArrayList<>();
        collisionLayers = new HashMap<>();
    }

    /**
     * Executes only once at the beginning of the game.
     */
    void setup() {
        startTime = Instant.now();
    }

    /**
     * Executes repeatedly.
     */
    void update() {
        Duration tmp = Duration.between(startTime, Instant.now());
        deltaTime = tmp.getSeconds();
        deltaTime += tmp.getNano() * 1e-9;

        for (GameObject gameObject : objects) {
            gameObject.update();
        }
        for (GameObject gameObject : objects) {
            gameObject.draw(g);
        }
    }

    /**
     * Draws the window from scratch.
     */
    void redraw() {

        for (GameObject object : objects) {
            object.draw(g);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Window");
        JPanel panel = new JPanel();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(panel);

        GameRuntime.rt = new GameRuntime();
        rt.g = panel.getGraphics();

        GameRuntime.rt.setup();

        while (true) {
            System.out.println("while");
            GameRuntime.rt.update();
            GameRuntime.rt.redraw();
        }

    }
}
