package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions;

public class InvalidCredentialsException extends  Exception{

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

}
