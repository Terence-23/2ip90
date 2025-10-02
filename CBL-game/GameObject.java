public interface GameObject {
    Vec2 getPos();

    void setup();

    void draw();

    void update();

    default void onDestroy() {
    }

    default void onCollide(GameObject oth) {
    }
}
