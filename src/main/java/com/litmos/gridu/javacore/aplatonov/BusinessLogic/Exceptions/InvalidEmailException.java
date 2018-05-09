package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions;

public class InvalidEmailException extends Exception{

    public InvalidEmailException(String message) {
        super(message);
    }

    public InvalidEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
