package com.litmos.gridu.javacore.aplatonov.Models;

import com.google.gson.annotations.Expose;

public class LoginResponseModel {

    @Expose
    private String sessionid;

    public LoginResponseModel(String sessionid){
        this.sessionid = sessionid;
    }

}
