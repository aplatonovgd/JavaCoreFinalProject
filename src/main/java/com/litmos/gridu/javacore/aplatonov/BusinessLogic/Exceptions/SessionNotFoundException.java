package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions;

public class SessionNotFoundException extends Exception{

    public SessionNotFoundException(String message) {
        super(message);
    }

    public SessionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }


}
