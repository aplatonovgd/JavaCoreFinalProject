package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators;

import com.litmos.gridu.javacore.aplatonov.Models.ValidationResultModel;

public interface BaseRequestValidator {

    ValidationResultModel getRequestValidationResult();

   // boolean areHttpHeadersValid(HttpServletRequest request);

   // boolean isRequestMethodNameValid(String methodname);
}
