package wolosky.admin.dialog.button;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 *
 * @author Arturh
 */
public class ExternalDialogController implements Initializable {
    
    @FXML
    private TextField emailTF;
    
    private String action;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void initData(String email) {
        this.emailTF.setText(email);
    }
    
    public String getAction() {
        return this.action;
    }
    
    public String getUpdate() {
        return this.emailTF.getText();
    }
    
    @FXML
    private void edit(ActionEvent event) {
        
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        
        Matcher matcher = pattern.matcher(this.emailTF.getText());
        
        if (matcher.matches()) {
            
            this.action = "edit";
            
            ((Node) event.getSource()).getScene().getWindow().hide();
            
        } else {
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Error en el e-mail");
            alert.setContentText("Por favor introduzca un e-mail correcto...");
            alert.showAndWait();
            
        }
        
    }
    
    @FXML
    private void delete(ActionEvent event) {
        
        this.action = "delete";
        
        ((Node) event.getSource()).getScene().getWindow().hide();
        
    }
    
}