package com.svalero.happDeporte.exception;

public class ClothesNotFoundException extends Exception {

    public ClothesNotFoundException() {
        super("Clothes not found");
    }
    public ClothesNotFoundException(String message) {
        super(message);
    }
}
