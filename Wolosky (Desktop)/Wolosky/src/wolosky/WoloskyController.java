package wolosky;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import static wolosky.Wolosky.sqlConnection;
import wolosky.admin.AdminController;

/**
 *
 * @author VakSF
 */
public class WoloskyController implements Initializable {
    
    @FXML
    private TextField userTF;
    
    @FXML
    private PasswordField passwordPF; 
   
    private Scene scene;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    @FXML
    private void login(ActionEvent event) throws IOException, SQLException {
        
        String password = passwordPF.getText();
        String user = userTF.getText();
        
        if (!user.isEmpty() && !password.isEmpty()) {
            
            ResultSet result = sqlConnection.search("SELECT user, password FROM user_accounts "
                    + "WHERE user = '" + user + "' AND password = '" + password + "';");
            
            if (result != null && result.first()) {
                    
                openFXML("/wolosky/admin/AdminFXML.fxml", "Administrador \t-\t @Kaizen Soft by Arturo Cordero");
                ((Node) event.getSource()).getScene().getWindow().hide();
                
            } else {
                
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("No se ha podido iniciar sesión");
                alert.setContentText("Usuario y/o contraseña incorrectos...");
                alert.showAndWait();

            }
            
        } else {
            
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Ha olvidado llenar algún campo");
            alert.setContentText("Por favor introduzca los campos faltantes...");
            alert.showAndWait();
            
        }
        
    }
    
    public void setScene(Scene scene) {
        this.scene = scene;
        this.initStyles();
    }
    
    public void initStyles() {
        
        this.scene.getStylesheets().clear();
        this.scene.getStylesheets().add(
                wolosky.WoloskyController.class.getResource("css/styles.css").toExternalForm()
        );
        
    }
    
    private void openFXML(String fxml, String title) throws IOException {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml)); 
       
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setScene(new Scene((Pane) loader.load()));
        
        AdminController controller = loader.<AdminController>getController();
        stage.setTitle(title);
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                System.out.println("Closing...");
                
                controller.killThread();
                
            }
        });
        
        stage.show();
        
    }
    
}