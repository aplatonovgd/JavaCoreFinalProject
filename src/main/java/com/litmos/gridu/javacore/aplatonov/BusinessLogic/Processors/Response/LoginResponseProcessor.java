package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Response;

import com.google.gson.Gson;
import com.litmos.gridu.javacore.aplatonov.models.LoginResponseModel;

public class LoginResponseProcessor implements BaseResponseProcessor{

    private String sessionid;
    private Gson gson = new Gson();


    public LoginResponseProcessor(String sessionId){
        this.sessionid = sessionId;
    }

    @Override
    public String getResponseBody() {
        return  gson.toJson(sessionid);
    }
}
