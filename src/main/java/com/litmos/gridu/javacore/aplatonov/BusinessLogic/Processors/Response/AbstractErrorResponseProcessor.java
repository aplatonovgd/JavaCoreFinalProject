package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Response;

import com.google.gson.Gson;

public abstract class AbstractErrorResponseProcessor implements BaseResponseProcessor {

    protected String message;
    protected String description;
    protected Gson gson = new Gson();

    public AbstractErrorResponseProcessor(String message, String description){
        this.message = message;
        this.description = description;
    }

    @Override
    public abstract String getResponseBody();
}
