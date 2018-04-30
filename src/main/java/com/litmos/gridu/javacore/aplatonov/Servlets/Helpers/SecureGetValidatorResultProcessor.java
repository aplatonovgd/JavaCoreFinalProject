package com.litmos.gridu.javacore.aplatonov.Servlets.Helpers;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Response.ErrorResponseProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.ValidationResultModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecureGetValidatorResultProcessor {

    public static boolean isResultSuccess(HttpServletResponse resp, ValidationResultModel validationResultModel, ServletContext servletContext) throws IOException {

        if(validationResultModel.isSuccess()) {
            servletContext.log("Request validation success");
            return true;
        }
        else if(!validationResultModel.isSuccess() && validationResultModel.getMessage()=="Unauthorized") {

            servletContext.log("Request validation failed - "  + validationResultModel.getMessage());
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor(validationResultModel.getMessage(), validationResultModel.getDescription());

            resp.setStatus(401);
            resp.setContentType("application/json");
            resp.addHeader("WWW-Authenticate", "Basic");
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }
        else {
            servletContext.log("Request validation failed - "  + validationResultModel.getMessage());
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor(validationResultModel.getMessage(), validationResultModel.getDescription());

            resp.addHeader("Allow","GET");
            resp.setContentType("application/json");
            resp.setStatus(405);
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }

        return false;
    }


}