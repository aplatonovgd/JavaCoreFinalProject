package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions;

public class IncorrectContentTypeException extends Exception{

    public IncorrectContentTypeException(String message) {
        super(message);
    }

    public IncorrectContentTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
