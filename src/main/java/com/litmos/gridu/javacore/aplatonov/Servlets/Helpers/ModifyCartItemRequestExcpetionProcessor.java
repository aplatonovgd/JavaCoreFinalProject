package com.litmos.gridu.javacore.aplatonov.Servlets.Helpers;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.IncorrectQuantityException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidJsonException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.ItemNotfoundException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request.AddItemToCartRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request.ModifyCartItemRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Response.ErrorResponseProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.ModifyCartItemRequestModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ModifyCartItemRequestExcpetionProcessor {

    public static void processRequest (HttpServletResponse resp, ModifyCartItemRequestProcessor modifyCartItemRequestProcessor, ServletContext servletContext) throws IOException {

        try {
            modifyCartItemRequestProcessor.processRequest();
        }
        catch (InvalidJsonException e){
            servletContext.log("Something wrong with JSON. " + e.getMessage());
            resp.setStatus(400);
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor(e.getMessage(),
                            "Your request has invalid parameters");
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }
        catch (ItemNotfoundException e){
            servletContext.log(e.getMessage());
            resp.setStatus(409);
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor("ItemIdNotExist",
                            e.getMessage());
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }
        catch (IncorrectQuantityException e){
            servletContext.log(e.getMessage());
            resp.setStatus(409);
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor("IncorrectQuantity",
                            e.getMessage());
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }
        catch (Exception e){
            servletContext.log("Something went wrong: " + e.getMessage());

            StringWriter outError = new StringWriter();
            e.printStackTrace(new PrintWriter(outError));
            servletContext.log(outError.toString());

            resp.setStatus(500);
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor("Something went wrong",
                            "Something went wrong. Try again");
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }
    }


}
