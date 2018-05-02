package com.litmos.gridu.javacore.aplatonov.Servlets.Helpers;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.EmptyCartException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.IncorrectQuantityException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.ItemNotfoundException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.CheckoutRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Response.CheckoutResponseProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Response.ErrorResponseProcessor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class CheckoutRequestExcpetionProcessor {

    public static void processRequest (HttpServletResponse resp, CheckoutRequestProcessor checkoutRequestProcessor
                                       ,ServletContext servletContext) throws IOException {
        resp.setContentType("application/json");
        try {
            String orderId = checkoutRequestProcessor.processRequest();
            CheckoutResponseProcessor checkoutResponseProcessor = new CheckoutResponseProcessor(orderId);
            resp.getWriter().write(checkoutResponseProcessor.getResponseBody());
        }
        catch(EmptyCartException e){
            servletContext.log(e.getMessage());
            resp.setStatus(409);
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor("EmptyCart",
                            e.getMessage());
            resp.getWriter().write(errorResponseProcessor.getResponseBody());

        }
        catch (ItemNotfoundException e){
            servletContext.log(e.getMessage());
            resp.setStatus(409);
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor("ItemIdNotExist",
                            "Requested productId not found");
            resp.getWriter().write(errorResponseProcessor.getResponseBody());
        }
        catch (IncorrectQuantityException e){
            servletContext.log(e.getMessage());
            resp.setStatus(409);
            ErrorResponseProcessor errorResponseProcessor =
                    new ErrorResponseProcessor("IncorrectQuantity",
                            "Incorrect product quantity");
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
