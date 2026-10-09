record StreakInfo(int streakLength, Coords nextCoordsInStreak) { }

public class Board {
    public static final int N = 5;
    public static final int K = 3;

    private Mark[][] cells;

    public Board() {
        cells = new Mark[N][N];
        for (int row = 0; row< cells.length; row++){
            for (int col = 0; col<cells[row].length;col++){
                cells[row][col] = Mark.BLANK;
            }
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

        for (int row = 0; row < N; row++){
            for (int col = 0; col < N; col++){
                if (cells[row][col] == mark.other()) {
                    continue;
                }
                for (int[] direction : directions) {
                    int rowc = direction[0];
                    int colc = direction[1];
                    int streakLen = 0;
                    for (int i = 0; i < K; i++) {
                        int nextRow = row + i * rowc;
                        int nextCol = col + i * colc;
                        if (nextRow < 0 || nextRow >= N || nextCol < 0 || nextCol >= N) {
                            break;
                        }
                            int currRow = row + i * rowc;
                            int currCol = col + i * colc;
                            if (cells[currRow][currCol] != mark) {
                                break;
                            }

                        streakLen++;
                    }
                    //TODO: streak is only relevant if it is fully completable
                    for (int leftForStreak = streakLen; leftForStreak < K; leftForStreak++) {

                        int nextRow = row + leftForStreak * rowc;
                        int nextCol = col + leftForStreak * colc;
                        if (!(nextRow < 0 || nextRow >= N || nextCol < 0 || nextCol >= N) && cells[nextRow][nextCol] == Mark.BLANK) {

                            isCompletable = true;

                        } else {

                            isCompletable = false;
                            break;

                        }

                    }
                    int nextRow = row + streakLen * rowc;
                    int nextCol = col + streakLen * colc;
                    if (streakLen > longestStreak && streakLen < K
                            && nextRow >= 0 && nextRow < N
                            && nextCol >= 0 && nextCol < N
                            && cells[nextRow][nextCol] == Mark.BLANK
                            && isCompletable) {
                        longestStreak = streakLen;
                        nextCoords = new Coords(nextRow, nextCol);
                    } else if (streakLen == K) {
                        longestStreak = streakLen;
                        nextCoords = null;
                        return new StreakInfo(longestStreak, nextCoords);
                    } else {
                        continue;
                    }

                }
            }
        }
        if (longestStreak == 0){
            return null;
        }
        return new StreakInfo(longestStreak, nextCoords);
    }
}
