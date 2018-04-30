package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request.LoginRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.ValidationResultModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class SecureGetRequestValidator extends AbstractSecureRequestValidator {


    public SecureGetRequestValidator(HttpServletRequest request, LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo, boolean isUnauthorizedRequestExpected, ServletContext servletContext) {
        super(request, loggedInUserInfo, isUnauthorizedRequestExpected, servletContext);
    }

    @Override
    protected ValidationResultModel getRequestMethodValidationResult(String requestMethodName) {
        ValidationResultModel validationResultModel;
        boolean isValid = false;

        if (requestMethodName.equals("GET") ){
            isValid = true;
            validationResultModel = new ValidationResultModel(true);
        }
        else {
            validationResultModel = new ValidationResultModel(false, "InvalidRequestMethod", "Only GET method is allowed");
        }
        return validationResultModel;
    }
}
