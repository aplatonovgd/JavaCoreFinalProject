package com.litmos.gridu.javacore.aplatonov.Models;

import com.google.gson.annotations.Expose;

public class ValidationResultModel {

    @Expose
    private boolean success;
    @Expose
    private String message;
    @Expose
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
