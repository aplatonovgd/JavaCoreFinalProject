package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators;

import com.litmos.gridu.javacore.aplatonov.models.ValidationResultModel;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractRequestValidator implements BaseRequestValidator {

    @Override
    public ValidationResultModel getRequestValidationResult(HttpServletRequest request){
        ValidationResultModel validationResultModel;
        validationResultModel = getHttpHeadersValidationResult(request);
        return validationResultModel;
    }

    protected ValidationResultModel getHttpHeadersValidationResult(HttpServletRequest request){
        return getRequestMethodValidationResult(request.getMethod());
    }
    protected abstract ValidationResultModel getRequestMethodValidationResult(String requestMethodName);

}
