package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions;

public class IncorrectQuantityException extends Exception {

    public IncorrectQuantityException(String message) {
        super(message);
    }

    public IncorrectQuantityException(String message, Throwable cause) {
        super(message, cause);
    }


}
