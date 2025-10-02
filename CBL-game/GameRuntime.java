import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

/**
 * The main runtime of the game.
 */
public class GameRuntime {

    public static GameRuntime rt;
    public double deltaTime;
    Instant startTime;

    ArrayList<GameObject> objects;

    GameRuntime() {
        objects = new ArrayList<>();
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
            gameObject.draw();
        }
    }

    /**
     * Draws the window from scratch.
     */
    void redraw() {

        for (GameObject object : objects) {
            object.draw();
        }
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
