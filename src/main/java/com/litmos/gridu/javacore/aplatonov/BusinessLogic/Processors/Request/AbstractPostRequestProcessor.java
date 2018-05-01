package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidJsonException;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public abstract class AbstractPostRequestProcessor {

    protected HttpServletRequest request;
    protected Gson gson = new GsonBuilder().
            excludeFieldsWithoutExposeAnnotation().
            create();
    protected String requestBody;
    protected DBProcessor dbProcessor;


    protected AbstractPostRequestProcessor(HttpServletRequest request, DBProcessor dbProcessor) throws IOException {
        this.request = request;
        if (request != null){
            this.requestBody = getBody(request);
        }
        this.dbProcessor = dbProcessor;

    }

    protected abstract Object parseJson(String json) throws InvalidJsonException;

    protected final String getBody(HttpServletRequest request) throws IOException {

        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String buf = buffer.toString();
        return buf;
    }


}
