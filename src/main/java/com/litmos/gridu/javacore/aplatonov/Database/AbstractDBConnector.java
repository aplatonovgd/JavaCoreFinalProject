package com.litmos.gridu.javacore.aplatonov.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;

public abstract class AbstractDBConnector {

    private String fullDbUrl;
    private String cleanDbUrl;
    protected String dbName;
    private String dbUsername;
    private String dbPassword;

    private Connection dbConnection;

    protected void initDBConnector(String dbUrl, String dbName, String dbUsername, String dbPassword){
            this.dbName = dbName;
            this.cleanDbUrl = dbUrl;
            this.fullDbUrl = dbUrl + dbName;
            this.dbUsername = dbUsername;
            this.dbPassword = dbPassword;
    }

    protected Connection getDataBaseConnection(boolean sqlServerConnectionOnly) throws SQLException {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String databaseUrl;
        if (sqlServerConnectionOnly){
            databaseUrl = cleanDbUrl;
        }
        else {
            databaseUrl = fullDbUrl;
        }

         try {
             Connection dbConnection = DriverManager.getConnection(databaseUrl,dbUsername,dbPassword);
              this.dbConnection = dbConnection;
         }
         catch (SQLException e){
             dbConnection = null;
            throw e;
        }
        return dbConnection;
    }


}
