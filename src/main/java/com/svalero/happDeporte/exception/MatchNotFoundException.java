package com.svalero.happDeporte.exception;

public class MatchNotFoundException extends Exception {

    public MatchNotFoundException() {
        super("Match not Found");
    }

    public MatchNotFoundException(String message) {
        super(message);
    }
}
