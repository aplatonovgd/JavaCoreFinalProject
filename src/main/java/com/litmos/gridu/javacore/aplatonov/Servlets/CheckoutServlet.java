package com.litmos.gridu.javacore.aplatonov.Servlets;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models.ValidationResult;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.AbstractCartRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.CheckoutRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.LoginRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators.SecureGetRequestValidator;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Servlets.Helpers.CheckoutRequestExcpetionProcessor;
import com.litmos.gridu.javacore.aplatonov.Servlets.Helpers.SecureGetValidatorResultProcessor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

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

        CheckoutRequestProcessor checkoutRequestProcessor = new CheckoutRequestProcessor(req, dbProcessor, cartInfo, productInfo, loggedInUserInfo);

        CheckoutRequestExcpetionProcessor.processRequest(resp,checkoutRequestProcessor,getServletContext());

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().log("/checkout request:");
        getServletContext().log("RequestAndOther Method " + req.getMethod());
        getServletContext().log("RequestAndOther headers validation started");

        ServletConfig servletConfig = getServletConfig();
        LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo =
                (LoginRequestProcessor.LoggedInUserInfo) servletConfig.getServletContext().getAttribute("loggedInUserInfo");

        SecureGetRequestValidator postRequestValidator = new SecureGetRequestValidator(req,loggedInUserInfo,
                false, getServletContext());
        ValidationResult validationResultModel = postRequestValidator.getRequestValidationResult();

        boolean isResultSuccess = SecureGetValidatorResultProcessor.isResultSuccess(resp,validationResultModel,getServletContext());

        if (isResultSuccess){
            doGet(req,resp);
        }
    }

}
