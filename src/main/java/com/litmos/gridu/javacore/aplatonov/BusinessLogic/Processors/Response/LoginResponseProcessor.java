package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Response;

import com.google.gson.Gson;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models.LoginResponseModel;

public class LoginResponseProcessor implements BaseResponseProcessor{

    private String sessionId;

    private Gson gson = new Gson();

    public LoginResponseProcessor(String sessionId){
        this.sessionId = sessionId;
    }

    @Override
    public String getResponseBody() {

        LoginResponseModel loginResponseModel = new LoginResponseModel(sessionId);

        return gson.toJson(loginResponseModel,LoginResponseModel.class);
    }
}
