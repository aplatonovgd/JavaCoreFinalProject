package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors;


import com.google.gson.Gson;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.BaseRequestProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public abstract  class RequestProcessor implements BaseRequestProcessor {

    private HttpServletRequest request;
    private HttpServletResponse response;
    protected Gson gson = new Gson();
    protected String JsonResponseBody;


    RequestProcessor(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.request = request;
        this.response = response;
        this.JsonResponseBody = getBody(request);
    }


    public final String getBody(HttpServletRequest request) throws IOException {

        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String buf = buffer.toString();
        return buf;
    }

  /*  @Override
    public RegisterRequest ParseJson(String json){
        gson = new Gson();
        RegisterRequest newUserJSON = gson.fromJson(json,RegisterRequest.class);
        return newUserJSON;
    }*/


}
