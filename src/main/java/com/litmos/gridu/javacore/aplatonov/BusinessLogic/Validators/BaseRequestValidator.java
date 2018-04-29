package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators;

import com.litmos.gridu.javacore.aplatonov.models.ValidationResultModel;

import javax.servlet.http.HttpServletRequest;

public interface BaseRequestValidator {

    ValidationResultModel getRequestValidationResult(HttpServletRequest request);

   // boolean areHttpHeadersValid(HttpServletRequest request);

   // boolean isRequestMethodNameValid(String methodname);
}
