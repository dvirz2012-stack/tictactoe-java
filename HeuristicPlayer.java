import java.util.*;

public class HeuristicPlayer implements IPlayer {
    private final Mark mark;
    private final Random random = new Random();

    public HeuristicPlayer(Mark mark) { this.mark = mark; }

    @Override
    public Coords playTurn(Board board) {

        var streakInfo = board.findLongestCompletableStreak(mark);
        var differentStreakInfo = board.findLongestCompletableStreak(mark.other());

        if ((differentStreakInfo != null) && ((streakInfo == null) ||
                (differentStreakInfo.streakLength() == Board.K - 1 && streakInfo.streakLength() < Board.K - 1))){
            return differentStreakInfo.nextCoordsInStreak();
        } else if (differentStreakInfo == null && streakInfo == null){
                int numOfEmptyCells = 0;
                for (int row = 0; row < Board.N; row++) {
                    for (int col = 0; col < Board.N; col++) {
                        if (board.getMarkAt(row, col) == Mark.BLANK) {
                            numOfEmptyCells++;
                        }
                    }
                }
                int emptyCellNum = random.nextInt(numOfEmptyCells) + 1;
                for (int row = 0; row < Board.N; row++){
                    for (int col = 0; col < Board.N; col++){
                        if (board.getMarkAt(row, col) == Mark.BLANK){
                            emptyCellNum--;
                            if (emptyCellNum == 0){
                                return new Coords(row, col);
                            }
                        }
                    }
                }
        }
        return streakInfo.nextCoordsInStreak();
    }
}