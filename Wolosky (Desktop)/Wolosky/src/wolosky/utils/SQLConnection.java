package wolosky.utils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import wolosky.admin.Client;

/**
 *
 * @author Arturo Cordero
 */
public class SQLConnection {
    
    private Connection myConnection;
    
    private Statement statement;

    public SQLConnection(String user, String password, String dataBase, String type) 
            throws SQLException, ClassNotFoundException {
        
        String connectionURL = null;
        
        try {
        
            if (type.equalsIgnoreCase("sqlserver")) {

                connectionURL = "jdbc:sqlserver://localhost:1433;"
                              + "databaseName = " + dataBase + ";";

                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            }

            if (type.equalsIgnoreCase("mysql")) {

                connectionURL = "jdbc:mysql://74.63.215.106/" + dataBase;

                Class.forName("com.mysql.jdbc.Driver");

            }

            if (type.equalsIgnoreCase("db2")) {

                connectionURL = "jdbc:db2://localhost:50000/" + dataBase;

                Class.forName("com.ibm.db2.jcc.DB2Driver");
                
            }
            
            myConnection = DriverManager.getConnection(connectionURL, user, password);
            
            statement = myConnection.createStatement();
            
        } catch (ClassNotFoundException ex) {
            
            throw new ClassNotFoundException("Conector no encontrado. Mensaje de error: " + ex.getMessage());
            
        } catch (SQLException ex) {
            
            throw new SQLException("Error en SQL. Mensaje de error: " + ex.getMessage());
            
        } 
        
    }
    
    public boolean insert(String query) {
        
        try {
            
            if (!myConnection.isClosed()) {
                
                statement.execute(query);
                
            } else {
                
                System.out.println("Mensaje de error: La conexión con la Base de Datos está cerrada.");
                
                return false;
            }
            
        } catch(SQLException ex) {
            
            System.out.println("Mensaje de error: " + ex.getMessage());
            
            return false;
            
        }
        
        return true;
        
    }
    
    public boolean update(String query) {
        
        try {
            
            if (!myConnection.isClosed()) {
                
                statement.executeUpdate(query);
                
            } else {
                
                System.out.println("Mensaje de error: La conexión con la Base de Datos está cerrada.");
                
                return false;
                
            }
            
        } catch(SQLException ex) {
            
            System.out.println("Error " + ex.getMessage());
            
            return false;
            
        }
        
        return true;
        
    }
    
    public boolean delete(String query) {
        return insert(query);
    }
    
    public ResultSet search(String query) {
        
        try {
            
            if (!myConnection.isClosed()) {
                
                ResultSet resultSet = statement.executeQuery(query);
                
                if (!resultSet.next()) {
                    
                    System.out.println("No hay resultados que coincidan con la búsqueda.");
                    
                    return null;
                    
                }

                return resultSet;
                
            } else {
                
                System.out.println("La conexión con la Base de Datos está cerrada.");
                
                return null;
                
            }
            
        } catch(SQLException ex) {
            
            System.out.println("Error " + ex.getMessage());
            
            return null;
            
        }
        
    }
    
    public int countRecords(String table) {
        
        ResultSet resultSet = search("SELECT COUNT(*) FROM " + table);
        
        if (resultSet != null) {
            
            try {
                
                return resultSet.getInt(1);
                
            } catch (SQLException ex) {
                
                System.out.println("Error " + ex.getMessage());
                
                return -1;
            }
            
        } else {
            
            return -1;
            
        }
        
    }
    
    public void close() {
        
        try {
            
            statement.close();
            myConnection.close();
            
        } catch (SQLException ex) {
            
            System.out.println("Error " + ex.getMessage());
            
        } finally {
            
            myConnection = null;
            statement = null;
            
        }
        
    }
    
}