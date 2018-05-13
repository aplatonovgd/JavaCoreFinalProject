package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models.ValidationResult;

public interface BaseRequestValidator {

    ValidationResult getRequestValidationResult();

   // boolean areHttpHeadersValid(HttpServletRequest request);

   // boolean isRequestMethodNameValid(String methodname);
}
