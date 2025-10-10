import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * The main runtime of the game.
 */
public class GameRuntime {

    public Player player;
    // The size of the screen in gamespace;
    protected Vec2 screenSize = new Vec2(64, 36);
    boolean started = false;
    Executor renderer = Executors.newSingleThreadExecutor();

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
        return new Vec2(x * canvas.getWidth(), y * canvas.getHeight());
    }

    void gameOver() {
        startTime = null;
        started = false;
        // throw new UnsupportedOperationException("this feature is not yet
        // implemented");
        synchronized (objects) {
            objects.clear();
            SwingUtilities.invokeLater(() -> {
                gOver = new GameOver();
                frame.getContentPane().remove(canvas);
                frame.getContentPane().remove(panel);
                if (!Arrays.asList(frame.getContentPane().getComponents()).contains(panel)) {
                    frame.getContentPane().add(gOver);
                }

                frame.revalidate();
                frame.repaint();
                System.out.println("gover");
                System.out.println("Current components: " + Arrays.toString(frame.getContentPane().getComponents()));
            });
        }
    }

    public static GameRuntime rt;
    public double deltaTime;
    Instant startTime;
    // the drawing area of the game.

    JFrame frame;
    GameCanvas canvas;
    GameStart start = new GameStart();
    GamePanel panel;
    GameOver gOver = new GameOver();
    private ArrayList<GameObject> addQue = new ArrayList<>();
    private ArrayList<GameObject> delQue = new ArrayList<>();

    private ArrayList<GameObject> objects;
    HashMap<String, CollisionLayer> collisionLayers;

    GameRuntime() {
        objects = new ArrayList<>();
        frame = new JFrame("Title Pending");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvas = new GameCanvas(objects);
        collisionLayers = new HashMap<>();
        frame.getContentPane().add(start);
        // frame.add(canvas);

        frame.setSize(1280, 720);
        frame.setVisible(true);
    }

    void add(GameObject o) {
        synchronized (addQue) {
            addQue.add(o);
        }
    }

    void remove(GameObject o) {
        synchronized (delQue) {
            delQue.add(o);
        }
    }

    /**
     * Executes only once at the beginning of the game.
     */
    void setup() {
        started = true;
        startTime = Instant.now();
        frame.remove(gOver);
        panel = new GamePanel();
        panel.add(canvas);

        frame.getContentPane().remove(start);
        frame.getContentPane().remove(gOver);
        frame.getContentPane().add(panel);

        frame.revalidate();
        frame.repaint();

        // canvas.setDimensions(1280, 720);
        canvas.createBufferStrategy(2);
        player = new Player();
        objects.add(player);

        for (GameObject gameObject : objects) {
            gameObject.setup();
        }

        synchronized (canvas) {
            if (!canvas.isDisplayable()) {
                return;
            }
            redraw();
        }

    }

    /**
     * Executes repeatedly.
     */
    void update() {
        synchronized (objects) {
            synchronized (addQue) {
                for (GameObject o : addQue) {
                    System.out.println("object added");
                    objects.add(o);
                }
                addQue.clear();
            }
            synchronized (delQue) {
                for (GameObject o : delQue) {
                    o.onDestroy();
                    objects.remove(o);
                }
                delQue.clear();
            }

        }
        if (!started) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }
            return;
        }
        Duration tmp = Duration.between(startTime, Instant.now());
        deltaTime = tmp.getSeconds();
        deltaTime += tmp.getNano() * 1e-9;

        startTime = Instant.now();
        // for testing only
        if (Input.isKeyPressed('o')) {
            player.damage(deltaTime * 100);
        }

        for (GameObject gameObject : objects) {
            gameObject.update();
        }

        redraw();
    }

    /**
     * Draws the window from scratch.
     */
    void redraw() {
        if (canvas.clean) {
            canvas.clean = false;
            renderer.execute(canvas);
        }

    }

    public static void main(String[] args) {
        GameRuntime.rt = new GameRuntime();
        Input input = new Input();
        rt.canvas.addMouseListener(input);
        rt.canvas.addMouseMotionListener(input);
        rt.canvas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                rt.canvas.setDimensions(rt.canvas.getWidth(), rt.canvas.getHeight());
            }
        });
        rt.canvas.setBackground(Color.gray);

        // GameRuntime.rt.setup();

        while (true) {
            GameRuntime.rt.update();
        }

    }
}
