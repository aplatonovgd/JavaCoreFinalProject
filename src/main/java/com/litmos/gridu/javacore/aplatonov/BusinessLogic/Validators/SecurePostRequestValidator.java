package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request.LoginRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.ValidationResultModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class SecurePostRequestValidator extends AbstractSecureRequestValidator {


    public SecurePostRequestValidator(HttpServletRequest request, LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo,
                                      boolean isUnauthorizedRequestExpected, ServletContext context) {
        super(request, loggedInUserInfo, isUnauthorizedRequestExpected, context);
    }


    @Override
    protected ValidationResultModel getRequestMethodValidationResult(String requestMethodName) {
        ValidationResultModel validationResultModel;
        boolean isValid = false;

        if (requestMethodName.equals("POST") ){
            isValid = true;
            validationResultModel = new ValidationResultModel(true);
        }
        else {
            validationResultModel = new ValidationResultModel(false, "InvalidRequestMethod", "Only POST method is allowed");
        }
        return validationResultModel;
    }
}
