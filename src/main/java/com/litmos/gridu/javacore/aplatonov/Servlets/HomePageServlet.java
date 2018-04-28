package com.litmos.gridu.javacore.aplatonov.Servlets;


import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("")
public class HomePageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      //  Cookie[] cookies = req.getCookies();
       // String cookieName = cookies[0].getName();
       // resp.setContentType("application/json");
       // Cookie cookie = new Cookie("user","123456");

        //getServletContext().setAttribute("test","test3");
       //resp.addCookie(cookie);
        ServletContext servletContext = getServletContext();
        servletContext.log("log testing");

        ServletConfig servletConfig = getServletConfig();
        //String dbinfo = (String)servletConfig.getServletContext().getAttribute("dbConnection");
        //servletContext.log(dbinfo);

        String sayHi = (String) servletContext.getAttribute("testhi");

        resp.getWriter().write(sayHi);
       // DBProcessor dbProcessor = (DBProcessor) servletConfig.getServletContext().getAttribute("dbConnection");
        //resp.getWriter().write(dbProcessor.toString());

    }
/*
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String buf = buffer.toString();
        resp.getWriter().write(buf);
    }*/
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        servletContext.setAttribute("testhi",req.getContextPath());
        doGet(req,resp);
    }


}
