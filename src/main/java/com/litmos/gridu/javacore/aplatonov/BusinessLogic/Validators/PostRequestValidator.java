package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators;

import com.litmos.gridu.javacore.aplatonov.models.ValidationResultModel;

public class PostRequestValidator extends AbstractRequestValidator {

    @Override
    protected ValidationResultModel getRequestMethodValidationResult(String requestMethodName) {
       ValidationResultModel validationResultModel;
        boolean isValid = false;

        if (requestMethodName.equals("POST") ){
            isValid = true;
            validationResultModel = new ValidationResultModel(true);
        }
        else {
            validationResultModel = new ValidationResultModel(false, "Invalid request method", "Only POST method is allowed");
        }
        return validationResultModel;
    }
}
