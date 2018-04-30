package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators;

import com.litmos.gridu.javacore.aplatonov.Models.ValidationResultModel;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractRequestValidator implements BaseRequestValidator {

    protected HttpServletRequest request;
    protected String methodName;

    AbstractRequestValidator(HttpServletRequest request){
        this.request = request;
        this.methodName = request.getMethod();
    }

    @Override
    public ValidationResultModel getRequestValidationResult(){
        ValidationResultModel validationResultModel;
        validationResultModel = getHttpHeadersValidationResult(request);
        return validationResultModel;
    }

    protected ValidationResultModel getHttpHeadersValidationResult(HttpServletRequest request){
        return getRequestMethodValidationResult(methodName);
    }

    protected abstract ValidationResultModel getRequestMethodValidationResult(String requestMethodName);

}
