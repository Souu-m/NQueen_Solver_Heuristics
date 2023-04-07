package solvers;

import java.util.*;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Heuristic1 {
    private int n;
    private PriorityQueue<State> openList;
    private Set<State> closedList;
    private int[] solutionFound;
    private int nodesGenerated;
    private  int nodesRemoved;
    private int[] stats;
    private long elapsedTime;

    public Heuristic1(int n) {
        this.n = n;
        this.openList = new PriorityQueue<>(Comparator.comparing(State::getF));
        this.closedList = new HashSet<>();
        this.elapsedTime = -1;
        this.stats = new int[2];
        this.nodesGenerated = 0;
        this.nodesRemoved = 0;
        this.solutionFound=new int[n];
    }

    private int heuristic(State state) {
        int conflicts = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (state.queens[i] == state.queens[j] || Math.abs(state.queens[i] - state.queens[j]) == j - i) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }

    private boolean isGoalState(State state) {
        return heuristic(state) == 0;
    }

    private List<State> getSuccessors(State state) {
        List<State> successors = new ArrayList<>();
        int[] conflicts = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (state.queens[i] == state.queens[j] || Math.abs(state.queens[i] - state.queens[j]) == j - i) {
                    conflicts[i]++;
                    conflicts[j]++;
                }
            }
        }
        Integer[] sortedRows = new Integer[n];
        for (int i = 0; i < n; i++) {
            sortedRows[i] = i;
        }
        Arrays.sort(sortedRows, Comparator.comparingInt(i -> -conflicts[i]));
        int row = sortedRows[0];
        for (int col = 0; col < n; col++) {
            State successor = new State(state.row + 1, state.queens.clone(), state.g + 1);
            successor.queens[row] = col;
            for (int i = 0; i < n; i++) {
                if (i == row) {
                    continue;
                }
                if (successor.queens[i] == col) {

                }
                if (Math.abs(successor.queens[i] - col) == Math.abs(i - row)) {

                }
            }
            successors.add(successor);
            nodesGenerated++;
        }
        return successors;
    }

    public void solve() {
        State initialState = new State(0, new int[n], 0);
        openList.add(initialState);

        while (!openList.isEmpty()) {
            State currentState = openList.poll();
            if (isGoalState(currentState)) {
                solutionFound = currentState.queens.clone();
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (solutionFound[i] == j) {
                            System.out.print("Q ");
                        } else {
                            System.out.print(". ");
                        }
                    }
                    System.out.println();
                }
                return;
            }
            closedList.add(currentState);
            for (State successor : getSuccessors(currentState)) {
                if (closedList.contains(successor)) {
                    nodesRemoved++;
                    continue;
                }

                successor.f = successor.g + heuristic(successor);
                openList.add(successor);
            }
        }
        System.out.println("No solution found.");
    }

    public boolean[][] getQueenPositions(int size) {
        boolean[][] positions = new boolean[size][size];

        long startTime = System.currentTimeMillis();
        solve();
        long endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime; //nano sec
        for (int i = 0; i < size; i++) {
            positions[i][solutionFound[i]] = true;
        }
        if (positions == null) {
            System.out.println("No solution found.");
        }
        stats[0]=nodesGenerated;
        stats[1]=nodesRemoved;
        System.out.println("Found solution:");
        /*for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (positions[i][j]) {
                    System.out.print("Q ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
       }*/
        return positions;
    }
    public long getTime(){
        return elapsedTime;
    }
    public int[] getStats(){
        return stats;
    }


        public void displaySolution() {
            System.out.println("Found solution:");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (solutionFound[i] == j) {
                        System.out.print("Q ");
                    } else {
                        System.out.print(". ");
                    }
                }
                System.out.println();
            }
        }
        private static class State {
            int row;
            int[] queens;
            int g;
            int f;

            State(int row, int[] queens, int g) {
                this.row = row;
                this.queens = queens;
                this.g = g;
                this.f = 0;
            }

            int getF() {
                return f;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof State)) return false;
                State state = (State) o;
                return row == state.row && g == state.g && java.util.Arrays.equals(queens, state.queens);
            }

            @Override
            public int hashCode() {
                int result = java.util.Objects.hash(row, g);
                result = 31 * result + java.util.Arrays.hashCode(queens);
                return result;
            }
        }

        public static void main (String[]args){
            Heuristic1 solver = new Heuristic1(8);
            solver.solve();
            solver.displaySolution();
            //solver.getQueenPositions(8);

        }
    }
