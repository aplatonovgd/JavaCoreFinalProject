package com.litmos.gridu.javacore.aplatonov.Servlets;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request.AbstractCartRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request.LoginRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;

import javax.servlet.*;
import java.io.IOException;
import java.sql.SQLException;


public class ServerInit implements ServletContextListener{

//private static Logger logger = Logger.getLogger(ServerInit.class);





    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.log("===========SERVER INIT===========");
        //TODO DELETE;
        //String hello = (String) servletContext.getInitParameter("email");
       // servletContext.log(hello);
        //servletContext.setInitParameter("email","john@wick.com");
       // hello = (String) servletContext.getInitParameter("email");
       // servletContext.log(hello);
       // servletContext.setAttribute("dbConnection","dbconninfo"); // use for test
        //throw new RuntimeException("test");
        //getting Server properties
        servletContext.log("Getting DB params from web.xml");

        String dbUrl = servletContext.getInitParameter("databaseUrl");
        String dbName = servletContext.getInitParameter("databaseName");
        String dbUsername = servletContext.getInitParameter("databaseLogin");
        String dbPassword = servletContext.getInitParameter("databasePassword");
        String dbCreateScript = servletContext.getRealPath("/WEB-INF/dbCreateScript.sql");
        String addTableDataScript = servletContext.getRealPath("/WEB-INF/addTableDataScript.sql");

        servletContext.log("Database Parameters");
        servletContext.log ("Database URL: " + dbUrl);
        servletContext.log ("Database Name: " + dbName);
        servletContext.log ("Database LOGIN: " + dbUsername);
        servletContext.log ("Database password: " + dbPassword);

        servletContext.log("Getting settings from web.xml");
        boolean hashPasswords = Boolean.parseBoolean(servletContext.getInitParameter("hashPasswords"));
        servletContext.log("Hash password required: " + hashPasswords);


        servletContext.log("Trying to connect to the database");

        DBProcessor dbProcessor;
        try
        {
            dbProcessor = new DBProcessor(dbUrl, dbName, dbUsername, dbPassword, dbCreateScript, addTableDataScript);
            servletContext.log("Connected Successfully");
        } catch (IOException e) {
            servletContext.log("FATAL ERROR" );
            throw new RuntimeException(e);
        } catch (SQLException e) {
            servletContext.log("FATAL ERROR" );
            throw new RuntimeException(e);
        }

        LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo = new LoginRequestProcessor.LoggedInUserInfo();

        AbstractCartRequestProcessor.CartInfo cartInfo = new AbstractCartRequestProcessor.CartInfo();

        servletContext.setAttribute("dbConnection",dbProcessor);
        servletContext.setAttribute("hashPasswords", hashPasswords);
        servletContext.setAttribute("loggedInUserInfo", loggedInUserInfo);
        servletContext.setAttribute("cartInfo", cartInfo);
        servletContext.log("Initialized objects added to servletContext");

        servletContext.log("===========INIT SUCCESSFUL===========");

    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
