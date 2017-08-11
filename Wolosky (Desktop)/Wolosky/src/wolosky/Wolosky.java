package wolosky;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wolosky.utils.SQLConnection;

/**
 *
 * @author VakSF
 */
public class Wolosky extends Application {
    
    public static SQLConnection sqlConnection;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("WoloskyFXML.fxml"));
        
        Parent root = (Parent) myLoader.load();
        
        WoloskyController myController = myLoader.getController();
        Scene scene = new Scene(root);
        
        myController.setScene(scene);
        
        this.initConnection();
        
        stage.setTitle("Wolosky - Iniciar sesión");
        stage.setScene(scene);
        stage.show();
        
    }
    
    public void initConnection() {
        
        try {
            
            Wolosky.sqlConnection = new SQLConnection("woloskyg_vaksfk", "jaqart_56923", "  ", "mysql");
            
            System.out.println("Conexion a la base de datos exitosa!");
            
        } catch (ClassNotFoundException | SQLException ex) {
            
            System.out.println("Error en la conexion a la base de datos...");
            
        }
        
    }

    public void initConnection() {
        
        try {
            
            Wolosky.sqlConnection = new SQLConnection("woloskyg_vaksfk", "jaqart_56923", "  ", "mysql");
            
            System.out.println("Conexion a la base de datos exitosa!");
            
        } catch (ClassNotFoundException | SQLException ex) {
            
            System.out.println("Error en la conexion a la base de datos...");
            
        }
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
