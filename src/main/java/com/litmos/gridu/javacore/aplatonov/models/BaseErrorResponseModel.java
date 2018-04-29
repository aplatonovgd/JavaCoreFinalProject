package com.litmos.gridu.javacore.aplatonov.models;

public class BaseErrorResponseModel {

    private String message;
    private String description;

    public BaseErrorResponseModel(String message, String description){
        this.message = message;
        this.description = description;
    }
}
