package me.moontimer.dreampvp.dremapi;

import com.sun.rowset.CachedRowSetImpl;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;

public class MySQL {

    private final String hostname, database, username, password, port;
    private HikariDataSource hikariDataSource;

    public MySQL(String hostname, String database, String username, String password, String port) {
        this.hostname = hostname;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;



    }

    public void connect() {
        this.hikariDataSource = new HikariDataSource();

        hikariDataSource.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database);
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        hikariDataSource.setMaximumPoolSize(25);

        execute("CREATE TABLE IF NOT EXISTS PlayerTimes (UUID TEXT, PlayTime INT)");
    }

    public void execute(String query) {
        DreamAPI.getInstance().getProxy().getScheduler().runAsync(DreamAPI.getInstance(), new Runnable() {
            @Override
            public void run() {
                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement = hikariDataSource.getConnection().prepareStatement(query);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public ResultSet executeQuery(String query) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        CachedRowSet cachedRowSet = null;

        try {
            connection = hikariDataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            cachedRowSet = new CachedRowSetImpl();
            cachedRowSet.populate(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cachedRowSet;
    }

    public String getSpecificString (String table, String whatReturn, String bedingung, String value) {
        ResultSet resultSet = executeQuery("SELECT * FROM " + table + " WHERE " + bedingung + "='" + value + "'");
        String returner = null;

        try {
            if (resultSet.next()) {
                returner = resultSet.getString(whatReturn);
            }
        } catch (SQLException e) {
            return null;
        }
        return returner;
    }

    public String getString(String table, String whatReturn) {
        ResultSet  rs = executeQuery("SELECT * FROM " + table);
        String returner = null;

        try {
            if(rs.next()) {
                returner = rs.getString(whatReturn);
            }
        } catch (SQLException e) {
            return null;
        }

        return returner;
    }
}
