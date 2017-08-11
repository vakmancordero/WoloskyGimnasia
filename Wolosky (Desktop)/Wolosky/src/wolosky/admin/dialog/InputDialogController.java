package wolosky.admin.dialog;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import wolosky.admin.Client;

/**
 *
 * @author VakSF
 */
public class InputDialogController implements Initializable {
    
    @FXML
    private TextField nameTF, emailTF, phoneTF;
    
    @FXML
    private ComboBox genderCB, yearsCB, typeCB;
    
    @FXML
    private Label titleLabel;
    
    private TextField[] texts;
    private ComboBox[] combos;
    
    private Client client;
    
    private Pattern pattern;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        initArrays();
        
        this.pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        
    }
    
    public void initCBs(ArrayList<String> arrType) {
        
        this.genderCB.getItems().addAll(
                "Femenino",
                "Masculino"
        );
        
        this.typeCB.getItems().addAll(arrType);
        
        for (int i = 1; i < 80; i++) {
            
            this.yearsCB.getItems().add(i);
            
        }
        
    }
    
    private void initArrays() {
        
        this.texts = new TextField[] {
            nameTF,
            emailTF,
            phoneTF
        };
        
        this.combos =  new ComboBox[] {
            genderCB,
            yearsCB,
            typeCB
        };
        
    }
    
    private void fillInputs() {
        
        if (client != null) {
            
            this.nameTF.setText(client.getNombre());
            this.emailTF.setText(client.getEmail());
            this.genderCB.getSelectionModel().select(client.getSexo());
            this.yearsCB.getSelectionModel().select(client.getEdad());
            this.phoneTF.setText(client.getTelefono());
            this.typeCB.getSelectionModel().select(client.getTipo());
            
        }
        
    }
    
    @FXML
    private void finalizeID(ActionEvent event) {
        
        if (check()) {
            
            Matcher matcher = pattern.matcher(this.emailTF.getText());
        
            if (matcher.matches()) {

                this.client = new Client(
                        this.nameTF.getText(), 
                        this.emailTF.getText(),
                        this.genderCB.getValue().toString(), 
                        this.phoneTF.getText(),
                        Integer.parseInt(this.yearsCB.getValue().toString()),
                        this.typeCB.getValue().toString()
                );
                
                ((Node) event.getSource()).getScene().getWindow().hide();

            } else {
                
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Error en el e-mail");
                alert.setContentText("Por favor introduzca un e-mail correcto...");
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
    
    public Client getClient() {
        
        return this.client;
        
    }
    
    public void setClient(Client client) {
        
        this.client = client;
        
        System.out.println(this.client);
        
        fillInputs();
        
    }
    
    public void setTitle(String title) {
        
        this.titleLabel.setText(title);
    }
    
    private boolean check() {
        
        for (TextField text : texts) {
            
            if (text.getText().isEmpty()) {
                return false;
            }
            
        }
        
        for (ComboBox combo : combos) {
            
            if (combo.getSelectionModel().getSelectedIndex() < 0) {
                return false;
            }
            
        }
        
        return true;
        
    }
    
}