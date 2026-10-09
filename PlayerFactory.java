public class PlayerFactory {
    public static final String PLAYER_OPTIONS = "Input / Random / Heuristic";

    public IPlayer create(String playerType, Mark mark) {
        return switch (playerType.toLowerCase()) {
            case "input" -> new InputPlayer();
            case "random" -> new RandomPlayer();
            case "heuristic" -> new HeuristicPlayer(mark);
            default -> null;
        };
    }
}