import java.util.Arrays;

record StreakInfo(int streakLength, Coords nextCoordsInStreak) { }

public class Board {
    public static final int N = 3;
    public static final int K = 3;

    private Mark[][] cells;

    public Board() {
        cells = new Mark[N][N];
        for (Mark[] cell : cells) {
            Arrays.fill(cell, Mark.BLANK);
        }
    }

    public Mark getMarkAt(int row, int col) {
        if(row < 0 || row >= N || col < 0 || col >= N) {
            return null;
        }
        return cells[row][col];
    }

    public void setMarkAt(Coords coords, Mark mark){
        int row = coords.getCoords(0);
        int col = coords.getCoords(1);
        cells[row][col] = mark;
    }

    public GameStatus gameStatus() {
        int blankPigeonholesCount = 0;
        StreakInfo xStreak = findLongestCompletableStreak(Mark.X);
        StreakInfo oStreak = findLongestCompletableStreak(Mark.O);

        if (xStreak != null 
            && xStreak.streakLength() == K 
            && xStreak.nextCoordsInStreak() == null){

            return GameStatus.X_VICTORY;

        } else if (oStreak != null 
            && oStreak.streakLength() == K 
            && oStreak.nextCoordsInStreak() == null){

            return GameStatus.O_VICTORY;

        }

        for (int row = 0; row < N; row++){
            for (int col = 0; col < N; col++){
                if (cells[row][col] == Mark.BLANK){
                    blankPigeonholesCount++;
                }
            }
        }

        if (blankPigeonholesCount == 0){
            return GameStatus.DRAW;
        }
        return GameStatus.IN_PROGRESS;
    }

    StreakInfo findLongestCompletableStreak(Mark mark){
        int[][] directions = {{1 , 1} , {0 , 1} , {1 , 0}, {1 , -1}};
        int longestStreak = 0;
        Coords nextCoords = null;
        boolean isCompletable = false;
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                for (int[] direction : directions) {
                    Coords blankInStreak = null;
                    int rowc = direction[0];
                    int colc = direction[1];
                    int streakLen = 0;
                    int i;
                    for (i = 0; i < K; i++) {
                        int nextRow = row + i * rowc;
                        int nextCol = col + i * colc;
                        if (nextRow < 0 || nextRow >= N || nextCol < 0 || nextCol >= N) {
                            break;
                        }
                        int currRow = row + i * rowc;
                        int currCol = col + i * colc;
                        if (cells[currRow][currCol] == mark.other()) {
                            break;
                        }
                        if (blankInStreak == null && cells[currRow][currCol] == Mark.BLANK) {
                            blankInStreak = new Coords(currRow, currCol);
                        } else if (cells[currRow][currCol] == mark)
                            streakLen++;
                    }
                    //if this streak had mark.other, or if no longer than the longest streak so far
                    if (i < K || streakLen <= longestStreak)
                        continue;
                    longestStreak = streakLen;
                    nextCoords = blankInStreak;
                }
            }
        }
        if (longestStreak == 0)
            return null;
        return new StreakInfo(longestStreak, nextCoords);
    }

    public Board clone(){

        var board = new Board();
        for (int row = 0; row < N; row++){
            for (int col = 0; col < N; col++){
                board.cells[row][col] = cells[row][col];
            }
        }
        return board;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Mark[] row : cells) {
            for (Mark mark : row) {
                hash = switch (mark) {
                    case Mark.O -> hash * 3 + 2;
                    case Mark.X -> hash * 3 + 1;
                    case Mark.BLANK -> hash * 3;
                };
            }
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Board other))
            return false;

        for (int i = 0; i < cells.length; i++){
            if (Arrays.compare(cells[i], other.cells[i]) != 0){
                return false;
            }
        }
        return true;
        //return Arrays.compare(cells, other.cells, Arrays::compare) == 0;
    }
}
