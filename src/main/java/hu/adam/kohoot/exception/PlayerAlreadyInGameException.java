package hu.adam.kohoot.exception;

public class PlayerAlreadyInGameException extends Exception {
    public PlayerAlreadyInGameException(String message) {
        super(message);
    }
}
