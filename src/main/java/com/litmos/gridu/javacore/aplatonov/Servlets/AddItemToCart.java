package com.litmos.gridu.javacore.aplatonov.Servlets;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.IncorrectQuantityException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidJsonException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.ItemNotfoundException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request.AbstractCartRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request.AddItemToCartRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request.LoginRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request.RootRequestProccessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Response.ErrorResponseProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators.SecurePostRequestValidator;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.ValidationResultModel;
import com.litmos.gridu.javacore.aplatonov.Servlets.Helpers.SecurePostRequestsValidatorProcessor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


@WebServlet("/addItemToCart")
public class AddItemToCart extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ServletConfig servletConfig = getServletConfig();
        DBProcessor dbProcessor = (DBProcessor) servletConfig.getServletContext().getAttribute("dbConnection");

        AbstractCartRequestProcessor.CartInfo cartInfo = (AbstractCartRequestProcessor.CartInfo)
                servletConfig.getServletContext().getAttribute("cartInfo");

        AddItemToCartRequestProcessor addItemToCartRequestProcessor = new AddItemToCartRequestProcessor(req, dbProcessor, cartInfo);

        String responseBody;
        try {
           addItemToCartRequestProcessor.processRequest();
        }
        catch (InvalidJsonException e){
            getServletContext().log("Something wrong with JSON. " + e.getMessage());
            resp.setStatus(400);
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor(e.getMessage(),
                            "Your request has invalid parameters");
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }
        catch (ItemNotfoundException e){
            getServletContext().log(e.getMessage());
            resp.setStatus(409);
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor("ItemIdNotExist",
                            "Requested productId not found");
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }
        catch (IncorrectQuantityException e){
            getServletContext().log(e.getMessage());
            resp.setStatus(409);
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor("IncorrectQuantity",
                            "Incorrect product quantity");
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }
        catch (Exception e){
            getServletContext().log("Something went wrong: " + e.getMessage());

            StringWriter outError = new StringWriter();
            e.printStackTrace(new PrintWriter(outError));
            getServletContext().log(outError.toString());

            resp.setStatus(500);
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor("Something went wrong",
                            "Something went wrong. Try again");
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().log("/addItemToCart request:");
        getServletContext().log("Request Method " + req.getMethod());
        getServletContext().log("Request headers validation started");

        ServletConfig servletConfig = getServletConfig();
        LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo =
                (LoginRequestProcessor.LoggedInUserInfo) servletConfig.getServletContext().getAttribute("loggedInUserInfo");


        SecurePostRequestValidator postRequestValidator = new SecurePostRequestValidator(req,loggedInUserInfo,
                false, getServletContext());
        ValidationResultModel validationResultModel = postRequestValidator.getRequestValidationResult();

        boolean isResultSuccess = SecurePostRequestsValidatorProcessor.isResultSuccess(resp,validationResultModel,getServletContext());

        if (isResultSuccess){
            doPost(req,resp);
        }
    }

}
