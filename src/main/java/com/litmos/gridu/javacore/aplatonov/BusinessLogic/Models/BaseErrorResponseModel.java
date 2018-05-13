package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models;

import com.google.gson.annotations.Expose;

public class BaseErrorResponseModel {

    @Expose
    private String message;
    @Expose
    private String description;

    public BaseErrorResponseModel(String message, String description){
        this.message = message;
        this.description = description;
    }
}
