package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions;

public class EmptyCartException extends Exception {

    public EmptyCartException(String message) {
        super(message);
    }

    public EmptyCartException(String message, Throwable cause) {
        super(message, cause);
    }

}
