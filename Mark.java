public enum Mark {
    BLANK, X, O;

    public Mark other() {
        return switch(this) {
            case BLANK -> BLANK;
            case X -> O;
            case O -> X;
        };
    }
}
