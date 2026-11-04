import util.FloatPair;
import util.MathExtension;
import com.google.gson.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

record Info(int boardHash, int row, int col, Float avgTemp) {

    public String showMoveInfo() {

        if (avgTemp > 0)
            return "[ Move On: row " + row + " column " + col + " ] On board " + boardHash + ": resulted in a win. Score: " + avgTemp;
        else if (avgTemp < 0)
            return "[ Move On: row " + row + " column " + col + " ] On board " + boardHash + ": resulted in a loss or draw. Score: " + avgTemp;

        return "";

    }

}

public class RLPlayer implements IPlayer {

    //==== types ====
    private static record Choice(Board board, Coords coords) { }

    //==== constants ====
    private static final float LEARN_DECAY = .5f;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    //==== private fields ====
    private final Mark mark;
    private final Map<Board, Map<Coords, FloatPair>> moveScores;
    private final List<Choice> history = new ArrayList<>();
    private final Map<Integer, Integer> timesPlayed;
    private final IPlayer explorationPlayer;
    private final Random random = new Random();
    private final String prevScoresFilepath;

    /*the class needs:
    * file that saves all scores for all moves for all boards
    * save the history of the current game
     */

    //==== public methods =====

    public RLPlayer(Mark mark) {
        this.mark = mark;
        moveScores = new HashMap<>();
        timesPlayed = new HashMap<>();
        explorationPlayer = new RandomPlayer();
        prevScoresFilepath = "scores.json";
    }

    @Override
    public Coords playTurn(Board board) throws IOException {
        //count how many times we played this board total
        //the explorationChance is lerp between 1, 0 where
        //t01 = gamesPlayed / 1000*Board.N
        //see how it performs: against itself (is it approx. 50/50?)
        //performance vs. random and heuristic
        //how many rounds needed to achieve 65% against random?
        //how many against heuristic - if at all possible?
        //try to add the file to store our progress.
        int currCount = timesPlayed.getOrDefault(board.hashCode(), 0);
        timesPlayed.put(board.hashCode(), currCount+1);
        float explorationChance = MathExtension.lerp(1, 0, (float) (currCount + 1) / (1000 * Board.N));////
        boolean exploreOrExploit = random.nextFloat() < explorationChance;
        Coords coords;
        if(exploreOrExploit)
            coords = explorationPlayer.playTurn(board);
        else
            coords = chooseBestMove(board);
        history.add(new Choice(board.clone(), coords));

        ////Board b = new Board();
        ////b.setMarkAt(new Coords(0, 0), Mark.X);
        ////b.setMarkAt(new Coords(0, 2), Mark.O);
        ////b.setMarkAt(new Coords(2, 2), Mark.O);
        ////b.setMarkAt(new Coords(1, 1), Mark.X);
        ////b.setMarkAt(new Coords(1, 0), Mark.X);
        ////if (board.equals(b)){
        ////    int x = 3;
        ////}


        return coords;

    }


    @Override
    public void onGameEnd(GameStatus finalStatus) {
        float learningWeight = 1f;
        for(int i = history.size() - 1 ; i >= 0 ; i--) {
            var choice = history.get(i);
            var row = choice.coords.getCoords(0);
            var col = choice.coords.getCoords(1);
            var boardHash = choice.board.hashCode();
            var boardChoices = moveScores.putIfAbsent(choice.board, new HashMap<>());
            if(boardChoices == null)
                boardChoices = moveScores.get(choice.board);
            var currChoiceScore = boardChoices.putIfAbsent(choice.coords, new FloatPair(0f, 0f));

            if (currChoiceScore == null){
                currChoiceScore = new FloatPair(0f, 0f);
            }
            var contributionToScore = 0f;
            boolean isWin = (finalStatus == GameStatus.O_VICTORY && mark == Mark.O) ||
                    (finalStatus == GameStatus.X_VICTORY && mark == Mark.X);
            boolean isLose = !isWin && finalStatus != GameStatus.DRAW;
            if(isWin)
                contributionToScore = 1f * learningWeight;
            else if(isLose) //then we lost
                contributionToScore = -1f * learningWeight;
            else //then draw
                contributionToScore = -.5f * learningWeight;
            var floatPairForSumming = new FloatPair(currChoiceScore.num1() + contributionToScore
                    , currChoiceScore.num2() + 1);
            boardChoices.put(choice.coords, floatPairForSumming);
            Float avgTemp = floatPairAvg(floatPairForSumming);
            learningWeight *= LEARN_DECAY;
            Info latestMoveInfo = new Info(boardHash, row, col, avgTemp);
        }
        try (FileWriter writer = new FileWriter(prevScoresFilepath)){
            GSON.toJson(history, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        history.clear();
    }

    //==== private methods =====
    private Coords chooseBestMove(Board board) throws IOException {
        var movesForBoard = moveScores.getOrDefault(board, null);
        if (movesForBoard == null) {
            return explorationPlayer.playTurn(board);
        }
        Coords bestMove = null;
        float bestMoveScore = Float.NEGATIVE_INFINITY;
        for (Coords c : movesForBoard.keySet()) {
            var currMoveAvg = floatPairAvg(movesForBoard.get(c));
            if (bestMoveScore < currMoveAvg) {
                bestMoveScore = currMoveAvg;
                bestMove = c;
            }
        }
        return bestMove;
    }

    private static float floatPairAvg(FloatPair pair) { return pair.num1() / pair.num2(); }
}
