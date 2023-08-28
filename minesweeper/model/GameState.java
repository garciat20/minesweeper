package minesweeper.model;

public enum GameState {
    NOT_STARTED("Not Started"),
    IN_PROGRESS("Game in progress"),
    WON("You won!"),
    LOST("Boom! Better luck next time");

    private String str;

    private GameState(String str){
        this.str= str;
    }

    @Override
    public String toString() {
        return str;
    }
    
}
