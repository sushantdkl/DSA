// A Maze Solver
// Functionality:
//  Graph (Adjacency List/Matrix): Use a graph to represent the maze where each cell is a node
// connected to its adjacent cells.
//  Stack (DFS) / Queue (BFS): Use a stack for Depth-First Search (DFS) or a queue for Breadth-First
// Search (BFS) to find a path from start to finish.
// GUI:
//  A grid-based maze where each cell is a node.
//  A start and end point for the player or algorithm.
//  Buttons to solve the maze using DFS or BFS.
//  A "Generate New Maze" button to create a randomized maze.
// Implementation:
// Initialization:
// 1. Generate a random maze using a grid where walls block movement.
// 2. Represent the maze as a graph (adjacency list or matrix).
// 3. Allow the user to choose a start and end point.
// 4. Display the maze in the GUI.
// Solving the Maze:
// While the path is not found:
// 1. Choose an algorithm:
// o If DFS is selected, use a stack.
// o If BFS is selected, use a queue.
// 2. Explore adjacent nodes:
// o If a path is found, mark it.
// o If a dead-end is reached, backtrack.
// 3. Highlight the solution path on the GUI.
// Game Over:
//  If the end point is reached, display a success message.
//  If no path exists, display a failure message.
// Data Structures:
//  Graph: Represent the maze where each cell is a node connected to adjacent walkable cells.
//  Queue: Used for BFS to find the shortest path.
//  Stack: Used for DFS to explore paths.
//  2D Array: Represents the grid-based maze.
// Additional Considerations:
//  Random Maze Generation: Use algorithms like Prim's or Recursive Backtracking to generate
// new mazes dynamically.
//  User Input: Allow the player to manually navigate through the maze.
//  Path Animation: Visually show the algorithm exploring paths.
//  Scoring System: Award points based on efficiency (steps taken, time, etc.).

package Question5;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

class Cell {
    int row, col;
    boolean isWall, visited, inPath, isStart, isEnd;
    Cell parent;
    int distance; // for BFS shortest path

    public Cell(int row, int col, boolean isWall) {
        this.row = row;
        this.col = col;
        this.isWall = isWall;
        this.visited = false;
        this.inPath = false;
        this.isStart = false;
        this.isEnd = false;
        this.parent = null;
        this.distance = Integer.MAX_VALUE;
    }
}

class MazePanel extends JPanel {
    Cell[][] maze;
    List<Cell> path = new ArrayList<>();
    List<Cell> exploredCells = new ArrayList<>();
    int rows = 20, cols = 20, cellSize = 25;
    Cell startCell, endCell;
    boolean isSolving = false;
    long startTime;
    int stepsTaken = 0;

    public MazePanel() {
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        setBorder(BorderFactory.createTitledBorder("🧩 Maze Solver"));
        initializeMaze();
    }
    
