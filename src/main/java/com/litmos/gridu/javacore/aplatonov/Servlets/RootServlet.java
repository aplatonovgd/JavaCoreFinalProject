package com.litmos.gridu.javacore.aplatonov.Servlets;


import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request.AbstractCartRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request.LoginRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request.RootRequestProccessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Response.ErrorResponseProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators.SecureGetRequestValidator;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.ValidationResult;
import com.litmos.gridu.javacore.aplatonov.Servlets.Helpers.SecureGetValidatorResultProcessor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("")
public class RootServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ServletConfig servletConfig = getServletConfig();
        DBProcessor dbProcessor = (DBProcessor) servletConfig.getServletContext().getAttribute("dbConnection");
        AbstractCartRequestProcessor.ProductInfo productInfo =
                (AbstractCartRequestProcessor.ProductInfo) servletConfig.getServletContext().getAttribute("productInfo");


        RootRequestProccessor rootRequestProccessor = new RootRequestProccessor(req, productInfo);

        String responseBody;
        try {
            responseBody = rootRequestProccessor.processRequest();
            resp.getWriter().write(responseBody);
        }
        catch (Exception e){
            getServletContext().log("Something went wrong: " + e.getMessage());

            resp.setStatus(500);
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor("Something went wrong",
                            "Something went wrong. Try again");
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().log("Root request:");
        getServletContext().log("Request Method " + req.getMethod());
        getServletContext().log("Request headers validation started");

        ServletConfig servletConfig = getServletConfig();
        LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo =
                (LoginRequestProcessor.LoggedInUserInfo) servletConfig.getServletContext().getAttribute("loggedInUserInfo");

        SecureGetRequestValidator secureGetRequestValidator = new SecureGetRequestValidator(req,loggedInUserInfo,
                false, getServletContext());
        ValidationResult validationResultModel  = secureGetRequestValidator.getRequestValidationResult();

        boolean isResultSuccess = SecureGetValidatorResultProcessor.isResultSuccess(resp,validationResultModel,getServletContext());

        if (isResultSuccess){
            doGet(req,resp);
        }
    }


}
