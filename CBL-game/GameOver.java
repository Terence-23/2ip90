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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

class GameOver extends JPanel {

    GameOver() {
        super(new BorderLayout());
        var text = new JLabel("GAME OVER", SwingConstants.CENTER);
        setBackground(Color.BLACK);
        // text.setText("GAME OVER");
        text.setBackground(Color.black);
        text.setForeground(Color.white);

        text.setFont(text.getFont().deriveFont(80f));

        var button = new JButton("Restart");
        button.setBackground(Color.black);
        button.setForeground(Color.white);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Restart Game");
                GameRuntime.rt.setup();
            }

        });
        add(text, BorderLayout.CENTER);
        add(button, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);

            frame.setVisible(true);
            frame.validate();
            var game = new GameOver();
            frame.setContentPane(game);
            frame.validate();
            // System.out.println(game.getSize());
        });
    }

}
