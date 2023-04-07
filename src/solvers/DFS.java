package solvers;


public class DFS {
        private int n;
        private int cols;
        private int diags1;
        private int diags2;
        private int[] queens;

        private int[] stats;
        private long elapsedTime;
        private int solutionCount;
        private int[] solutionFound;
    private int nodesGenerated;
    private  int nodesRemoved;

        public DFS(int n) {
            this.n = n;
            this.cols = 0;
            this.diags1 = 0;
            this.diags2 = 0;
            this.queens = new int[n];
            this.nodesGenerated=0;
            this.nodesRemoved=0;
            this.stats = new int[3];
            this.elapsedTime = -1;
            this.solutionCount = 0;
            this.solutionFound =new int[n];
        }

        public int solve(int row) {

            if (row == n) {
                // found a valid solution
                solutionCount++;
                if (solutionCount == 1) {
                    solutionFound = queens.clone();
                }
            } else {
                for (int col = 0; col < n; col++) {
                    int colBit = 1 << col;
                    int diag1Bit = 1 << (row + col);
                    int diag2Bit = 1 << (row - col + n - 1);
                    if ((cols & colBit) == 0 && (diags1 & diag1Bit) == 0 && (diags2 & diag2Bit) == 0) {
                        queens[row] = col;
                        cols |= colBit;
                        diags1 |= diag1Bit;
                        diags2 |= diag2Bit;

                        solutionCount = solve(row + 1);
                        cols &= ~colBit;
                        diags1 &= ~diag1Bit;
                        diags2 &= ~diag2Bit;
                    }
                }
            }
            return solutionCount;
        }
        /*
        Les nœuds relatifs sont comptés en incrémentant la variable relativeNodes chaque fois qu'un nœud est visité.
         Les nœuds exposés sont comptés chaque fois qu'une branche ne mène pas à une solution valide,
         en incrémentant la variable exposedNodes dans le else qui suit la condition de placement de la reine.*/





        public boolean[][] getQueenPositions(int size) {
            boolean[][] positions = new boolean[size][size];
            long startTime = System.currentTimeMillis();

            if (solve(0) > 0) {

                for (int row = 0; row < size; row++) {

                    positions[row][solutionFound[row]] = true;

                }
            }
            long endTime = System.currentTimeMillis();
            elapsedTime = endTime - startTime; //nano sec
            stats[0] = nodesGenerated;
            stats[1] = nodesRemoved;
            stats[2]=solutionCount;
            return positions;
        }


        public void displaySolution() {
            System.out.println("Solution:");
            for (int row = 0; row < n; row++) {
                System.out.print(queens[row]);
            }
            System.out.println();
        }


        public int[] getStats() {

            return stats;
        }

        public long getTime() {
            return elapsedTime;
        }
    }
