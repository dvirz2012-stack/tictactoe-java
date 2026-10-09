public interface IPlayer {
    Coords playTurn(Board board);
    default void setMark(Mark mark) { }
    default void onGameEnd(GameStatus finalStatus) { }
}