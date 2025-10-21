import java.awt.image.BufferedImage;

public class Animation {
    private BufferedImage[] frames;
    private int currentFrame;
    private int delay; // in milliseconds
    private long lastTime;

    public Animation(BufferedImage[] frames, int delay) {
        this.frames = frames;
        this.delay = delay;
        this.currentFrame = 0;
        this.lastTime = System.currentTimeMillis();
    }

    public void update() {
        if (System.currentTimeMillis() - lastTime >= delay) {
            currentFrame = (currentFrame + 1) % frames.length;
            lastTime = System.currentTimeMillis();
        }
    }

    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }
}
