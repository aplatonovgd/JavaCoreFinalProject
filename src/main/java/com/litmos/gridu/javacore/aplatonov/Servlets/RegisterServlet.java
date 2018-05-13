package com.litmos.gridu.javacore.aplatonov.Servlets;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidEmailException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidJsonException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.UserExistException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.LoginRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.RegistrationRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Response.ErrorResponseProcessor;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators.SecurePostRequestValidator;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Servlets.Helpers.RegisterRequestValidationResultProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.ValidationResult;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ServletConfig servletConfig = getServletConfig();
        DBProcessor dbProcessor = (DBProcessor) servletConfig.getServletContext().getAttribute("dbConnection");
        boolean hashPasswrods = (boolean) servletConfig.getServletContext().getAttribute("hashPasswords");
        getServletContext().log("Post request processor started");

        try {
            RegistrationRequestProcessor registrationRequestProcessor = new RegistrationRequestProcessor(req, dbProcessor, hashPasswrods);
            registrationRequestProcessor.processRequest();
        }

        catch (InvalidJsonException e ){
            getServletContext().log("Something wrong with JSON. " + e.getMessage());
            resp.setStatus(400);
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor(e.getMessage(),
                            "Your request has invalid parameters");
            resp.addHeader("Content-Type","application/json");
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }

        catch (UserExistException e) {
            getServletContext().log(e.getMessage());
            resp.setStatus(409);
            resp.addHeader("Content-Type","application/json");
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor("User already exists",
                            "User already exists in the database");
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }
        catch (InvalidEmailException e){
            getServletContext().log("Invalid Credentials:" + e.getMessage());
            resp.setStatus(401);
            resp.addHeader("WWW-Authenticate", "Basic");
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor("Invalid credentials",
                            "Invalid email or password");
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }
        catch (Exception e ){
            getServletContext().log("Something went wrong: " + e.getMessage());
            getServletContext().log(e.getStackTrace().toString());
            resp.addHeader("Content-Type","application/json");
            resp.setStatus(500);
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor("Something went wrong",
                            "Something went wrong. Try again");
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().log("Register request:");
        getServletContext().log("RequestAndOther Method " + req.getMethod());
        getServletContext().log("RequestAndOther headers validation started");

        ServletConfig servletConfig = getServletConfig();
        LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo =
                (LoginRequestProcessor.LoggedInUserInfo) servletConfig.getServletContext().getAttribute("loggedInUserInfo");


        SecurePostRequestValidator postRequestValidator = new SecurePostRequestValidator(req,loggedInUserInfo,
                true, getServletContext());
        ValidationResult validationResultModel = postRequestValidator.getRequestValidationResult();

        boolean isResultSuccess = RegisterRequestValidationResultProcessor.isResultSuccess(resp,validationResultModel,getServletContext());

        if (isResultSuccess){
            doPost(req,resp);
        }
    }


}
