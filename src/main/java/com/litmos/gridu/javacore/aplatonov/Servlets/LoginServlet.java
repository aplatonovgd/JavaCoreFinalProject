package com.litmos.gridu.javacore.aplatonov.Servlets;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidEmailException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.*;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.IncorrectNameOrPasswordException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.SessionNotFoundException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.LoginRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Response.ErrorResponseProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Response.LoginResponseProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators.SecurePostRequestValidator;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Servlets.Helpers.LoginRequestValidationResultProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models.ValidationResult;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ServletConfig servletConfig = getServletConfig();
        DBProcessor dbProcessor = (DBProcessor) servletConfig.getServletContext().getAttribute("dbConnection");
        LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo = (LoginRequestProcessor.LoggedInUserInfo) servletConfig.getServletContext().getAttribute("loggedInUserInfo");
        boolean hashPasswords = (boolean) servletConfig.getServletContext().getAttribute("hashPasswords");


        getServletContext().log("Post request processor started");

        try {
            LoginRequestProcessor loginProcessor = new LoginRequestProcessor(req,dbProcessor,hashPasswords,loggedInUserInfo);
            String sessionId = loginProcessor.processRequest();
            addCookiesToResponse(sessionId, loggedInUserInfo, resp);

            LoginResponseProcessor loginResponseProcessor = new LoginResponseProcessor(sessionId);
            resp.getWriter().write(loginResponseProcessor.getResponseBody());

        }
        catch (IncorrectNameOrPasswordException e){
            getServletContext().log("Incorrect email or password : " + e.getMessage());

            resp.setStatus(401);
            resp.addHeader("WWW-Authenticate", "Basic");
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor("Incorrect credentials",
                            "Incorrect email or password");
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
            resp.setStatus(500);
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor("Something went wrong",
                            "Something went wrong. Try again");
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }

    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().log("Login request:");
        getServletContext().log("RequestAndOther Method " + req.getMethod());
        getServletContext().log("RequestAndOther headers validation started");

        ServletConfig servletConfig = getServletConfig();
        LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo =
                (LoginRequestProcessor.LoggedInUserInfo) servletConfig.getServletContext().getAttribute("loggedInUserInfo");

        SecurePostRequestValidator postRequestValidator = new SecurePostRequestValidator(req, loggedInUserInfo,
                true, getServletContext());
        ValidationResult validationResultModel = postRequestValidator.getRequestValidationResult();

        boolean isResultSuccess = LoginRequestValidationResultProcessor.isResultSuccess(resp,validationResultModel,getServletContext());

        if (isResultSuccess){
            doPost(req,resp);
        }

    }

    public void addCookiesToResponse(String sessionId, LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo, HttpServletResponse resp) throws SessionNotFoundException {

        LoggedinUser user = loggedInUserInfo.getUserBySessionId(sessionId);
        String passwordHash = user.getUserPasswordHash();

        Cookie passwordHashCookie = new Cookie("ph", passwordHash);
        Cookie userSessionCookie = new Cookie("sessionId", sessionId);
        resp.addCookie(passwordHashCookie);
        resp.addCookie(userSessionCookie);
    }



}
