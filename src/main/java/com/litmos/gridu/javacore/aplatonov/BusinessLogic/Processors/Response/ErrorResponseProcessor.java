package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Response;

import com.litmos.gridu.javacore.aplatonov.Models.BaseErrorResponseModel;

public class ErrorResponseProcessor extends AbstractErrorResponseProcessor {

    public ErrorResponseProcessor(String message, String description){
       super(message,description);
    }

    @Override
    public String getResponseBody() {
        BaseErrorResponseModel baseErrorResponseModel = new BaseErrorResponseModel(message, description);
        String responseBody = gson.toJson(baseErrorResponseModel);
        return responseBody;
    }
}
