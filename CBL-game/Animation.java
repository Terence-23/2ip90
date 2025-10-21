import java.awt.image.BufferedImage;

public class Animation {
    private BufferedImage[] frames;
    private int currentFrame;
    private int delay; // in milliseconds
    // changed time representation to be more inline with rest of the repo
    private long timeSinceLastChange; // in milliseconds

    public Animation(BufferedImage[] frames, int delay) {
        this.frames = frames;
        this.delay = delay;
        this.currentFrame = 0;
        this.timeSinceLastChange = 0;
    }

    public void update() {
        timeSinceLastChange += (long) (GameRuntime.rt.deltaTime * 1000);
        if (timeSinceLastChange >= delay) {
            currentFrame = (currentFrame + 1) % frames.length;
            timeSinceLastChange = 0;
        }
    }

    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }
}
