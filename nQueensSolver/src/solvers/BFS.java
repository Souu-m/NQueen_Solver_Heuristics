package solvers;
import java.util.*;

import java.util.*;

public class BFS {
    private int n;
    private int[] queens;
    private Queue<int[]> queue;
    private int solutionCount;
    private boolean firstSolutionFound;
    private int exposedNodes;
    private int relativeNodes;
    private long elapsedTime;
    private int[] stats;
    private int nodesGenerated;
    private  int nodesRemoved;
    public BFS(int n) {
        this.n = n;
        this.queens = new int[n];
        this.queue = new LinkedList<>();
        this.solutionCount = 0;
        this.firstSolutionFound = false;
        this.nodesGenerated=0;
        this.nodesRemoved=0;
        this.elapsedTime=-1;
        this.stats=new int[3];
    }

    public void solve() {
        queue.offer(new int[0]); // Ajouter une configuration initiale vide

        while (!queue.isEmpty()) {
            int[] config = queue.poll();
            nodesGenerated++;
            if (config.length == n) { // Une solution a été trouvée
                solutionCount++;
                if (!firstSolutionFound) {
                    firstSolutionFound = true;
                    queens = config;

                }
            } else {
                for (int col = 0; col < n; col++) {
                    int row = config.length;
                    boolean isValid = true;
                    for (int i = 0; i < row; i++) {
                        if (col == config[i] || Math.abs(col - config[i]) == row - i) {
                            isValid = false; // Cette colonne n'est pas valide
                            nodesRemoved++;
                            break;
                        }
                    }
                    if (isValid) {
                        int[] nextConfig = Arrays.copyOf(config, row + 1);
                        nextConfig[row] = col;
                        queue.offer(nextConfig); // Ajouter la configuration suivante à la queue
                    }
                    nodesGenerated++;
                }
            }

        }
    }

    public void displaySolution() {
        if (solutionCount == 0) {
            System.out.println("No solution found.");
        } else {
            System.out.println("Number of solutions: " + solutionCount);
            System.out.println("Solution:");
            for (int row = 0; row < n; row++) {
                for (int col = 0; col < n; col++) {
                    if (queens[row] == col) {
                        System.out.print("Q ");
                    } else {
                        System.out.print(". ");
                    }
                }
                System.out.println();
            }

        }
    }
    public boolean[][] getQueenPositions(int size) {
        boolean[][] positions = new boolean[size][size];
        long startTime = System.currentTimeMillis();

        solve();
        long endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime; //nano sec
            for (int row = 0; row < size; row++) {

                positions[row][queens[row]] = true;

            }
        stats[0] = nodesGenerated;
        stats[1] = nodesRemoved;
        stats[2]=solutionCount;

        return positions;
    }
    public int[] getStats() {

        return stats;
    }
    public long getTime() {
        return elapsedTime;
    }
    public static void main(String[] args) {
        int n = 12;
        BFS solver = new BFS(n);
        solver.solve();
        solver.displaySolution();
    }
}
