package com.litmos.gridu.javacore.aplatonov.Servlets.Helpers;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Response.ErrorResponseProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models.ValidationResult;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecureGetValidatorResultProcessor {

    public static boolean isResultSuccess(HttpServletResponse resp, ValidationResult validationResultModel, ServletContext servletContext) throws IOException {

        resp.setContentType("application/json");
        if(validationResultModel.isSuccess()) {
            servletContext.log("RequestAndOther validation success");
            return true;
        }
        else if(!validationResultModel.isSuccess() && validationResultModel.getMessage()=="Unauthorized") {

            servletContext.log("RequestAndOther validation failed - "  + validationResultModel.getMessage());
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor(validationResultModel.getMessage(), validationResultModel.getDescription());

            resp.setStatus(401);
            resp.addHeader("WWW-Authenticate", "Basic");
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }
        else {
            servletContext.log("RequestAndOther validation failed - "  + validationResultModel.getMessage());
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor(validationResultModel.getMessage(), validationResultModel.getDescription());

            resp.addHeader("Allow","GET");
            resp.setStatus(405);
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }

        return false;
    }


}
