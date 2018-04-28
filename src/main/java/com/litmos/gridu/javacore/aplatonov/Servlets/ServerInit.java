package com.litmos.gridu.javacore.aplatonov.Servlets;

import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.sun.javafx.binding.StringFormatter;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;


public class ServerInit implements ServletContextListener{

//private static Logger logger = Logger.getLogger(ServerInit.class);





    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.log("===========SERVER INIT===========");
        //String hello = (String) servletContext.getInitParameter("email");
       // servletContext.log(hello);
        //servletContext.setInitParameter("email","john@wick.com");
       // hello = (String) servletContext.getInitParameter("email");
       // servletContext.log(hello);
       // servletContext.setAttribute("dbConnection","dbconninfo"); // use for test
        //throw new RuntimeException("test");
        //getting Server properties

        servletContext.log("Getting DB params");

        String dbUrl = servletContext.getInitParameter("databaseUrl");
        String dbName = servletContext.getInitParameter("databaseName");
        String dbUsername = servletContext.getInitParameter("databaseLogin");
        String dbPassword = servletContext.getInitParameter("databasePassword");
        String dbCreateScript = servletContext.getRealPath("/WEB-INF/dbCreateScript.sql");
        String addTableDataScript = servletContext.getRealPath("/WEB-INF/addTableDataScript.sql");
        servletContext.log("DB PARAMS: " + "dbUrl: " + dbUrl + "dbName: " + dbName+  " dbUsername: " + dbUsername + " dbPassword: " + dbPassword);


        servletContext.log("Trying to connect to the database");

        DBProcessor dbProcessor;
        try
        {
            dbProcessor = new DBProcessor(dbUrl, dbName, dbUsername, dbPassword, dbCreateScript, addTableDataScript);
            servletContext.log("success");
        } catch (IOException e) {
            servletContext.log("FATAL ERROR" );
            throw new RuntimeException(e);
        } catch (SQLException e) {
            servletContext.log("FATAL ERROR" );
            throw new RuntimeException(e);
        }

        servletContext.setAttribute("dbConnection",dbProcessor);




    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
