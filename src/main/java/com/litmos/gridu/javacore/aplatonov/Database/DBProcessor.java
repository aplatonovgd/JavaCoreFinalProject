package com.litmos.gridu.javacore.aplatonov.Database;

import com.sun.javafx.binding.StringFormatter;

import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class DBProcessor extends AbstractDBConnector {

    boolean isDbExist= false;


    public List<String> parseSQLScript(String filepath) throws IOException {
        List<String> dbScript  = Files.readAllLines(Paths.get(filepath), Charset.defaultCharset());
        dbScript = dbScript.stream().map(r -> r.replaceAll("dbname",dbName)).collect(Collectors.toList());//.forEach(arg -> arg.replaceAll("dbname",dbName));
        return dbScript;
    }


    public DBProcessor(String dbUrl, String dbName, String username, String dbPassword, String dbCreateScript, String addTableDataScript) throws IOException, SQLException {
        initDBConnector(dbUrl, dbName, username, dbPassword);
        try (Connection connection = getDataBaseConnection(false)) {}
        catch (SQLException e) {
            //MySQL only
            if (e.getSQLState().equalsIgnoreCase("42000")) {
                executeBatchStatement(dbCreateScript);
                executeBatchStatement(addTableDataScript);
             }
             else throw new SQLException(e);
        }
    }

    /*public List<Products> getProducts (){

        try(Connection sqlConnection = getDataBaseConnection(true)) {

           // Statement statement = sqlConnection.createStatement();
           // Statement dataBaseCreationStatement = generateBatch(dbScript, sqlConnection, statement);
            dataBaseCreationStatement.executeBatch();
            dataBaseCreationStatement.close();

        }

    }*/

    public Statement generateBatch(List<String> sqlScript, Connection connection, Statement statement) throws SQLException {
        for(String line : sqlScript){
            statement.addBatch(line);
        }
        return statement;
    }

    public void executeBatchStatement(String sqlCreateScript) throws IOException, SQLException {
        List<String> dbScript = parseSQLScript(sqlCreateScript);

        try(Connection sqlConnection = getDataBaseConnection(true)) {

            Statement statement = sqlConnection.createStatement();
            Statement dataBaseCreationStatement = generateBatch(dbScript, sqlConnection, statement);
            dataBaseCreationStatement.executeBatch();
            dataBaseCreationStatement.close();

        }

    }

}
