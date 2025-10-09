
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

class GameStart extends JPanel {

    GameStart() {
        super(new BorderLayout());

        setOpaque(true);
        setBackground(Color.pink);
        var button = new JButton("Start");
        button.setBackground(Color.pink);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Start Game");
                GameRuntime.rt.setup();
            }

        });
        add(button, BorderLayout.CENTER);
        // System.out.println("Panel size: " + getSize());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);

            frame.setVisible(true);
            frame.validate();
            var game = new GameStart();
            frame.setContentPane(game);
            frame.validate();
            // System.out.println(game.getSize());
        });
    }
}
