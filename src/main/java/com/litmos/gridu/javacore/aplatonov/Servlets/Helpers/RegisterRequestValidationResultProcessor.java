package com.litmos.gridu.javacore.aplatonov.Servlets.Helpers;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Response.ErrorResponseProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models.ValidationResult;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterRequestValidationResultProcessor {


    public static boolean isResultSuccess(HttpServletResponse resp, ValidationResult validationResultModel, ServletContext servletContext) throws IOException {

        resp.setContentType("application/json");

        if(validationResultModel.isSuccess()) {
            servletContext.log("RequestAndOther validation success");
            return true;
          //  doPost(req,resp);
        }
        else if(!validationResultModel.isSuccess() && validationResultModel.getMessage()=="UnsupportedContentType"){
            servletContext.log("RequestAndOther validation failed - "  + validationResultModel.getMessage());
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor(validationResultModel.getMessage(), validationResultModel.getDescription());
            resp.setStatus(415);
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }
        else if(!validationResultModel.isSuccess() && validationResultModel.getMessage()=="UserAlreadyLoggedIn") {

            servletContext.log("RequestAndOther validation failed - "  + validationResultModel.getMessage());
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor(validationResultModel.getMessage(), validationResultModel.getDescription());

            resp.addHeader("Allow","POST");
            resp.setStatus(409);
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }
        else {
            servletContext.log("RequestAndOther validation failed - "  + validationResultModel.getMessage());
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor(validationResultModel.getMessage(), validationResultModel.getDescription());

            resp.addHeader("Allow","POST");
            resp.setStatus(405);
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }

        return false;
    }
}