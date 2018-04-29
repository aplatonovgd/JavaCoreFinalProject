package com.litmos.gridu.javacore.aplatonov.models;

public class ValidationResultModel {
    private boolean success;
    private String message;
    private String description;


    public ValidationResultModel(boolean isSuccess){
        this.success = isSuccess;
    }

    public ValidationResultModel(boolean isSuccess, String message, String description){
        this.success = isSuccess;
        this.message = message;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
