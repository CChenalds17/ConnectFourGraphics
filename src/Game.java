// Sets up game and board

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Game extends JPanel {

    private static final long serialVersionUID = 1L;

    static JFrame f = new JFrame("Connect Four");

    // Creates an empty board
    protected static char[][] board = new char[6][7];
    // Creates 2 players
    public final Player player1, player2;
    private static int turnCount = 1;
    private static int numPlayers = -1;

    private static String playerTurnMessage;
    private static int moveColumn = -1;
    private static char winner = ' ';

    // Graphics constants

    private static final int boardWidth = 370, boardHeight = 315;
    private static final int boardX = 90, boardY = 95;

    private static final int frameWidth = 550, frameHeight = 600;

    private static final int pieceWidth = 35, pieceHeight = pieceWidth;
    private static final int pieceX = 105, pieceY = 110;
    private static final int pieceIncrement = 15;

    private static int messageX = 170, messageY = 475;

    // Mouse variables
    private int mouseX, mouseY, mouseButton;
    
    public Game(Player player1, Player player2) {
        // Empty board of '-'
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = '-';
            }
        }

        this.player1 = player1;
        this.player2 = player2;

        // Window size
        setPreferredSize(new Dimension(frameWidth, frameHeight));

        // Mouse action
        addMouseListener(new MouseInputAdapter(){
            public void mouseReleased(MouseEvent me) {
                mouseX = me.getX();
                mouseY = me.getY();
                mouseButton = me.getButton(); /** Button 1 is left click */                
                
                mouseReleasedAction(mouseX, mouseY, mouseButton);

            }
        });

    }
    
    private void mouseReleasedAction(int mouseX, int mouseY, int mouseButton) {
        // For two player game:
        if (getNumPlayers() == 2) {
            // Only does something if mouseButton is left click and mouse is on the board
            if (mouseButton == 1 && mouseY > boardY && mouseY < boardY + boardHeight) {
                // Checks which column the click is on
                for (int i = 0; i < board[0].length; i++) {
                    if (mouseX > pieceX + pieceWidth * i + pieceIncrement * i && mouseX < pieceX + pieceIncrement * i + pieceWidth * (i + 1)) {
                        moveColumn = i + 1;
                        // System.out.printf("column %d \n", moveColumn);
                        // System.out.printf("mouseX: %d, mouseY: %d, mouseButton: %d \n", mouseX, mouseY, mouseButton);

                        // If column isn't full:
                        if (board[0][moveColumn - 1] == '-') {
                            if (turnCountIsOdd()) {
                                // Plays turn for player 1
                                playTurnPlayer(player1, moveColumn);
                            } else {
                                // Plays turn for player 2
                                playTurnPlayer(player2, moveColumn);
                            }
                            // Reset moveColumn
                            moveColumn = -1;
                            break;
                        } else {
                            // System.out.println("ERROR: Column is full. Pick another column.");
                        }
                    }
                }
            }
            // Check win conditions:
            if (checkWin(player1)) {
                winner = '1';
                setNumPlayers(-1);
                MyFrame.setFrame();
            } else if (checkWin(player2)) {
                winner = '2';
                setNumPlayers(-1);
                MyFrame.setFrame();
            } else if (checkTie()) {
                winner = 't';
                setNumPlayers(-1);
                MyFrame.setFrame();
            }            
        // For one player game:
        } else if (getNumPlayers() == 1) {
            // Only does something if mouseButton is left click and mouse is on the board
            if (mouseButton == 1 && mouseY > boardY && mouseY < boardY + boardHeight) {
                // Checks which column the click is on
                for (int i = 0; i < board[0].length; i++) {
                    if (mouseX > pieceX + pieceWidth * i + pieceIncrement * i && mouseX < pieceX + pieceIncrement * i + pieceWidth * (i + 1)) {
                        moveColumn = i + 1;
                        // System.out.printf("column %d \n", moveColumn);
                        // System.out.printf("mouseX: %d, mouseY: %d, mouseButton: %d \n", mouseX, mouseY, mouseButton);

                        // If column isn't full:
                        if (board[0][moveColumn - 1] == '-') {
                            // Plays turn for player 1
                            playTurnPlayer(player1, moveColumn);

                            // Check if player one wins:
                            if (checkWin(player1)) {
                                winner = '1';
                                setNumPlayers(-1);
                                MyFrame.setFrame();
                            } else {
                                // Plays random move for computer
                                playTurnComputer(player2);
                            }

                            // Check if computer wins or if there is a tie:
                            if (checkWin(player2)) {
                                winner = 'c';
                                setNumPlayers(-1);
                                MyFrame.setFrame();
                            } else if (checkTie()) {
                                winner = 't';
                                setNumPlayers(-1);
                                MyFrame.setFrame();
                            }
                            
                            // Reset moveColumn
                            moveColumn = -1;
                            break;
                        } else {
                            // System.out.println("ERROR: Column is full. Pick another column.");
                        }
                    }
                }
            }
        }
        
    }

    protected static void setNumPlayers(int i) {
        numPlayers = i;
    }

    public static int getNumPlayers() {
        return numPlayers;
    }

    protected static void resetBoard() {
        // Empty board of '-'
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = '-';
            }
        }
    }

    protected static void resetTurnCount() {
        turnCount = 1;
    }

    protected static void resetWinner() {
        winner = ' ';
    }

    private void playTurnPlayer(Player player, int column) {

        // Puts chip on lowest available row
        for (int i = board.length - 1; i >= 0; i--) {
            if (board[i][column - 1] == '-') {
                board[i][column - 1] = player.playerChar;
                // System.out.printf("Move made in column: %d", column);
                break;
            }
        }

        turnCount++;

        repaint();
    }

    private void playTurnComputer(Player player) {

        boolean isValidMove = false;
        // Gets random int (1-column count) as computer move (if it's a valid move)
        while (!isValidMove) {
            Random rand = new Random();
            moveColumn = rand.nextInt(board[0].length);

            // Checks that the column isn't empty
            if (board[0][moveColumn] != '-') {
                // System.out.println("Column " + moveColumn + " + 1 is full. Try again.");
                isValidMove = false;
            } else {
                isValidMove = true;
            }
        }

        // Puts chip on lowest available row
        for (int i = board.length - 1; i >= 0; i--) {
            if (board[i][moveColumn] == '-') {
                board[i][moveColumn] = player.playerChar;
                break;
            }
        }

        turnCount ++;

        repaint();
    }

    private boolean checkWin(Player player) {
        return checkRow(player) || checkColumn(player) || checkRightDiagonal(player) || checkLeftDiagonal(player);
    }

    private boolean checkTie() {
        return turnCount > 42;
    }

    private boolean turnCountIsOdd() {
        return turnCount % 2 == 1;
    }

    private boolean checkRow(Player player) {

        int count = 0;
        // For each row:
        for (int i = board.length - 1; i >= 0; i--) {
            // Reset count when switching rows
            count = 0;
            //For each element within the row:
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == player.playerChar) {
                    count += 1;
                } else {
                    count = 0;
                }
                if (count >= 4) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkColumn(Player player) {

        int count = 0;
        // For each column:
        for (int i = board[0].length - 1; i >= 0; i--) {
            // Reset count when switching columns
            count = 0;
            // For each element within the column:
            for (int j = 0; j < board.length; j++) {
                if (board[j][i] == player.playerChar) {
                    count += 1;
                } else {
                    count = 0;
                }
                if (count >= 4) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkRightDiagonal(Player player) {

        int count = 0;

        // For each column:
        for (int i = 0; i < board[0].length - 3; i++) {
            // For each element within the column:
            for (int j = 0; j < board.length - 3; j++) {
                // Checking the diagonal
                count = 0;
                for (int k = 0; k < 4; k++) {
                    if (board[j+k][i+k] == player.playerChar) {
                        count += 1;
                    } else {
                        count = 0;
                    }
                    if (count >= 4) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean checkLeftDiagonal(Player player) {

        int count = 0;

        // For each column:
        for (int i = 3; i < board[0].length; i++) {
            // For each element within the column:
            for (int j = 0; j < board.length - 3; j++) {
                // Checking the diagonal
                count = 0;
                for (int k = 0; k < 4; k++) {
                    // System.out.printf("Checking %d,%d",j+k,i-k);
                    if (board[j+k][i-k] == player.playerChar) {
                        count += 1;
                    } else {
                        count = 0;
                    }
                    if (count >= 4) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Prints Connect Four along the top
        Font titleFont = new Font ("Comic Sans MS", Font.BOLD + Font.ITALIC, 40);
        g.setFont(titleFont);
        g.setColor(Color.ORANGE);
        g.drawString("Connect Four", 150, 5 * boardY / 7);

        g.setColor(Color.BLUE);
        g.fillRect(boardX, boardY, boardWidth, boardHeight);

        // Makes the slots with chip color based on playerChar
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == player1.playerChar) {
                    g.setColor(Color.YELLOW);
                } else if (board[i][j] == player2.playerChar) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillOval(pieceX+j*(pieceWidth+pieceIncrement), pieceY+i*(pieceHeight+pieceIncrement), pieceWidth, pieceHeight);
            }
        }

        // Prints player turn message
        g.setColor(Color.BLACK);
        Font font = new Font ("Comic Sans MS", Font.BOLD, 30);
        Font fontb = new Font ("Comic Sans MS", Font.PLAIN, 20);
        g.setFont(font);
        
        // Win message
        if (winner == '1') {
            messageX = 150;
            playerTurnMessage = "PLAYER 1 WINS!";
        } else if (winner == '2') {
            messageX = 150;
            playerTurnMessage = "PLAYER 2 WINS!";
        } else if (winner == 'c') {
            messageX = 50;
            g.setFont(fontb);
            playerTurnMessage = "You just lost to a computer making random moves.";
        } else if (winner == 't') {
            messageX = 200;
            playerTurnMessage = "It's a tie.";
        } else {
            messageX = 170;
            if (getNumPlayers() == -1) {
                playerTurnMessage = "";
            } else if (turnCountIsOdd()) {
                playerTurnMessage = "Player 1's turn";
            } else {
                playerTurnMessage = "Player 2's turn";
            }
        }
           
        g.drawString(playerTurnMessage, messageX, messageY);

    }
    
}
