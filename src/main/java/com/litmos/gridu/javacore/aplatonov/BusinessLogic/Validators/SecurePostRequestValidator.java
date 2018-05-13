package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.LoginRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.ValidationResult;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class SecurePostRequestValidator extends AbstractSecureRequestValidator {


    public SecurePostRequestValidator(HttpServletRequest request, LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo,
                                      boolean isUnauthorizedRequestExpected, ServletContext context) {
        super(request, loggedInUserInfo, isUnauthorizedRequestExpected, context);
    }


    @Override
    protected ValidationResult getRequestMethodValidationResult(String requestMethodName) {
        ValidationResult validationResultModel;
        boolean isValid = false;

        if (requestMethodName.equals("POST") ){
            isValid = true;
            validationResultModel = new ValidationResult(true);
        }
        else {
            validationResultModel = new ValidationResult(false, "InvalidRequestMethod", "Only POST method is allowed");
        }
        return validationResultModel;
    }
}
