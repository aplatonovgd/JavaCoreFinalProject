package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request;


import com.google.gson.Gson;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.InvalidJsonException;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public abstract class AbstractRequestProcessor {

    private HttpServletRequest request;
    protected Gson gson = new Gson();
    protected String jsonResponseBody;
    protected DBProcessor dbProcessor;


    protected AbstractRequestProcessor(HttpServletRequest request, DBProcessor dbProcessor) throws IOException {
        this.request = request;
        this.jsonResponseBody = getBody(request);
        this.dbProcessor = dbProcessor;

        getBody(request);
    }

    protected abstract Object parseJson(String jsonResponseBody) throws InvalidJsonException;

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
