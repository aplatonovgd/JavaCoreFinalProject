package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.InvalidJsonException;
import com.litmos.gridu.javacore.aplatonov.models.RegisterRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegistrationProcessor extends RequestProcessor {


    RegistrationProcessor(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super(request,response);
    }

   @Override
   public RegisterRequest ParseJson(String json) throws InvalidJsonException {

      RegisterRequest registerRequest =  gson.fromJson(json, RegisterRequest.class);

      if (registerRequest == null ||  registerRequest.email == null || registerRequest.password == null){
         throw new InvalidJsonException("Invalid JSON");
      }

      return registerRequest;
    }



}
