package sample.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {

    public static Connection makeConnection() {
        Connection cn = null;
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=PlantShop";
            String username = System.getenv("sa");
            String password = System.getenv("123");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            cn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return cn;
    }
}
