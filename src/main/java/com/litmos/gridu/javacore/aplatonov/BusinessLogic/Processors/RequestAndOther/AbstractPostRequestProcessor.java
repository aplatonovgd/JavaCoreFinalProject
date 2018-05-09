package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidEmailException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidJsonException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidPasswordException;
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


    public void checkUserEmail(String email) throws InvalidEmailException {
        if(!email.contains("@") || !(email.contains("."))){
            throw new InvalidEmailException("Incorrect email address");
        }
    }

    public void checkUserPassword(String password) throws InvalidPasswordException {
       if(password.length() <= 0) {
            throw new InvalidPasswordException("Your password is invalid");
        }

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
