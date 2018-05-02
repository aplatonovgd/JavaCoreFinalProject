package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.IncorrectContentTypeException;
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

        if(validationResultModel.isSuccess()){
           return checkRequestContentType(request);
        }

        return validationResultModel;
    }


    public ValidationResult checkRequestContentType(HttpServletRequest request) {

        String ContentType = request.getHeader("Content-type");

        if( ContentType==null || ContentType.length()==0){
           return new ValidationResult(false, "UnsupportedContentType", "No Content-Type" );
        }

        if (!ContentType.toLowerCase().equals("application/json")){
            return new ValidationResult(false, "UnsupportedContentType", "Unsupported Content-Type" );
        }
        return new ValidationResult(true);
    }


    protected ValidationResult getHttpHeadersValidationResult(HttpServletRequest request){
        return getRequestMethodValidationResult(methodName);
    }

    protected abstract ValidationResult getRequestMethodValidationResult(String requestMethodName);

}
