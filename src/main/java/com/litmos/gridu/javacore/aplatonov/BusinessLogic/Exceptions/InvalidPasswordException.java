package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions;

public class InvalidPasswordException extends InvalidCredentialsException{

    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
