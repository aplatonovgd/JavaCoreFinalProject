package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request.LoginRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.ValidationResult;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class SecureGetRequestValidator extends AbstractSecureRequestValidator {


    public SecureGetRequestValidator(HttpServletRequest request, LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo, boolean isUnauthorizedRequestExpected, ServletContext servletContext) {
        super(request, loggedInUserInfo, isUnauthorizedRequestExpected, servletContext);
    }

    @Override
    protected ValidationResult getRequestMethodValidationResult(String requestMethodName) {
        ValidationResult validationResultModel;
        boolean isValid = false;

        if (requestMethodName.equals("GET") ){
            isValid = true;
            validationResultModel = new ValidationResult(true);
        }
        else {
            validationResultModel = new ValidationResult(false, "InvalidRequestMethod", "Only GET method is allowed");
        }
        return validationResultModel;
    }
}
