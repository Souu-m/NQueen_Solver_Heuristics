package Gui;
import solvers.DFS;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessboardGUI extends JFrame {
    private int boardSize;
    private JPanel chessboardPanel;
    private JButton solveButton;
    private JTextField sizeInputField;
    private JTextArea statsTextArea;
    private JPanel graphPanel;
    private final int SQUARE_SIZE = 50; // Constant for the size of each square on the chessboard
    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 600;
    private JComboBox<String> solverComboBox;

    public ChessboardGUI() {
        // Set up the GUI components
        this.boardSize = 8;
        chessboardPanel = new JPanel(new GridLayout(boardSize, boardSize));
        solveButton = new JButton("Solve");
        sizeInputField = new JTextField("8", 10);
        statsTextArea = new JTextArea(10, 20);
        graphPanel = new JPanel();
        String[] solverChoices = {"List","DFS", "BFS","Min_conflict","Manhattan_distance"}; // List of solver choices
        solverComboBox = new JComboBox<>(solverChoices); // Create a combo box with solver choices
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
        topPanel.add(solverComboBox); // Add the solver combo box instead of the DFS button

        JPanel rightPanel = new JPanel(new GridLayout(2, 1));
        rightPanel.add(new JScrollPane(statsTextArea));
        JPanel rightFillerPanel = new JPanel();
        rightFillerPanel.setPreferredSize(new Dimension(50, 0));


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
                    chessboardPanel.setMinimumSize(new Dimension(chessboardSize, chessboardSize));

                    // Update the chessboard with the new queens positions
                    updateChessboard(new int[boardSize]);

                    pack();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog
                            (null, "Invalid input. Please enter a positive integer.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        solverComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int size = Integer.parseInt(sizeInputField.getText());
                boolean[][] solution=new boolean[size][size];
                int[] stats=new int[3];
                long elapsedTime;
                // Get the selected solver from the combo box
                String selectedSolver = (String) solverComboBox.getSelectedItem();
                System.out.println(selectedSolver);

                // Call the appropriate solver based on the selection
                switch (selectedSolver) {
                    case "DFS":
                        solvers.DFS dfs = new solvers.DFS(size);
                        solution = dfs.getQueenPositions(size);
                        stats = dfs.getStats();
                        elapsedTime = dfs.getTime();
                        if (solution != null) {
                            int[] queens = new int[size];
                            for (int row = 0; row < size; row++) {
                                for (int col = 0; col < size; col++) {
                                    if (solution[row][col]) {
                                        queens[row] = col;
                                        break;
                                    }
                                }
                            }
                            updateChessboard(queens);
                            updateStatistics(size, stats, elapsedTime,selectedSolver);
                        } else {
                            JOptionPane.showMessageDialog(ChessboardGUI.this, "No solution found for board size " + size, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    case "BFS":
                        solvers.BFS bfs = new solvers.BFS(size);
                        solution = bfs.getQueenPositions(size);
                        stats = bfs.getStats();
                        elapsedTime = bfs.getTime();
                        if (solution != null) {
                            int[] queens = new int[size];
                            for (int row = 0; row < size; row++) {
                                for (int col = 0; col < size; col++) {
                                    if (solution[row][col]) {
                                        queens[row] = col;
                                        break;
                                    }
                                }
                            }
                            updateChessboard(queens);
                            updateStatistics(size, stats, elapsedTime,selectedSolver);
                        } else {
                            JOptionPane.showMessageDialog(ChessboardGUI.this, "No solution found for board size " + size, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    case"Min_conflict":
                        solvers.Heuristic1 h1 = new solvers.Heuristic1(size);
                        solution = h1.getQueenPositions(size);
                        stats = h1.getStats();
                        elapsedTime = h1.getTime();
                        if (solution != null) {
                            int[] queens = new int[size];
                            for (int row = 0; row < size; row++) {
                                for (int col = 0; col < size; col++) {
                                    if (solution[row][col]) {
                                        queens[row] = col;
                                        break;
                                    }
                                }
                            }
                            updateChessboard(queens);
                            updateStatistics(size, stats, elapsedTime,selectedSolver);
                        } else {
                            JOptionPane.showMessageDialog(ChessboardGUI.this, "No solution found for board size " + size, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    case"Manhattan_distance":
                        solvers.Heuristic2 h2 = new solvers.Heuristic2(size);
                        solution = h2.getQueenPositions(size);
                        stats = h2.getStats();
                        elapsedTime = h2.getTime();
                        if (solution != null) {
                            int[] queens = new int[size];
                            for (int row = 0; row < size; row++) {
                                for (int col = 0; col < size; col++) {
                                    if (solution[row][col]) {
                                        queens[row] = col;
                                        break;
                                    }
                                }
                            }
                            updateChessboard(queens);
                            updateStatistics(size, stats, elapsedTime,selectedSolver);
                        } else {
                            JOptionPane.showMessageDialog(ChessboardGUI.this, "No solution found for board size " + size, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        break;

                }
            }

            private void updateStatistics(int size,int[] stats,long elapsedTime,String solver) {
                statsTextArea.setText("Statistics:\n");
                statsTextArea.append("Nodes Generated: " + stats[0] + "\n");
                statsTextArea.append("Nodes Removed: " + stats[1] + "\n");
                if(solver.equalsIgnoreCase("BFS") || solver.equalsIgnoreCase("DFS")){
                    statsTextArea.append("Total solutions : " + stats[2] + "\n");
                }
                statsTextArea.append("Elapsed time (nano): " + elapsedTime+ "\n");
            }

        });


        // Set some properties of the main window
        setTitle("N-Queens Problem Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
    }

    /**
     * Updates the chessboard panel with the queens positions.
     *
     * @param queensPositions An array of integers representing the row positions of the queens on the chessboard.
     */
    public void updateChessboard(int[] queens) {
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
                if (j == queens[i]) {
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
        private void addSquaresToChessboardPanel() {
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    JPanel square = new JPanel();
                    square.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
                    if ((i + j) % 2 == 0) {
                        square.setBackground(Color.WHITE);
                    } else {
                        square.setBackground(Color.GRAY);
                    }
                    chessboardPanel.add(square);
                }
            }
        }
    public static void main(String[] args) {
        ChessboardGUI chessboard = new ChessboardGUI();
        chessboard.setVisible(true);
    }
}