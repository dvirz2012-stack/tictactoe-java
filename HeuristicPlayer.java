import java.io.IOException;
import java.util.*;

public class HeuristicPlayer implements IPlayer {
    private final Mark mark;
    private final Random random = new Random();
    private final IPlayer randomPlayer = new RandomPlayer();
    public HeuristicPlayer(Mark mark) { this.mark = mark; }

    @Override
    public Coords playTurn(Board board) throws IOException {

        var streakInfo = board.findLongestCompletableStreak(mark);
        var differentStreakInfo = board.findLongestCompletableStreak(mark.other());

        if ((differentStreakInfo != null) && ((streakInfo == null) ||
                (differentStreakInfo.streakLength() == Board.K - 1 && streakInfo.streakLength() < Board.K - 1))){
            return differentStreakInfo.nextCoordsInStreak();
        } else if (differentStreakInfo == null && streakInfo == null){
            return randomPlayer.playTurn(board);
        }
        return streakInfo.nextCoordsInStreak();
    }
}