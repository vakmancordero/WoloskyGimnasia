package wolosky.admin.dialog.clients;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import wolosky.admin.Client;

/**
 *
 * @author VakSF
 */
public class ShowClientsController implements Initializable {
    
    @FXML
    private TableView<Client> clientsTV;
    
    private ObservableList<Client> clients;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void initData(ObservableList<Client> clients) {
        
        this.clients = clients;
        
        this.clientsTV.setItems(this.clients);
        
        this.clientsTV.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
    }
    
    @FXML
    private void remove() {
        
        if (!this.clients.isEmpty()) {
        
            ObservableList<Client> toRemove = this.clientsTV.getSelectionModel().getSelectedItems();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("Confirmación de eliminación");
            alert.setContentText("¿Está seguro de eliminar los clientes seleccionados?");

            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == ButtonType.OK) {

                this.clients.removeAll(toRemove);

                Alert ok = new Alert(AlertType.INFORMATION);
                ok.setTitle("Información");
                ok.setHeaderText("Editar");
                ok.setContentText("La selección de clientes ha sido removida con éxito");
                ok.showAndWait();

            }
            
        } else {
            
            Alert ok = new Alert(AlertType.INFORMATION);
            ok.setTitle("Información");
            ok.setHeaderText("Removar");
            ok.setContentText("No hay clientes seleccionados por remover...");
            ok.showAndWait();
            
        }
    }
    
    public ObservableList<Client> getList() {
        
        return this.clients;
        
    }
    
}
