package com.litmos.gridu.javacore.aplatonov.Servlets;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.CartSessionProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.AbstractCartRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.LoginRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.ProductModel;

import javax.servlet.*;
import java.util.List;


public class ServerInit implements ServletContextListener{


    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.log("===========Server Initialization===========");
        servletContext.log("Getting DB params from web.xml");

        String dbUrl = servletContext.getInitParameter("databaseUrl");
        String dbName = servletContext.getInitParameter("databaseName");
        String dbUsername = servletContext.getInitParameter("databaseLogin");
        String dbPassword = servletContext.getInitParameter("databasePassword");
        String sessionExpirationTime = servletContext.getInitParameter("sessionExpirationTime");
        String sessionExpirationCheckInterval =servletContext.getInitParameter("sessionExpirationCheckInterval");
        String dbCreateScript = servletContext.getRealPath("/WEB-INF/dbCreateScript.sql");
        String addTableDataScript = servletContext.getRealPath("/WEB-INF/addTableDataScript.sql");

        servletContext.log("Database Parameters");
        servletContext.log ("Database URL: " + dbUrl);
        servletContext.log ("Database Name: " + dbName);
        servletContext.log ("Database LOGIN: " + dbUsername);
        servletContext.log ("Database password: " + dbPassword);

        servletContext.log("Getting settings from web.xml");
        boolean hashPasswords = Boolean.parseBoolean(servletContext.getInitParameter("hashPasswords"));
        servletContext.log ("Cart session time: " + sessionExpirationTime);
        servletContext.log ("Thread session check interval: " + sessionExpirationCheckInterval);

        servletContext.log("Hash password required: " + hashPasswords);


        servletContext.log("Trying to connect to the database");

        DBProcessor dbProcessor;
        List<ProductModel> productModelList;
        try
        {
            dbProcessor = new DBProcessor(dbUrl, dbName, dbUsername, dbPassword, dbCreateScript, addTableDataScript);
            servletContext.log("Connected Successfully");

            productModelList = dbProcessor.getProducts();
        }
        catch (Exception e) {
            servletContext.log("FATAL ERROR" );
            throw new RuntimeException(e);
        }

        LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo = new LoginRequestProcessor.LoggedInUserInfo();

        AbstractCartRequestProcessor.CartInfo cartInfo = new AbstractCartRequestProcessor.CartInfo();

        AbstractCartRequestProcessor.ProductInfo productInfo =
                new AbstractCartRequestProcessor.ProductInfo(productModelList);

        try {
            CartSessionProcessor cartSessionProcessor = new CartSessionProcessor(null,dbProcessor,cartInfo,
                    productInfo,loggedInUserInfo, Long.valueOf(sessionExpirationTime),
                    Long.valueOf(sessionExpirationCheckInterval), servletContextEvent.getServletContext());
            Thread thread = new Thread(cartSessionProcessor);
            thread.start();
        } catch (Exception e) {
            servletContext.log("Error" + e.getMessage());
        }

        servletContext.setAttribute("dbConnection",dbProcessor);
        servletContext.setAttribute("hashPasswords", hashPasswords);
        servletContext.setAttribute("loggedInUserInfo", loggedInUserInfo);
        servletContext.setAttribute("cartInfo", cartInfo);
        servletContext.setAttribute("productInfo", productInfo);
        servletContext.log("Initialized objects added to servletContext");

        servletContext.log("===========Initialized succesfully===========");

    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
