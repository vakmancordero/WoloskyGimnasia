package wolosky.admin.dialog;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import wolosky.admin.Client;

/**
 *
 * @author Arturh
 */
public class DetailsDialogController implements Initializable {
    
    @FXML
    private TextField nameTF, emailTF, phoneTF, genderTF, yearsTF, typeTF;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void initData(Client client) {
        
        this.nameTF.setText(client.getNombre());
        this.emailTF.setText(client.getEmail());
        this.phoneTF.setText(client.getTelefono());
        this.genderTF.setText(client.getSexo());
        this.yearsTF.setText(Integer.toString(client.getEdad()));
        this.typeTF.setText(client.getTipo());
        
    }
    
    @FXML
    private void close(ActionEvent event) {
        
        ((Node) event.getSource()).getScene().getWindow().hide();
        
    }
    
}
