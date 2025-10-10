import java.awt.Graphics;

/**
 * The set of things all game objects should be able to do.
 */
public interface GameObject {
    Vec2 getPos();

    void setup();

    /**
     * objects should during drawing also remove their artifacts.
     */
    void draw(Graphics g);

    void update();

    default void onDestroy() {
    }

    default void onCollide(GameObject oth) {
    }
}
