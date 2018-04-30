package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions;

public class ItemNotfoundException extends  Exception{

    public ItemNotfoundException(String message) {
        super(message);
    }

    public ItemNotfoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
