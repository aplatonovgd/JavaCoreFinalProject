package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects;

public class SessionNotFoundException extends Exception{

    public SessionNotFoundException(String message) {
        super("Session not found");
    }

    public SessionNotFoundException(String message, Throwable cause) {
        super("Session not found", cause);
    }


}
