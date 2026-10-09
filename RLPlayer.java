import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RLPlayer implements IPlayer {
    private static final float LEARN_DECAY = .5f;

    private static record Choice(Board board, Coords coords) { }

    private final Mark mark;
    private final Map<Board, Map<Coords, Float>> moveScores;
    private final List<Choice> history = new ArrayList<>();
    private final IPlayer explorationPlayer;

    /*the class needs:
    * file that saves all scores for all moves for all boards
    * save the history of the current game
     */


    public RLPlayer(Mark mark, String prevScoresFilepath) {
        this.mark = mark;
        moveScores = new HashMap<>();
        explorationPlayer = new RandomPlayer();
    }

    @Override
    public Coords playTurn(Board board) {
        var coords = explorationPlayer.playTurn(board);
        history.add(new Choice(board, coords));
        return coords;
    }

    @Override
    public void onGameEnd(GameStatus finalStatus) {

        float learningWeight = 1f;
        for(int i = history.size() - 1 ; i >= 0 ; i--) {
            var choice = history.get(i);
            var boardChoices = moveScores.putIfAbsent(choice.board, new HashMap<>());
            assert boardChoices != null;
            var currChoiceScore = boardChoices.putIfAbsent(choice.coords, 0f);
            if (currChoiceScore == null){
                currChoiceScore = 0f;
            }
            var newScore = currChoiceScore + (learningWeight * switch (finalStatus){
                case GameStatus.O_VICTORY:
                    if (mark == Mark.X) {
                        yield -1;
                    } else {
                        yield 1;
                    }

                case GameStatus.X_VICTORY:
                    if (mark == Mark.O) {
                        yield -1;
                    } else {
                        yield 1;
                    }

                case IN_PROGRESS:
                    yield 0;

                case GameStatus.DRAW:
                    yield .5f;

            });
            boardChoices.put(choice.coords, newScore);
            learningWeight *= LEARN_DECAY;
        }
    }
}
