import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SudokuVisualization extends JFrame {

    private static final int GRID_SIZE = 9;
    private static final int CELL_SIZE = 60;
    private static final int BOARD_SIZE = CELL_SIZE * GRID_SIZE;

    private List<List<Integer>> grid;
    private Random random;

    private JPanel sudokuPanel;

    public SudokuVisualization() {
        setTitle("Sudoku Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        grid = new ArrayList<>();
        random = new Random();

        sudokuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawSudokuGrid(g);
                drawSudokuValues(g);
            }
        };
        sudokuPanel.setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
        add(sudokuPanel, BorderLayout.CENTER);

        JButton generateButton = new JButton("Generate Sudoku");
        generateButton.addActionListener(e -> {
            generateSudoku();
            sudokuPanel.repaint();
        });
        add(generateButton, BorderLayout.SOUTH);

        pack();
    }

    private void generateSudoku() {
        initializeGrid();
        collapse();
    }

    private void initializeGrid() {
        grid.clear();
        for (int i = 0; i < GRID_SIZE; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < GRID_SIZE; j++) {
                row.add(0);
            }
            grid.add(row);
        }
    }

    private void collapse() {
        int totalCells = GRID_SIZE * GRID_SIZE;
        int cellIndex = 0;

        while (cellIndex < totalCells) {
            int row = cellIndex / GRID_SIZE;
            int col = cellIndex % GRID_SIZE;

            if (grid.get(row).get(col) == 0) {
                List<Integer> possibleValues = getPossibleValues(row, col);
                if (!possibleValues.isEmpty()) {
                    int value = possibleValues.get(random.nextInt(possibleValues.size()));
                    grid.get(row).set(col, value);
                    cellIndex++;
                } else {
                    resetGrid();
                    cellIndex = 0;
                }
            } else {
                cellIndex++;
            }
        }
    }


    private List<Integer> getPossibleValues(int row, int col) {
        List<Integer> possibleValues = new ArrayList<>();
        for (int value = 1; value <= GRID_SIZE; value++) {
            if (isValidPlacement(row, col, value)) {
                possibleValues.add(value);
            }
        }
        return possibleValues;
    }

    private boolean isValidPlacement(int row, int col, int value) {
        return !isUsedInRow(row, value) && !isUsedInColumn(col, value) && !isUsedInBox(row, col, value);
    }

    private boolean isUsedInRow(int row, int value) {
        List<Integer> rowData = grid.get(row);
        for (int num : rowData) {
            if (num == value) {
                return true;
            }
        }
        return false;
    }

    private boolean isUsedInColumn(int col, int value) {
        for (List<Integer> rowData : grid) {
            if (rowData.get(col) == value) {
                return true;
            }
        }
        return false;
    }

    private boolean isUsedInBox(int row, int col, int value) {
        int boxRow = row - row % 3;
        int boxCol = col - col% 3;

        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (grid.get(i).get(j) == value) {
                    return true;
                }
            }
        }

        return false;
    }

    private void resetGrid() {
        grid.clear();
        initializeGrid();
    }

    private void drawSudokuGrid(Graphics g) {
        g.setColor(Color.BLACK);
        for (int i = 0; i <= GRID_SIZE; i++) {
            g.drawLine(0, i * CELL_SIZE, BOARD_SIZE, i * CELL_SIZE);
            g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, BOARD_SIZE);
        }
    }

    private void drawSudokuValues(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g.getFontMetrics();

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                int value = grid.get(i).get(j);
                if (value != 0) {
                    int x = j * CELL_SIZE + (CELL_SIZE / 2) - fm.stringWidth(Integer.toString(value)) / 2;
                    int y = i * CELL_SIZE + (CELL_SIZE / 2) + fm.getAscent() / 2;
                    g.drawString(Integer.toString(value), x, y);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SudokuVisualization sudoku = new SudokuVisualization();
            sudoku.setVisible(true);
        });
    }
}


