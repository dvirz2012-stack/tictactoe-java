import java.util.*;
public class RandomPlayer implements IPlayer {
    private Scanner scanner = new Scanner(System.in);
    private String scannerInfo;
    private Random random = new Random();

    @Override
    public Coords playTurn(Board board) {
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
        return null;
    }

    @Override
    public void setMark(Mark mark) { 
        random = new Random(System.currentTimeMillis() + 173 * mark.ordinal());
    }

}