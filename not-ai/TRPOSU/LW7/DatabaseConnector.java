package by.shobik.lw4;

import java.sql.*;

public class DatabaseConnector {

    private static final String url = "jdbc:mysql://localhost:3306/local_network_lw4";
    private static final String user = "user";
    private static final String password = "dekan37gsu@";

    public static Connection getInstance() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.exit(1);
        }
        return connection;
    }

}
