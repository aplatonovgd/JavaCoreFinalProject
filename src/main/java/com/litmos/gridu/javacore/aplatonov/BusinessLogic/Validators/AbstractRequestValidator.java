package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.ValidationResult;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractRequestValidator implements BaseRequestValidator {

    protected HttpServletRequest request;
    protected String methodName;

    AbstractRequestValidator(HttpServletRequest request){
        this.request = request;
        this.methodName = request.getMethod();
    }

    @Override
    public ValidationResult getRequestValidationResult(){
        ValidationResult validationResultModel;
        validationResultModel = getHttpHeadersValidationResult(request);
        return validationResultModel;
    }

    protected ValidationResult getHttpHeadersValidationResult(HttpServletRequest request){
        return getRequestMethodValidationResult(methodName);
    }

    protected abstract ValidationResult getRequestMethodValidationResult(String requestMethodName);

}
