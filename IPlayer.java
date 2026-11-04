import java.io.IOException;

public interface IPlayer {
    Coords playTurn(Board board) throws IOException;
    default void setMark(Mark mark) { }
    default void onGameEnd(GameStatus finalStatus) { }
}