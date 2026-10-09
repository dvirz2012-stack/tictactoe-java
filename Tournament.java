class GameArguments {
    IPlayer[] players;
    String[] playerDescriptions;
    int roundsCount;
}

public class Tournament {
    public static void main(String[] args) {
        var gameArgs = parseArgs(args);
        int rounds = gameArgs.roundsCount;

        int playerOneCount = 0;
        int playerTwoCount = 0;
        for(int roundIdx = 0 ; roundIdx < rounds ; roundIdx++) {
            var gameManager = new GameManager(gameArgs.players);
            gameManager.runGame(new VoidRenderer());
            switch(GameManager.currStatus){
                case GameStatus.X_VICTORY -> playerOneCount++;
                case GameStatus.O_VICTORY -> playerTwoCount++;
                default -> { }
            }
        }

        String player1Str = gameArgs.playerDescriptions[0];
        String player2Str = gameArgs.playerDescriptions[1];
        if(player1Str.equals(player2Str)) {
            player1Str = "player 1";
            player2Str = "player 2";
        }
        System.out.printf("%s: %.2f%%, %s: %.2f%%%n",
                player1Str, 100f*playerOneCount/rounds,
                player2Str, 100f*playerTwoCount/rounds);
    }

    private static GameArguments parseArgs(String[] args) {
        //get the players and number of rounds from args
        //use the factory to get the two players. if the type is invalid, print so and exit.
        //parse the number of rounds - if an invalid positive number - print error and exit.\
        GameArguments gameArguments = new GameArguments();
        var factory = new PlayerFactory();
        IPlayer[] players = { factory.create(args[0], Mark.X), factory.create(args[1], Mark.O) };
        gameArguments.playerDescriptions = new String[]{args[0].toLowerCase(), args[1].toLowerCase()};
        gameArguments.players = players;
        try {
            gameArguments.roundsCount = Integer.parseInt(args[2]);
        } catch(NumberFormatException e) {
            System.err.println("Illegal number of rounds.");
            System.exit(1);
        }

        if (players[0] == null || players[1] == null){
            if(players[0] != null)
                System.err.println("Invalid player 2");
            else if(players[1] != null)
                System.err.println("Invalid player 1");
            else
                System.err.println("Invalid player 1 and player 2 types");
            System.exit(1);
        }
        return gameArguments;
    }
}
