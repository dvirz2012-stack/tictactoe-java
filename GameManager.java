import java.util.*;
public class GameManager {
    private final IPlayer[] players;
    public static GameStatus currStatus;
    public GameManager(IPlayer[] players) {
        this.players = players;
    }

    public static void main(String[] args) {
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
    
    public GameStatus runGame(IRenderer renderer) {
        Board board = new Board();

        Mark[] marks = new Mark[]{Mark.X , Mark.O};

        renderer.render(board);
        currStatus = GameStatus.IN_PROGRESS;
        for (int round = 0; currStatus == GameStatus.IN_PROGRESS ; round++){
            Coords coords = players[round % 2].playTurn(board);
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
 - 1) Streak check is buggy: should check for any streak of mark/blank indiscriminately, then the streak count is
how many of those were mark (and not blank).
1.5) play vs. heuristic and see that it makes sense (human vs. heuristic)
2) now that heuristic works: what percentage of games does Heuristic win against random? and heuristic vs. heuristic?
 -ב3) win will increase score by 1 * learningWeight. draw will increase by -.5 * learning weight, loss will
increase by -1 * learningWeight
4) print in some informational manner what the player learned (see if it makes sense)
*/