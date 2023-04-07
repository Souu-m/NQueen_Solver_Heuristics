package Gui;
import solvers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class newww extends JFrame {
    private int boardSize;
    private JPanel chessboardPanel;
    private JComboBox<String> solverComboBox;
    private JTextField sizeInputField;
    private JTextArea statsTextArea;
    private JPanel graphPanel;
    private final int SQUARE_SIZE = 50; // Constant for the size of each square on the chessboard
    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 600;

    public newww() {
        // Set up the GUI components
        this.boardSize = 8;
        chessboardPanel = new JPanel(new GridLayout(boardSize, boardSize));
        solverComboBox = new JComboBox<>(new String[]{"DFS", "BFS", "Heuristic 1", "Heuristic 2"});
        sizeInputField = new JTextField("8", 10);
        statsTextArea = new JTextArea(10, 20);
        graphPanel = new JPanel();
        // Set the preferred size, maximum size, and minimum size of the main window
        int windowWidth = 800;
        int windowHeight = 600;
        Dimension windowSize = new Dimension(windowWidth, windowHeight);
        setPreferredSize(windowSize);
        setMaximumSize(windowSize);
        setMinimumSize(windowSize);

        // Pack the components and display the window
        pack();
        setVisible(true);
        // Add the chessboard squares to the chessboard panel
        chessboardPanel.removeAll();
        addSquaresToChessboardPanel();

        // Set the preferred size and minimum size of the chessboard panel based on the board size
        int chessboardSize = boardSize * SQUARE_SIZE;
        chessboardPanel.setPreferredSize(new Dimension(chessboardSize, chessboardSize));
        chessboardPanel.setMinimumSize(new Dimension(chessboardSize, chessboardSize));

        // Add the components to the main window
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Enter size:"));
        topPanel.add(sizeInputField);
        topPanel.add(solverComboBox);

        JPanel rightPanel = new JPanel(new GridLayout(2, 1));
        rightPanel.add(new JScrollPane(statsTextArea));
        rightPanel.add(graphPanel);

        add(chessboardPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        add(rightPanel, BorderLayout.EAST);

        sizeInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int newSize = Integer.parseInt(sizeInputField.getText());
                    if (newSize < 1) {
                        throw new IllegalArgumentException("Board size must be positive");
                    }
                    boardSize = newSize;
                    chessboardPanel.removeAll();
                    chessboardPanel.setLayout(new GridLayout(boardSize, boardSize));
                    addSquaresToChessboardPanel();

                    // Set the preferred size and minimum size of the chessboard panel based on the new board size
                    int chessboardSize = boardSize * SQUARE_SIZE;
                    chessboardPanel.setPreferredSize(new Dimension(chessboardSize, chessboardSize));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter an integer value.");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        solverComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected solver type
                String solverType = (String) solverComboBox.getSelectedItem();
                // Create a new solver object based on the selected solver type
                int size = Integer.parseInt(sizeInputField.getText());
                boolean[][] solutions = null; // initialize solutions to null
                int[] stats=null;

                switch (solverType) {
                    case "DFS":
                        DFS dfs = new DFS(size);
                        solutions = dfs.getQueenPositions(size); // get solutions using dfs
                        stats=dfs.getStats();

                        break;
                    case "BFS":
                        BFS bfs = new BFS(size);
                        solutions = bfs.getQueenPositions(size); // get solutions using bfs
                        stats=bfs.getStats();
                        break;
            /*case "Heuristic 1":
                solver = new Heuristic1Solver();
                break;
            case "Heuristic 2":
                solver = new Heuristic2Solver();
                break;*/
                    default:
                        // default case is to use dfs solver
                        dfs = new DFS(size);
                        solutions = dfs.getQueenPositions(size);
                        break;
                }

                // Update the GUI components with the solutions
                //updateStatsTextArea(stats, solutions);
                updateChessboard(solutions);
                updateStatistics(size,stats);
            }
            private void updateStatistics(int size,int[] stats) {

                statsTextArea.setText("DFS statistics:\n");
                statsTextArea.append("Exposed Nodes: " + stats[0] + "\n");
                statsTextArea.append("Relative Nodes: " + stats[1] + "\n");
                statsTextArea.append("Total solutions: " + stats[2] + "\n");
                //statsTextArea.append("Elapsed time (nano): " + elapsedTime+ "\n");
            }
        });


    }


    private void updateStatsTextArea() {
        statsTextArea.setText("");
        statsTextArea.append("Algorithm: DFS\n");
        statsTextArea.append("Board size: " + boardSize + "\n");
        statsTextArea.append("Number of solutions: 1\n");
        statsTextArea.append("Time elapsed: 0 seconds\n");
    }

    private void addSquaresToChessboardPanel() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                JPanel squarePanel = new JPanel();
                squarePanel.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
                squarePanel.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
                chessboardPanel.add(squarePanel);
            }
        }
    }

    private void updateStatsTextArea(String solverType, List<int[]> solutions) {
        StringBuilder statsBuilder = new StringBuilder();
        statsBuilder.append("Solver: ").append(solverType).append("\n");
        statsBuilder.append("Board size: ").append(boardSize).append("\n");
        statsBuilder.append("Number of solutions: ").append(solutions.size()).append("\n\n");
        for (int[] solution : solutions) {
            statsBuilder.append("Solution: ");
            for (int col : solution) {
                statsBuilder.append(col).append(" ");
            }
            statsBuilder.append("\n");
        }
        statsTextArea.setText(statsBuilder.toString());
    }

    private void updateChessboard(boolean[][] queens) {
        // Clear the chessboard panel
        chessboardPanel.removeAll();

        // Add the chessboard squares to the chessboard panel
        int squareSize = chessboardPanel.getWidth() / boardSize;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                JPanel square = new JPanel();
                square.setPreferredSize(new Dimension(squareSize, squareSize));
                square.setBackground((i + j) % 2 == 0 ? Color.white : Color.gray);

                // Add a queen icon to the square if there is a queen in that position
                if (queens[i][j]==true) {
                    ImageIcon queenIcon = new ImageIcon(getClass().getResource("LightQueen.png"));
                    Image queenImage = queenIcon.getImage();
                    Image scaledQueenImage = queenImage.getScaledInstance(squareSize-10, squareSize-10, Image.SCALE_SMOOTH);
                    queenIcon = new ImageIcon(scaledQueenImage);
                    square.add(new JLabel(queenIcon));
                }
                chessboardPanel.add(square);
            }
        }
        // Repaint the chessboard panel
        chessboardPanel.revalidate();
        chessboardPanel.repaint();
    }

    public static void main(String[] args) {
        newww chessboard = new newww();
        chessboard.setVisible(true);
    }

}