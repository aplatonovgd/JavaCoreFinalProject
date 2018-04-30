package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators;

import com.litmos.gridu.javacore.aplatonov.Models.ValidationResultModel;

import javax.servlet.http.HttpServletRequest;

public class PostRequestValidator extends AbstractRequestValidator {

    public PostRequestValidator(HttpServletRequest request){
        super(request);
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
