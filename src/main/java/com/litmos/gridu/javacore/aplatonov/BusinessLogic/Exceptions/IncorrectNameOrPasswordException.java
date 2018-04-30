package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions;

public class IncorrectNameOrPasswordException extends Exception{

    public IncorrectNameOrPasswordException(String message) {
        super("Incorrect login or password");
    }

    public IncorrectNameOrPasswordException(String message, Throwable cause) {
        super("Incorrect login or password", cause);
    }

}
