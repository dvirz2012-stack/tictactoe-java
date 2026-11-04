import java.io.IOException;
import java.util.*;
public class GameManager {
    private final IPlayer[] players;
    public static GameStatus currStatus;
    public GameManager(IPlayer[] players) {
        this.players = players;
    }

    public static void main(String[] args) throws IOException {
        IPlayer[] players = createPlayers();
        var gameStatus = new GameManager(players).runGame(new ConsoleRenderer());
        switch(gameStatus){
            case GameStatus.X_VICTORY:
                System.out.println("x won.");
                break; 
            
            case GameStatus.O_VICTORY:
                System.out.println("o won."); 
                break;
            case GameStatus.DRAW:
                System.out.println("The game has ended in a draw."); 
                break;
            case GameStatus.IN_PROGRESS:
                break;
        }
    }
    
    public GameStatus runGame(IRenderer renderer) throws IOException {
        Board board = new Board();

        Mark[] marks = new Mark[]{Mark.X , Mark.O};

        renderer.render(board);
        currStatus = GameStatus.IN_PROGRESS;
        for (int round = 0; currStatus == GameStatus.IN_PROGRESS ; round++){
            Coords coords = players[round % 2].playTurn(board);
            if(board.getMarkAt(coords.getCoords(0), coords.getCoords(1)) != Mark.BLANK) {
                System.err.printf("Player %s chose occupied coords %s", marks[round % 2], coords);
                renderer.render(board);
                System.exit(-1);
            }
            board.setMarkAt(coords, marks[round % 2]);
            renderer.render(board);
            currStatus = board.gameStatus();
        }
        for(var player : players)
            player.onGameEnd(currStatus);
        return currStatus;
    }

    private static IPlayer[] createPlayers() {
        Scanner scanner = new Scanner(System.in);
        PlayerFactory factory = new PlayerFactory();
        Mark[] marks = { Mark.X, Mark.O };
        IPlayer[] players = new IPlayer[marks.length];
        
        for (int i = 0; i < marks.length; i++) {
            Mark mark = marks[i];
            IPlayer player = null;
            while(player == null) {
                System.out.printf("Choose player type for %s (%s): ", mark, PlayerFactory.PLAYER_OPTIONS);
                String type = scanner.nextLine();
                player = factory.create(type, mark);
            }
            players[i] = player;

            player.setMark(mark);
        }
        return players;
    }
}

/*
0) store for each board and coords how many times we chose this coords for this board and calculate on game end the average
score instead of the sum
1) try to complete the hashCode func of Board
2) Implement equals & hashCode in Coords
3) add me via dan.nirel@mail.huji.ac.il
-3) win will increase score by 1 * learningWeight. draw will increase by -.5 * learning weight, loss will
increase by -1 * learningWeight
4) print in some informational manner what the player learned (see if it makes sense)
*/


/*
primitives in java: basic built in types: char, int, double, float, bool, long

private void foo(int x) {
    x = 4;
}

int y = 5;
foo(y);

private void openBox(Box box) {
    box.open();
}

Box box = new Box();
openBox(box);

 HashMap<Integer, Board>
int y = 4;
Integer x = y;//(int)
int z = x;
x = null;
z = null;
 */