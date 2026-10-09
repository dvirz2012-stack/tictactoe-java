import java.util.*;

public class InputPlayer implements IPlayer {
    private Mark myMark;
    private Scanner scanner = new Scanner(System.in);
    
    public void setMark(Mark mark){
        myMark = mark;
    }

    //fix: '2' should not be in a valid input
    //fix: the user is not actually required to enter new coords
    //fix: what if the players enters invalid coords again?
    //fix: what if the player enters non-numbers??
    @Override 
    public Coords playTurn(Board board){
        while (true) {
            System.out.print(String.format("Choose Coordinates for %s: ", myMark));
            String input = scanner.next();
            int rowcol;
            try {
                rowcol = Integer.valueOf(input);
            } catch (NumberFormatException exception) {
                System.out.println("Enter integers only.");
                continue;
            }
            int row = rowcol / 10, col = rowcol % 10;
            if (input.length() != 2 
                    || input.charAt(0) < '0'                 
                    || input.charAt(0) > '9'
                    || input.charAt(1) < '0'
                    || input.charAt(1) > '9'){
                if (rowcol <= Board.N){
                    System.out.print("Invalid coordinates, did you mean 0" + rowcol + "? (y/n): ");
                    while (true) {
                        input = scanner.next();
                        if (input.equals("y")){
                            row = 0;
                            return new Coords(row, rowcol);
                        } else if (input.equals("n")){
                            break;
                        } else {
                            System.out.print("write only y / n: ");
                        }
                    }
                    continue;
                }    
            }

            if ((row >= Board.N || row < 0) || (col >= Board.N || col < 0)){
                System.out.print("Invalid coordinates, try again. ");
                continue;
            }
            
            if (board.getMarkAt(row, col) != Mark.BLANK){
                System.out.print("Coordinates have already been took, Try again: ");
                continue;
            } else {
                return new Coords(row, col);
            }
        }
    }
}

