import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Chomp extends JFrame implements ActionListener {
    // instance vars
    private Container window;
    private JScrollPane scroll;
    private JPanel panel;
    private JButton[][] buttons;
    private JTextField xSizeInput;
    private JTextField ySizeInput;
    private JButton startButton, restartButton;
    private JTextPane textPane;

    private Node[][] board;
    private int turn;
    private int xBoardSize;
    private int yBoardSize;
    private boolean isPlayer1Turn = true;
    private JButton p1Marker, p2Marker;
    private boolean isP1Marker, isP2Marker = false;

    private JButton backButton;
    private JButton softResetButton;
    private JButton switchTurnButton;

    public Chomp() {
        setUpWindow();
        setUpGame();
    }

    private void setUpWindow() {
        window = getContentPane();
        window.setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setVisible(true);
        setTitle("Chomp");
    }

    private void setUpGame() {
        xSizeInput = new JTextField("x size");
        xSizeInput.setBounds(50, 50, 100, 30);
        window.add(xSizeInput);

        ySizeInput = new JTextField("y size");
        ySizeInput.setBounds(50, 75, 100, 30);
        window.add(ySizeInput);


        startButton = new JButton("Start");
        startButton.setBounds(50, 100, 100, 30);
        startButton.addActionListener(this);
        window.add(startButton);
    }

    private void setUpBoard() {
        turn = 1;

        panel = new JPanel();  
        panel.setLayout(new GridBagLayout());
    

        board = new Node[yBoardSize][xBoardSize];
        buttons = new JButton[yBoardSize][xBoardSize];
        GridBagConstraints c = new GridBagConstraints();
        // made all of the buttons the same square size:
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        
        // fill the pane with buttons of size 50 x 50
        for (int i = 0; i < yBoardSize; i++) {
            for (int j = 0; j < xBoardSize; j++) {
                board[i][j] = new Node();
                buttons[i][j] = new JButton();
                buttons[i][j].addActionListener(this);
                // make them squares
                buttons[i][j].setPreferredSize(new Dimension(Math.max(((int) ((window.getHeight() - 100) / (double)yBoardSize)), 50), Math.max(((int) ((window.getHeight() - 100) / (double)yBoardSize)), 50)));
                buttons[i][j].setText((j + 1) + ", " + (Math.abs(i - yBoardSize)));
                panel.add(buttons[i][j], c);
                c.gridx++;
            }
            c.gridx = 0;
            c.gridy++;
        }

        scroll = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(0, 0,  window.getWidth() - 100,  window.getHeight() - 100);

        window.add(scroll);

        restartButton = new JButton("Restart");
        restartButton.setBounds(0, window.getHeight() - 50, 100, 30);
        restartButton.addActionListener(this);
        window.add(restartButton);

        softResetButton = new JButton("Soft Reset");
        softResetButton.setBounds(100, window.getHeight() - 50, 100, 30);
        softResetButton.addActionListener(this);
        window.add(softResetButton);

        textPane = new JTextPane();
        textPane.setBounds(300, window.getHeight() - 50, 100, 30);
        isPlayer1Turn = true;
        textPane.setText("Player 1's turn");
        textPane.setEditable(false);
        textPane.setBackground(null);
        window.add(textPane);

        p1Marker = new JButton();
        p1Marker.setBounds(0, window.getHeight() - 100, 50, 50);
        p1Marker.setBackground(Color.RED);
        p1Marker.setOpaque(true);
        p1Marker.setContentAreaFilled(true);
        p1Marker.setBorderPainted(true);
        p1Marker.addActionListener(this);
        window.add(p1Marker);

        p2Marker = new JButton();
        p2Marker.setBounds(50, window.getHeight() - 100, 50, 50);
        p2Marker.setBackground(Color.BLUE);
        p2Marker.setOpaque(true);
        p2Marker.setContentAreaFilled(true);
        p2Marker.setBorderPainted(true);
        p2Marker.addActionListener(this);
        window.add(p2Marker);

        backButton = new JButton("Back");
        backButton.setBounds(200, window.getHeight() - 50, 100, 30);
        backButton.addActionListener(this);
        window.add(backButton);
        
        switchTurnButton = new JButton("Switch Turn");
        switchTurnButton.setBounds(200, window.getHeight() - 100, 100, 50);
        switchTurnButton.addActionListener(this);
        window.add(switchTurnButton);
    }

    private boolean isInButtons(JButton button) {
        for (int i = 0; i < yBoardSize; i++) {
            for (int j = 0; j < xBoardSize; j++) {
                if (button == buttons[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            if (e.getSource() == restartButton) {
                window.removeAll();
                window.repaint();
                setUpGame();
                return;
            }
            if (e.getSource() == softResetButton) {
                window.removeAll();
                window.repaint();
                setUpBoard();
                return;
            }
            if (e.getSource() == backButton) {
                turn--;
                // if turn is negative, do not uneat any squares
                if (turn < 0) {
                    return;
                }
                
                // loop through the last buttons pressed and uneat all squares on turn number
                for (int i = 0; i < yBoardSize; i++) {
                    for (int j = 0; j < xBoardSize; j++) {
                        if (board[i][j].getTurnEaten() == turn) {
                            if (i == 0 || j == 0)
                            {
                                buttons[i][j].setOpaque(true);
                                buttons[i][j].setContentAreaFilled(true);
                                buttons[i][j].setBorderPainted(true);
                                buttons[i][j].setText((j + 1) + ", " + (Math.abs(i - yBoardSize)));
                                board[i][j].unEat();
                            } else {
                                buttons[i][j].setVisible(true);
                                board[i][j].unEat();
                            }
                        }
                    }
                }
                isPlayer1Turn = !isPlayer1Turn;
                textPane.setText(isPlayer1Turn ? "Player 1's turn" : "Player 2's turn");
                window.repaint();
                return;
            }
            if (e.getSource() == startButton) {
                do {
                    try {
                        xBoardSize = Integer.parseInt(xSizeInput.getText());
                        yBoardSize = Integer.parseInt(ySizeInput.getText());
                    } catch (NumberFormatException ex) {
                        xBoardSize = -1;
                        yBoardSize = -1;
                    }
                } while (xBoardSize < 2 || yBoardSize < 2);
                window.removeAll();
                window.repaint();
                setUpBoard();
            }
            if (e.getSource() == p1Marker) {
                isP1Marker = !isP1Marker;
                // if is p1 marker, set p2 marker to false
                if (isP1Marker) {
                    isP2Marker = false;
                }
                // change color of button to show it is selected
                if (isP1Marker) {
                    p1Marker.setBackground(Color.RED);
                    p2Marker.setBackground(null);
                } else {
                    p1Marker.setBackground(Color.RED);
                    p2Marker.setBackground(Color.BLUE);
                }
            }
            if (e.getSource() == p2Marker) {
                isP2Marker = !isP2Marker;
                
                if (isP2Marker) {
                    isP1Marker = false;
                }

                if (isP2Marker) {
                    p2Marker.setBackground(Color.BLUE);
                    p1Marker.setBackground(null);
                } else {
                    p2Marker.setBackground(Color.BLUE);
                    p1Marker.setBackground(Color.RED);
                }
            }
            if (e.getSource() == switchTurnButton) {
                isPlayer1Turn = !isPlayer1Turn;
                textPane.setText(isPlayer1Turn ? "Player 1's turn" : "Player 2's turn");
            }
            if(isInButtons((JButton)e.getSource())) {
                isPlayer1Turn = !isPlayer1Turn;
                textPane.setText(isPlayer1Turn ? "Player 1's turn" : "Player 2's turn");
                JButton button = (JButton)e.getSource();
                if (isP1Marker) {
                    // toggle color of button
                    if (button.getBackground() == Color.RED) {
                        button.setBackground(null);
                    } else {
                        button.setBackground(Color.RED);
                    }
                } else if (isP2Marker) {
                    if (button.getBackground() == Color.BLUE) {
                        button.setBackground(null);
                    } else {
                        button.setBackground(Color.BLUE);
                    }
                }
                else {
                    for (int i = 0; i < yBoardSize; i++) {
                        for (int j = 0; j < xBoardSize; j++) {
                            if (button == buttons[i][j]) {
                                // eat squares above and to the right
                                for (int k = i; k >= 0; k--) {
                                    for (int l = j; l < xBoardSize; l++) {
                                        if (k == 0 || l == 0)
                                        {
                                            buttons[k][l].setOpaque(false);
                                            buttons[k][l].setContentAreaFilled(false);
                                            buttons[k][l].setBorderPainted(false);
                                            buttons[k][l].setText("");
                                            board[k][l].eat(turn);
                                        } else {
                                            buttons[k][l].setVisible(false);
                                            board[k][l].eat(turn);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                turn++;
            }
        }
    }

    public static void main(String[] args) {
        Chomp chomp = new Chomp();
        chomp.setVisible(true);
    }
}

class Node {
    // instance vars
    private boolean isEaten;
    private int turnEaten;

    public Node() {
        isEaten = false;
        turnEaten = -1;
    }

    public boolean isEaten() {
        return isEaten;
    }

    public int getTurnEaten() {
        return turnEaten;
    }

    public void unEat() {
        isEaten = false;
        turnEaten = -1;
    }

    public void eat(int turn) {
        isEaten = true;
        if (turnEaten == -1)
            turnEaten = turn;
    }
}