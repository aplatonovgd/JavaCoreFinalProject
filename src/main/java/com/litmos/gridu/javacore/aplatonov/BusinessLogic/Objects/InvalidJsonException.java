package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects;

public class InvalidJsonException extends Exception{

    public InvalidJsonException(String message) {
        super("Invalid JSON");
    }

    public InvalidJsonException(String message, Throwable cause) {
        super("Invalid JSON", cause);
    }
}
