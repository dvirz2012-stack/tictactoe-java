public class ConsoleRenderer implements IRenderer {

    private static final String[] X_LINES = {
        "     ",
        "X   X",
        "  X  ",
        "X   X",
        "     "
    };

    private static final String[] O_LINES = {
        "     ",
        " OOO ",
        "O   O",
        " OOO ",
        "     "
    };

    private static final String[] BLANK_LINES = {
        "     ",
        "     ",
        "     ",
        "     ",
        "     "
    };

    private static final char ROW_SEPARATOR = '-';
    private static final char COL_SEPARATOR = '|';
    private Mark[][] previousMarks = new Mark[Board.N][Board.N];

    public ConsoleRenderer() {
        for (int row = 0; row < Board.N; row++) {
            for (int col = 0; col < Board.N; col++) {
                previousMarks[row][col] = Mark.BLANK;
            }
        }
    }

    @Override
    public void render(Board board) {
        int charsamount = BLANK_LINES[0].length() + 5;
        int rowsepamount = (charsamount * Board.N);
        for (int i=0; i < Board.N; i++){
            System.out.print("     " + i + "    ");
        }
        System.out.println();
        for (int row = 0; row < Board.N; row++) {
            for (int line = 0; line < X_LINES.length; line++) {
                if (line == 2) {
                    System.out.print(row);
                } else {
                    System.out.print(" ");
                }
                for (int col = 0; col < Board.N; col++) {
                    String lineToPrint = "";
                    Mark currentMark = board.getMarkAt(row, col);
                    switch (currentMark) {
                        case Mark.BLANK:
                            lineToPrint = BLANK_LINES[line];
                            break;
                        case Mark.X:
                            lineToPrint = X_LINES[line];
                            break;
                        case Mark.O:
                            lineToPrint = O_LINES[line];
                            break;
                    } 
                        if (currentMark != Mark.BLANK
                            && currentMark != previousMarks[row][col]) {
                        try {
                            Thread.sleep(50);
                        }
                        catch(InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    System.out.print("  " + lineToPrint + "  ");
                    if (col < Board.N - 1) {
                        System.out.print(COL_SEPARATOR);
                    }
                }

                System.out.println();
            }

            for (int sepchar = 0; sepchar < rowsepamount; sepchar++) {

                if (sepchar == 0){
                    System.out.print(" ");
                }
                if (sepchar % charsamount != 0 && row != Board.N-1){
                    System.out.print((ROW_SEPARATOR));
                } else if (sepchar % charsamount == 0 && sepchar != 0 && row != Board.N-1){
                    System.out.print("+");
                }
                if (sepchar == (rowsepamount)-1 && row != Board.N - 1){
                    System.out.print(ROW_SEPARATOR);
                }

            }
            System.out.println();
        }

        for (int row = 0; row < Board.N; row++) {
            for (int col = 0; col < Board.N; col++) {
                previousMarks[row][col] = board.getMarkAt(row, col);
            }
        }
    }
}

