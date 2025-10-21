import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashSet;
import javax.swing.event.MouseInputListener;

/**
 * Class fro handling input. Knows which keys are pressed.
 */
public class Input implements MouseInputListener, MouseWheelListener {
    private static HashSet<Character> pressed = new HashSet<>();
    private static HashSet<Integer> mousePressed = new HashSet<>();
    private static Vec2 mousePos = new Vec2(0, 0);

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // System.out.println("mouse pressed");
        Input.mousePressed.add(e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // System.out.println("mouse released");
        Input.mousePressed.remove(e.getButton());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Input.mousePos.x = e.getX();
        Input.mousePos.y = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Input.mousePos.x = e.getX();
        Input.mousePos.y = e.getY();
    }

    /**
     * Needed before any input actions are checked. Sets up the event listener.
     */
    Input() {
        KeyboardFocusManager
                .getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {

                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        switch (e.getID()) {
                            case KeyEvent.KEY_PRESSED:
                                Input.pressed.add(e.getKeyChar());
                                break;
                            case KeyEvent.KEY_RELEASED:
                                Input.pressed.remove(e.getKeyChar());
                                break;
                            default:
                                break;

                        }
                        return false;

                    }
                });

    }

    public static boolean isKeyPressed(Character k) {
        return pressed.contains(k);
    }

    public static boolean isMousePressed(int b) {
        return mousePressed.contains(b);
    }

    public static Vec2 getMousePos() {
        return mousePos;
    }

    double zoomPerScroll = 1.1;

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int scrollCount = e.getWheelRotation();
        // System.out.println("Scroll. Clicks: %d".formatted(scrollCount));
        GameRuntime.rt.screenVSize *= Math.pow(zoomPerScroll, scrollCount);

    }

}
