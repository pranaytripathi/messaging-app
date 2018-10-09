package com.messagingapp.exceptions;

public class InvalidMessageTypeException extends Exception {
    public InvalidMessageTypeException(String error) {
        super(error);
    }
}
