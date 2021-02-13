// Contains main method

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyFrame extends Game {

    private static final long serialVersionUID = 1L;

    // Frame window
    static JFrame f = new JFrame("Connect Four");
    private static final int frameWidth = 550, frameHeight = 600;

    private static final int button1X = 70, button1Y = 510, button2X = 380, button2Y = button1Y;
    private static final int buttonWidth = 100, buttonHeight = 50;
    
    static Player player1 = new Player('x');
    static Player player2 = new Player('o');
    static Game game = new Game(player1, player2);

    public MyFrame(Player player1, Player player2) {
        super(player1, player2);
    }

    public static void setFrame() {
        // Buttons
        JButton onePlayerB = new JButton("1 Player");
        JButton twoPlayerB = new JButton("2 Player");
        // Button size
        onePlayerB.setBounds(button1X, button1Y, buttonWidth, buttonHeight);
        twoPlayerB.setBounds(button2X, button2Y, buttonWidth, buttonHeight);
        // Button actions
        onePlayerB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetBoard();
                resetWinner();
                resetTurnCount();
                f.repaint();
                if (getNumPlayers() == -1) {
                    onePlayerB.setVisible(true);
                    twoPlayerB.setVisible(true);
                }
                // Play onePlayer game
                // System.out.println("1 Player game");
                onePlayerB.setVisible(false);
                twoPlayerB.setVisible(false);
                setNumPlayers(1);
            }
        });
        twoPlayerB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetBoard();
                resetWinner();
                resetTurnCount();
                f.repaint();
                if (getNumPlayers() == -1) {
                    onePlayerB.setVisible(true);
                    twoPlayerB.setVisible(true);
                }
                // Play twoPlayer game
                // System.out.println("2 Player game");
                onePlayerB.setVisible(false);
                twoPlayerB.setVisible(false);
                setNumPlayers(2);
            }
        });

        f.setSize(frameWidth, frameHeight);
        f.setBackground(Color.WHITE);

        // Choose between 1-player and 2-player if !gameActive
        f.add(onePlayerB);
        f.add(twoPlayerB);

        f.add(game);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }

    public static void main(String[] args) {

        setFrame();

    }
}
