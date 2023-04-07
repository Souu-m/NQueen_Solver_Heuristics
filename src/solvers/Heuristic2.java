package solvers;
import java.util.*;

public class Heuristic2 {
    private int n;
    private int[] queens; // queens[i] is the column number of the queen in row i
    private PriorityQueue<State> queue;

    private long elapsedTime;
    private int nodesGenerated;
    private  int nodesRemoved;
    private int[] stats;

    public long getTime() {
        return elapsedTime;
    }
    public int[] getStats(){
        return stats;
    }

    private class State implements Comparable<State> {
        int[] queens;
        int gScore, hScore;

        public State(int[] queens, int gScore) {
            this.queens = queens;
            this.gScore = gScore;
            this.hScore = computeHeuristic(queens);

        }

        public int compareTo(State other) {
            return (gScore + hScore) - (other.gScore + other.hScore);
        }
    }

    public Heuristic2(int n) {
        this.n = n;
        this.queens = new int[n];
        this.queue = new PriorityQueue<>();
        this.elapsedTime=-1;
        this.nodesGenerated = 0;
        this.nodesRemoved = 0;
        this.stats=new int[2];
    }

    private int computeHeuristic(int[] queens) {
        int h = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (queens[i] == queens[j] || Math.abs(queens[i] - queens[j]) == j - i) {
                    h++;
                }
            }
        }
        return h;
    }

    private boolean isGoalState(int[] queens) {
        return computeHeuristic(queens) == 0;
    }

    private void enqueueSuccessors(State state) {
        int[] queens = state.queens;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (queens[row] == col) continue;
                int[] successorQueens = Arrays.copyOf(queens, n);
                successorQueens[row] = col;
                nodesGenerated++;
                queue.offer(new State(successorQueens, state.gScore + 1));

            }
        }
    }

    public void solve() {
        queue.offer(new State(queens, 0));
        while (!queue.isEmpty()) {
            State state = queue.poll();
            queens = state.queens;
            nodesRemoved++;
            if (isGoalState(queens)) {

                return;
            }
            enqueueSuccessors(state);
        }
    }
    public boolean[][] getQueenPositions(int size) {
        boolean[][] positions = new boolean[size][size];

        long startTime = System.currentTimeMillis();
        solve();
        long endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime; //nano sec
        stats[0]=nodesGenerated;
        stats[1]=nodesRemoved;
        for (int i = 0; i < size; i++) {
            positions[i][queens[i]] = true;
        }
        System.out.println("Nodes Generated: " + stats[0] + "\n");
        System.out.println("Nodes Removed: " + stats[1] + "\n");
        System.out.println("Elapsed time: " + elapsedTime + "\n");
        return positions;
    }

    public static void main (String[]args){
        int n=40;
        Heuristic2 solver = new Heuristic2(n);
        boolean[][] solution = solver.getQueenPositions(n);

    }
}
