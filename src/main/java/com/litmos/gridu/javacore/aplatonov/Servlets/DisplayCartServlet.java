package com.litmos.gridu.javacore.aplatonov.Servlets;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.AbstractCartRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.DisplayCartRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.LoginRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Response.ErrorResponseProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators.SecureGetRequestValidator;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models.ValidationResult;
import com.litmos.gridu.javacore.aplatonov.Servlets.Helpers.SecureGetValidatorResultProcessor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/displayCart")
public class DisplayCartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ServletConfig servletConfig = getServletConfig();
        DBProcessor dbProcessor = (DBProcessor) servletConfig.getServletContext().getAttribute("dbConnection");

        AbstractCartRequestProcessor.CartInfo cartInfo = (AbstractCartRequestProcessor.CartInfo)
                servletConfig.getServletContext().getAttribute("cartInfo");

        AbstractCartRequestProcessor.ProductInfo productInfo = (AbstractCartRequestProcessor.ProductInfo)
                servletConfig.getServletContext().getAttribute("productInfo");

        LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo = (LoginRequestProcessor.LoggedInUserInfo)
                servletConfig.getServletContext().getAttribute("loggedInUserInfo");


        DisplayCartRequestProcessor displayCartRequestProcessor = new DisplayCartRequestProcessor(req, dbProcessor, cartInfo, productInfo, loggedInUserInfo);

        String responseBody;

        try {
            responseBody = displayCartRequestProcessor.processRequest();
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
        getServletContext().log("/displayCart request:");
        getServletContext().log("RequestAndOther Method " + req.getMethod());
        getServletContext().log("RequestAndOther headers validation started");

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
