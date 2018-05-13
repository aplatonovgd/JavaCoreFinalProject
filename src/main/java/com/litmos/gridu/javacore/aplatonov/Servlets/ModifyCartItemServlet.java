package com.litmos.gridu.javacore.aplatonov.Servlets;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.AbstractCartRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.LoginRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.ModifyCartItemRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators.SecurePostRequestValidator;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models.ValidationResult;
import com.litmos.gridu.javacore.aplatonov.Servlets.Helpers.ModifyCartItemRequestExcpetionProcessor;
import com.litmos.gridu.javacore.aplatonov.Servlets.Helpers.SecurePostRequestsValidatorProcessor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/modifyCartItem")
public class ModifyCartItemServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ServletConfig servletConfig = getServletConfig();
        DBProcessor dbProcessor = (DBProcessor) servletConfig.getServletContext().getAttribute("dbConnection");
        AbstractCartRequestProcessor.CartInfo cartInfo = (AbstractCartRequestProcessor.CartInfo)
                servletConfig.getServletContext().getAttribute("cartInfo");
        AbstractCartRequestProcessor.ProductInfo productInfo = (AbstractCartRequestProcessor.ProductInfo)
                servletConfig.getServletContext().getAttribute("productInfo");
        LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo = (LoginRequestProcessor.LoggedInUserInfo)
                servletConfig.getServletContext().getAttribute("loggedInUserInfo");

        ModifyCartItemRequestProcessor modifyCartItemRequestProcessor = new ModifyCartItemRequestProcessor(req, dbProcessor, cartInfo, productInfo, loggedInUserInfo);

        ModifyCartItemRequestExcpetionProcessor.processRequest(resp,modifyCartItemRequestProcessor,getServletContext());


    }



    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().log("/modifyCartItem request:");
        getServletContext().log("RequestAndOther Method " + req.getMethod());
        getServletContext().log("RequestAndOther headers validation started");

        ServletConfig servletConfig = getServletConfig();
        LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo =
                (LoginRequestProcessor.LoggedInUserInfo) servletConfig.getServletContext().getAttribute("loggedInUserInfo");


        SecurePostRequestValidator postRequestValidator = new SecurePostRequestValidator(req,loggedInUserInfo,
                false, getServletContext());
        ValidationResult validationResultModel = postRequestValidator.getRequestValidationResult();

        boolean isResultSuccess = SecurePostRequestsValidatorProcessor.isResultSuccess(resp,validationResultModel,getServletContext());

        if (isResultSuccess){
            doPost(req,resp);
        }
    }

}
