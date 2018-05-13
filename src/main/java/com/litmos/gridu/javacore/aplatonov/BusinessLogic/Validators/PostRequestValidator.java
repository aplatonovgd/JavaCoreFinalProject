package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models.ValidationResult;

import javax.servlet.http.HttpServletRequest;

public class PostRequestValidator extends AbstractRequestValidator {

    public PostRequestValidator(HttpServletRequest request){
        super(request);
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
