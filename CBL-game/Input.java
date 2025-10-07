import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Class fro handling input. Knows which keys are pressed.
 */
public class Input {
    private static ArrayList<Character> pressed = new ArrayList<>();

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
                                // System.out.println("key released: %s".formatted(e.getKeyChar()));
                                Input.pressed.remove(Input.pressed.indexOf(e.getKeyChar()));
                                // System.out.printf("position of %s: %d\n", e.getKeyChar(),
                                        Input.pressed.indexOf(e.getKeyChar()));
                                // if (pressed.contains(e.getKeyChar())) {
                                //     throw new RuntimeException("It contains the removed key");
                                // }
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

}
