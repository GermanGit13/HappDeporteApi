package com.svalero.happDeporte.exception;

public class PlayerNotFoundException extends Exception {

    public PlayerNotFoundException() {
        super("Player not found");
    }
    public PlayerNotFoundException(String message) {
        super(message);
    }
}
