/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubespbo;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author RAHMITHA MAHRUL
 */
public class Connection {
    private static com.mysql.jdbc.Connection conn;
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_NAME = "db_pharmacy";
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/" + DB_NAME;
    private static final String DB_UNAME = "root";
    private static final String DB_PASS = "";
    
    public static com.mysql.jdbc.Connection openConnection() {
        if(conn == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_URL, DB_UNAME, DB_PASS);
                
            } catch(ClassNotFoundException e) {
                System.err.format("Class not found");
               
            } catch (SQLException e) {
                System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}