    private void initializeMaze() {
        generateMaze();
        setupMouseListeners();
    }

    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isSolving) return;
                
                int col = e.getX() / cellSize;
                int row = e.getY() / cellSize;
                
                if (row >= 0 && row < rows && col >= 0 && col < cols) {
                    Cell clickedCell = maze[row][col];
                    if (!clickedCell.isWall) {
                        if (startCell == null) {
                            // Set start point
                            if (startCell != null) startCell.isStart = false;
                            startCell = clickedCell;
                            clickedCell.isStart = true;
                            clickedCell.isEnd = false;
                        } else if (endCell == null || endCell == startCell) {
                            // Set end point
                            if (endCell != null) endCell.isEnd = false;
                            endCell = clickedCell;
                            clickedCell.isEnd = true;
                            clickedCell.isStart = false;
                        } else {
                            // Reset selection
                            if (startCell != null) startCell.isStart = false;
                            if (endCell != null) endCell.isEnd = false;
                            startCell = clickedCell;
                            clickedCell.isStart = true;
                            endCell = null;
                        }
                        repaint();
                    }
                }
            }
        });
    }

    public void generateMaze() {
        maze = new Cell[rows][cols];
        
        // Initialize with walls
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                maze[r][c] = new Cell(r, c, true);
            }
        }
        
        // Generate maze using Recursive Backtracking
        generateMazeRecursive(1, 1);
        
        // Ensure start and end are accessible
        maze[1][1].isWall = false;
        maze[rows-2][cols-2].isWall = false;
        
        // Set default start and end
        startCell = maze[1][1];
        endCell = maze[rows-2][cols-2];
        startCell.isStart = true;
        endCell.isEnd = true;
        
        clearPath();
        repaint();
    }

    private void generateMazeRecursive(int row, int col) {
        maze[row][col].isWall = false;
        
        // Directions: up, right, down, left
        int[][] directions = {{-2, 0}, {0, 2}, {2, 0}, {0, -2}};
        List<Integer> dirList = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(dirList);
        
        for (int dir : dirList) {
            int newRow = row + directions[dir][0];
            int newCol = col + directions[dir][1];
            
            if (newRow > 0 && newRow < rows-1 && newCol > 0 && newCol < cols-1 && maze[newRow][newCol].isWall) {
                // Carve path
                maze[row + directions[dir][0]/2][col + directions[dir][1]/2].isWall = false;
                generateMazeRecursive(newRow, newCol);
            }
        }
    }

    private List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        for (int i = 0; i < 4; i++) {
            int nr = cell.row + dr[i];
            int nc = cell.col + dc[i];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !maze[nr][nc].isWall)
                neighbors.add(maze[nr][nc]);
        }
        return neighbors;
    }

    public void solveDFS() {
        if (startCell == null || endCell == null) {
            JOptionPane.showMessageDialog(this, "Please set start and end points first!");
            return;
        }
        
        clearPath();
        isSolving = true;
        startTime = System.currentTimeMillis();
        stepsTaken = 0;
        
        // Reset all cells
        for (Cell[] row : maze) {
            for (Cell cell : row) {
                cell.visited = false;
                cell.parent = null;
            }
        }

        Stack<Cell> stack = new Stack<>();
        startCell.visited = true;
        startCell.distance = 0;
        stack.push(startCell);

        javax.swing.Timer timer = new javax.swing.Timer(50, e -> {
            if (stack.isEmpty()) {
                ((javax.swing.Timer)e.getSource()).stop();
                isSolving = false;
                showResult(false);
                return;
            }

            Cell current = stack.pop();
            stepsTaken++;
            exploredCells.add(current);

            if (current == endCell) {
                ((javax.swing.Timer)e.getSource()).stop();
                isSolving = false;
                buildPath();
                showResult(true);
                return;
            }

            for (Cell neighbor : getNeighbors(current)) {
                if (!neighbor.visited) {
                    neighbor.visited = true;
                    neighbor.parent = current;
                    neighbor.distance = current.distance + 1;
                    stack.push(neighbor);
                }
            }
            repaint();
        });
        timer.start();
    }

    public void solveBFS() {
        if (startCell == null || endCell == null) {
            JOptionPane.showMessageDialog(this, "Please set start and end points first!");
            return;
        }
        
        clearPath();
        isSolving = true;
        startTime = System.currentTimeMillis();
        stepsTaken = 0;

        // Reset all cells
        for (Cell[] row : maze) {
            for (Cell cell : row) {
                cell.visited = false;
                cell.parent = null;
                cell.distance = Integer.MAX_VALUE;
            }
        }

        Queue<Cell> queue = new LinkedList<>();
        startCell.visited = true;
        startCell.distance = 0;
        queue.offer(startCell);

        javax.swing.Timer timer = new javax.swing.Timer(50, e -> {
            if (queue.isEmpty()) {
                ((javax.swing.Timer)e.getSource()).stop();
                isSolving = false;
                showResult(false);
                return;
            }

            Cell current = queue.poll();
            stepsTaken++;
            exploredCells.add(current);

            if (current == endCell) {
                ((javax.swing.Timer)e.getSource()).stop();
                isSolving = false;
                buildPath();
                showResult(true);
                return;
            }

            for (Cell neighbor : getNeighbors(current)) {
                if (!neighbor.visited) {
                    neighbor.visited = true;
                    neighbor.parent = current;
                    neighbor.distance = current.distance + 1;
                    queue.offer(neighbor);
                }
            }
            repaint();
        });
        timer.start();
    }

    private void buildPath() {
        path.clear();
        for (Cell c = endCell; c != null; c = c.parent) {
            c.inPath = true;
            path.add(c);
        }
        Collections.reverse(path);
    }

    public void clearPath() {
        path.clear();
        exploredCells.clear();
        for (Cell[] row : maze) {
            for (Cell cell : row) {
                cell.visited = false;
                cell.inPath = false;
                cell.parent = null;
            }
        }
    }

    private void showResult(boolean success) {
        long timeTaken = System.currentTimeMillis() - startTime;
        String message;
        
        if (success) {
            double score = calculateScore(timeTaken, stepsTaken, path.size());
            message = """
                🎉 SUCCESS!
                
                Path Length: %d steps
                Steps Explored: %d
                Time Taken: %.2f seconds
                Score: %.1f/100""".formatted(path.size(), stepsTaken, timeTaken/1000.0, score);
        } else {
            message = """
                ❌ FAILURE!
                
                No path found between start and end points.""";
        }
        
        JOptionPane.showMessageDialog(this, message, 
            success ? "Maze Solved!" : "No Solution", 
            success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }

    private double calculateScore(long timeTaken, int stepsExplored, int pathLength) {
        // Score based on efficiency: shorter path, fewer steps explored, faster time
        double timeScore = Math.max(0, 100 - (timeTaken / 100.0));
        double efficiencyScore = Math.max(0, 100 - (stepsExplored - pathLength) * 2);
        double pathScore = Math.max(0, 100 - pathLength * 5);
        
        return (timeScore + efficiencyScore + pathScore) / 3.0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = maze[r][c];
                int x = c * cellSize;
                int y = r * cellSize;

                if (cell.isWall) {
                    g2d.setColor(Color.DARK_GRAY);
                } else if (cell.isStart) {
                    g2d.setColor(Color.GREEN);
                } else if (cell.isEnd) {
                    g2d.setColor(Color.RED);
                } else if (cell.inPath) {
                    g2d.setColor(Color.YELLOW);
                } else if (cell.visited) {
                    g2d.setColor(new Color(200, 200, 255));
                } else {
                    g2d.setColor(Color.WHITE);
                }

                g2d.fillRect(x, y, cellSize, cellSize);
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawRect(x, y, cellSize, cellSize);

                // Draw labels
                if (cell.isStart) {
                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font("Arial", Font.BOLD, 12));
                    g2d.drawString("S", x + 8, y + 16);
                } else if (cell.isEnd) {
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Arial", Font.BOLD, 12));
                    g2d.drawString("E", x + 8, y + 16);
                }
            }
        }

        // Draw legend
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        g2d.drawString("Legend:", 10, rows * cellSize + 15);
        
        g2d.setColor(Color.GREEN);
        g2d.fillRect(60, rows * cellSize + 5, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Start", 80, rows * cellSize + 16);
        
        g2d.setColor(Color.RED);
        g2d.fillRect(120, rows * cellSize + 5, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.drawString("End", 140, rows * cellSize + 16);
        
        g2d.setColor(Color.YELLOW);
        g2d.fillRect(160, rows * cellSize + 5, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Path", 180, rows * cellSize + 16);
        
        g2d.setColor(new Color(200, 200, 255));
        g2d.fillRect(200, rows * cellSize + 5, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Explored", 220, rows * cellSize + 16);
    }
}

public class Question5a extends JFrame {
    MazePanel mazePanel = new MazePanel();
    JLabel statusLabel = new JLabel("Click to set start (green) and end (red) points, then solve!");

    public Question5a() {
        super("🧩 Maze Solver - DFS & BFS Algorithms");

        // Create control buttons
        JButton dfsBtn = new JButton("🔍 Solve with DFS");
        JButton bfsBtn = new JButton("🔍 Solve with BFS");
        JButton resetBtn = new JButton("🔄 Generate New Maze");
        JButton clearBtn = new JButton("🧹 Clear Path");

        // Style buttons
        dfsBtn.setBackground(new Color(100, 200, 100));
        bfsBtn.setBackground(new Color(100, 150, 255));
        resetBtn.setBackground(new Color(255, 200, 100));
        clearBtn.setBackground(new Color(200, 200, 200));

        // Add action listeners
        dfsBtn.addActionListener(_ -> mazePanel.solveDFS());
        bfsBtn.addActionListener(_ -> mazePanel.solveBFS());
        resetBtn.addActionListener(_ -> mazePanel.generateMaze());
        clearBtn.addActionListener(_ -> {
            mazePanel.clearPath();
            mazePanel.repaint();
        });

        // Layout
        JPanel controls = new JPanel(new FlowLayout());
        controls.add(dfsBtn);
        controls.add(bfsBtn);
        controls.add(resetBtn);
        controls.add(clearBtn);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(mazePanel, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        add(controls, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Question5a::new);
    }
}
