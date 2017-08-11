package wolosky.admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import wolosky.admin.dialog.DetailsDialogController;
import wolosky.admin.dialog.InputDialogController;
import wolosky.admin.dialog.button.ExternalDialogController;
import wolosky.admin.dialog.clients.ShowClientsController;

import wolosky.utils.MailConnection;
import wolosky.utils.ButtonClient;
        

import static wolosky.Wolosky.sqlConnection;
import wolosky.utils.FTPConnection;

/**
 *
 * @author VakSF
 */
public class AdminController implements Initializable {
    
    @FXML
    private TableView<Client> clientsTV;
    
    @FXML
    private TextField subjectTF, lowTF, highTF;
    
    @FXML
    private HTMLEditor messageTA;
    
    @FXML
    private Menu typeMenu;
    
    @FXML
    private MenuItem removeItem;
    
    @FXML
    private MenuButton menuButton;
    
    @FXML
    private AnchorPane rangePane;
    
    @FXML
    private HBox hbox;
    
    private Updater updater;
    
    private ObservableList<Client> clients, sendList, tempChildren;
    
    private ArrayList<String> arrTypes;
    
    private MailConnection myConnection;
    
    private FTPConnection myFTPConnection;
    
    private String lastDirectory = "nothing";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        initLists();
        initTV();
        initTFs();
        
        this.myConnection = new MailConnection("gimnasiawolosky@gmail.com", "wolosky1234", "smtp.gmail.com", "465");
        this.myFTPConnection = new FTPConnection("ftp.woloskygimnasia.com", "woloskyg", "n29A8v628-BF3]f", 21);
        
        this.fillTypeMenu();
        
    }
    
    @FXML
    private void insertImage() {
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir imagen");
        fileChooser.getExtensionFilters().add(
                new ExtensionFilter("Archivos de imagen", "*.jpg", "*.png")
        );
        
        if (!lastDirectory.equals("nothing")) {
            fileChooser.setInitialDirectory(
                    new File(lastDirectory)
            );
        }
        
        File selectedFile = fileChooser.showOpenDialog(null);
        
        if (selectedFile != null) {
            
            String remotePath = "Materialize/Images/";
            
            boolean storeFile = myFTPConnection.storeFile(selectedFile, "/public_html/" + remotePath);
            
            if (storeFile) {
            
                showAlert(
                        AlertType.INFORMATION, 
                        "Archivo subido", 
                        "La imagen: " + selectedFile.getName() 
                                + " ha sido insertada correctamente"
                );
                
                messageTA.setHtmlText(messageTA.getHtmlText() + "<img src=\"http://woloskygimnasia.com/" + remotePath + selectedFile.getName() + "\" border=\"0\" alt=\"\" style=\"width:304px; height:228px;\">");
                
            } else {
                
                showAlert(
                        AlertType.INFORMATION, 
                        "Error al subir el archivo", 
                        "La imagen: " + selectedFile.getName() 
                                + " no ha podido ser insertada"
                );

            }
            
        }
        
    }
    
    @FXML
    private void openFile() {
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir archivo web");
        fileChooser.getExtensionFilters().add(
                new ExtensionFilter("Archivos web", "*.html", "*.htm")
        );
        
        if (!lastDirectory.equals("nothing")) {
            fileChooser.setInitialDirectory(
                    new File(lastDirectory)
            );
        }
        
        File selectedFile = fileChooser.showOpenDialog(null);
        
        if (selectedFile != null) {
            
            lastDirectory = selectedFile.getParent();
        
            System.out.println("lastDirectory = " + lastDirectory);
            
            try {
                
                BufferedReader br = new BufferedReader(new FileReader(selectedFile));

                String code, html = "";

                while((code = br.readLine()) != null) {
                    html += (code += "\n");
                }
                
                System.out.println(html);
                
                this.messageTA.setHtmlText("");
                
                this.messageTA.setHtmlText(html);
                
            } catch (IOException ex) { }
            
        }
        
    }
    
    @FXML
    private void addExternal() {
        
        TextInputDialog inputDialog = new TextInputDialog();
        
        inputDialog.setTitle("Introducir información");
        inputDialog.setHeaderText("Introducir e-mail:");
        inputDialog.setContentText("Ingrese el e-mail externo a añadir");
        
        Optional<String> result = inputDialog.showAndWait();
        
        if (result.isPresent()) {
            
            if (!result.get().isEmpty()) {
                
                Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        
                Matcher matcher = pattern.matcher(result.get());
                
                if (matcher.matches()) {
                    
                    ButtonClient buttonClient = new ButtonClient(result.get());

                    buttonClient.setOnAction(this::externalDialog);

                    this.hbox.getChildren().add(buttonClient);
                    
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setHeaderText("Añadir");
                    alert.setContentText("El email: " + result.get() + " ha sido añadido correctamente");
                    alert.showAndWait();
                    
                } else {
                    
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Error en el e-mail");
                    alert.setContentText("Por favor introduzca un e-mail correcto...");
                    alert.showAndWait();
                    
                    addExternal();
                    
                }
            } else {
                
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Error en el e-mail");
                alert.setContentText("Por favor rellene el campo e-mail...");
                alert.showAndWait();

                addExternal();
                
            }
        } else {
            System.out.println(":(");
        }
    }
    
    @FXML
    private void externalDialog(ActionEvent event) {
        
        ButtonClient button = (ButtonClient)event.getSource();
        
        String email = button.getText();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/wolosky/admin/dialog/button/ExternalDialogFXML.fxml"));
        
        Stage stage = new Stage();
        
        try {
            
            stage.setScene(new Scene((Pane) loader.load()));
            
        } catch (IOException ex) { }
        
        ExternalDialogController externalDialog = loader.<ExternalDialogController>getController();
        
        externalDialog.initData(email);
        
        stage.showAndWait();
        
        String action = externalDialog.getAction();
        
        if (action.equals("edit")) {
            
            String update = externalDialog.getUpdate();
            
            int index = this.hbox.getChildren().indexOf(button);
            
            button.updateData(update);
            
            this.hbox.getChildren().set(index, button);
            
            showAlert(AlertType.INFORMATION, "Editar e-mail", "El e-mail: " + email + " ha sido editador correctamente");
            
        } else {
            
            if (action.equals("delete")) {
                
                this.hbox.getChildren().remove(button);

                showAlert(AlertType.INFORMATION, "Editar e-mail", "El e-mail: " + email + " ha sido removido exitosamente");
                
            }
            
        }
        
    }
    
    private void initTFs() {
        
        TextField[] textFields = new TextField[] {
            this.lowTF,
            this.highTF,
        };
        
        for (TextField textField : textFields) {
            
            textField.textProperty().addListener(new ChangeListener<String>() {

                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                    if (newValue.matches("\\d*")) {

                        try {

                            int value = Integer.parseInt(newValue);
                            
                            if (textField.getText().length() > 3) {
                                
                                textField.setText(textField.getText().substring(0, 3));
                                
                            } else {
                                
                                textField.setText(Integer.toString(value));
                                
                            }

                        } catch (Exception e) {

                            textField.setText("");

                        }

                    } else {

                        textField.setText(oldValue);

                    }

                }
            });
        }
        
        
    }
    
    private void initLists() {
        
        this.clients = FXCollections.observableArrayList();
        
        this.clientsTV.setItems(clients);
        
        this.sendList = FXCollections.observableArrayList();
        
        this.tempChildren = FXCollections.observableArrayList();
        
        this.arrTypes = new ArrayList<>();
        
        this.clientsTV.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        ResultSet result_types = sqlConnection.search("SELECT * FROM types");
        
        if (result_types != null) {
            
            try {

                do {
                    
                    this.arrTypes.add(
                            result_types.getString("nombre")
                    );
                    
                } while (result_types.next());
                

            } catch (SQLException ex) {

                System.out.println("Error al rellenar la lista de tipos.");
            }
            
        }
        
        fill();
        
        this.updater = new Updater();
        
        Thread thread = new Thread(this.updater);
        thread.start();
        
    }
    
    public void killThread() {
        
        this.updater.kill();
        
    }
    
    private Object[] toArray;
    
    private void fill() {
        
        ResultSet result_clients = sqlConnection.search("SELECT * FROM clients");
        
        ArrayList<Client> tempClients = new ArrayList<>();
        
        if (result_clients != null) {
        
            try {
                
                toArray = this.clientsTV.getSelectionModel().getSelectedIndices().toArray();
                
                do {
                    
                    tempClients.add(
                            
                            new Client(
                                    result_clients.getString("nombre"),
                                    result_clients.getString("email"),
                                    result_clients.getString("sexo"),
                                    result_clients.getString("telefono"),
                                    Integer.parseInt(result_clients.getString("edad")),
                                    result_clients.getString("tipo")
                            )
                            
                    );
                    
                } while (result_clients.next());
                
                this.clients.setAll(tempClients);
                
                for (int i = 0; i < toArray.length; i++) {
                    this.clientsTV.getSelectionModel().select((Integer)toArray[i]);
                }
                

            } catch (SQLException ex) {

                System.out.println("Error al rellenar la lista de clientes.");

            }
            
        }
        
    }
    
    private void removeDuplicates() {
        
        HashSet<Client> hashSet = new HashSet<>(this.sendList);
        
        this.sendList.clear(); this.sendList.addAll(hashSet);
        
        System.out.println("hashSet.size() = " + hashSet.size());
        
    }
    
    private void initTV() {
        
        if (!clientsTV.getItems().isEmpty()) {
                    
            removeItem.setDisable(false);
            
        } else {
            
            removeItem.setDisable(true);
            
        }
        
        this.clientsTV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Client>() {
            
            @Override
            public void changed(ObservableValue<? extends Client> observable, Client oldValue, Client newValue) {
                
                if (!clientsTV.getItems().isEmpty()) {
                    
                    removeItem.setDisable(false);
                    
                } else {
                    
                    removeItem.setDisable(true);
                    
                }
                
            }
            
        });
        
    }
    
    @FXML
    private void send() throws IOException {
        
        String subject = subjectTF.getText();
        
        String message = messageTA.getHtmlText();
        
        if (!subject.isEmpty() && !message.isEmpty()) {
            
            ObservableList<Node> children = hbox.getChildren();
            
            if (!this.tempChildren.isEmpty()) {
                
                this.sendList.removeAll(this.tempChildren);
                
            }
            
            this.tempChildren.clear();
            
            if (!children.isEmpty()) {
                    
                for (Node node : children) {

                    this.tempChildren.add(((ButtonClient)node).getClient());

                }
                
                this.sendList.addAll(this.tempChildren);

            }
            
            
            System.out.println("tempChildren = " + tempChildren.size());
            System.out.println("sendList = " + sendList.size());
            
            if (!sendList.isEmpty()) {
                
                System.out.println("Enviando emails...");
                
                int counter = 0;
                
                for (Client client : sendList) {

                    try {
                        
                        System.out.println("Enviando... " + client.toString());
                        
                        String email = client.getEmail();
                        
                        this.myConnection.sendMessage(email, subject, message, "html");
                        
                        System.out.println("OK! :D");
                        
                    } catch (Exception ex) {
                        
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error!");
                        alert.setHeaderText("Imposible enviar email");
                        alert.setContentText("No se ha podido enviar e-mail al cliente: " + client);
                        alert.show();
                        
                        try {
                            
                            Thread.sleep(1800);
                            
                            alert.close();
                            
                            counter++;
                            
                        } catch (Exception e) {
                            
                        }
                        
                    }
                }

                Alert information = new Alert(AlertType.INFORMATION);
                information.setTitle("Enviando");
                information.setHeaderText("Correcto!");
                information.setContentText("Su e-mail ha sido enviado correctamente a " + 
                        (sendList.size() - counter) + " clientes");
                information.showAndWait();
                
                if (counter > 0) {
                    
                    Alert error = new Alert(AlertType.ERROR);
                    error.setTitle("Enviando");
                    error.setHeaderText("Incorrecto!");
                    error.setContentText("Su e-mail no ha sido enviado a " + 
                            counter + " clientes, compruebe si los E-mails son correctos");
                    error.showAndWait();
                    
                }
                
            } else {
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Lista vacía!");
                alert.setHeaderText("No se han encontrado clientes en la lista de envío");
                alert.setContentText("Por favor agregue clientes por condición o por el campo de texto \"Destinatarios\"");
                alert.showAndWait();
                
            }
            
        } else {
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Ha olvidado llenar algún campo");
            alert.setContentText("Por favor introduzca los campos faltantes...");
            alert.showAndWait();
            
        }
        
    }
    
    @FXML
    private void remove() {
        
        ObservableList<Client> toRemove = this.clientsTV.getSelectionModel().getSelectedItems();
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Confirmación de eliminación");
        alert.setContentText("¿Está seguro de eliminar los clientes seleccionados?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK) {
            
            //serializeClients();
            for (int i = 0; i < toRemove.size(); i++) {
                
                Client client = toRemove.get(i);
                
                boolean deleted = sqlConnection.delete("DELETE FROM clients "
                        + "WHERE "
                            + "nombre = '" + client.getNombre() + "' AND "
                            + "email = '" + client.getEmail() + "';"
                );
                
                if (deleted) {
                    
                    Alert alertOk = new Alert(Alert.AlertType.INFORMATION);
                    alertOk.setTitle("Remover clientes");
                    alertOk.setHeaderText("Se han removido clientes");
                    alertOk.setContentText("Se ha eliminado el cliente " + client);
                    alertOk.showAndWait();
                    
                } else {
                    
                    Alert alertError = new Alert(AlertType.INFORMATION);
                    alertError.setTitle("Error");
                    alertError.setHeaderText("Eliminar");
                    alertError.setContentText("El cliente " + client + " no ha podido ser eliminado");
                    alertError.showAndWait();
                    
                }
                
            }
            
            this.clients.removeAll(toRemove);
            this.clientsTV.refresh();
            
        }
        
        this.fillTypeMenu();
        
    }
    
    @FXML
    private void addType() {
        
        TextInputDialog inputDialog = new TextInputDialog();
        
        inputDialog.setTitle("Introducir información");
        inputDialog.setHeaderText("Introducir tipo de cliente:");
        inputDialog.setContentText("Ingrese el tipo de cliente que desea registrar");
        
        Optional<String> result = inputDialog.showAndWait();
        
        if (result.isPresent()) {
            
            if (!result.get().isEmpty()) {
                
                String type = result.get();
                
                this.arrTypes.add(type);
                
                //this.serializer.serialize(this.arrTypes, "types.kz");
                sqlConnection.insert("INSERT INTO types(nombre) "
                        + "VALUES("
                            + "'" + type + "'"
                        + ");"
                );
                
                fillTypeMenu();
                
            }
            
        }
        
    }
    
    @FXML
    private void addSelection() {
        
        ObservableList<Client> selectedItems = this.clientsTV.getSelectionModel().getSelectedItems();
        
        if (!selectedItems.isEmpty()) {
            
            this.sendList.addAll(selectedItems);
            
            removeDuplicates();
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText("Añadir clientes >>");
            alert.setContentText("Se han añadido " + selectedItems.size() + " clientes a la lista de envío");
            alert.showAndWait();
            
        }
        
    }
    
    @FXML
    private void showInputDialog(ActionEvent event) throws IOException, InterruptedException {
                
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/wolosky/admin/dialog/InputDialogFXML.fxml"));
        
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        
        InputDialogController inputDialog = loader.<InputDialogController>getController();
        
        String type = event.getSource().toString().split("@")[0];
        String action = "";
        
        if (type.equals("MenuItem")) {
            
            action = ((MenuItem)event.getSource()).getText();
            
        } else {
            
            if (type.equals("Button")) {
                
                action = ((Button)event.getSource()).getText();
                
            }
            
        }
        
        inputDialog.initCBs(this.arrTypes);
        
        System.out.println("action = " + action);
        
        if (action.equalsIgnoreCase("Añadir cliente")) {
            
            stage.setTitle("Wolosky - Añadir cliente");
            
            stage.showAndWait();
            
            Client client = inputDialog.getClient();
        
            if (client != null) {
                
                //serializeClients();
                boolean insert = sqlConnection.insert("INSERT INTO clients(nombre, email, sexo, telefono, edad, tipo) "
                        + "VALUES("
                        + "'" + client.getNombre() + "', "
                        + "'" + client.getEmail() + "', "
                        + "'" + client.getSexo() + "', "
                        + "'" + client.getTelefono() + "', "
                        + "'" + client.getEdad() + "', "
                        + "'" + client.getTipo()+ "'"
                        + ")"
                );
                
                if (insert) {
                    
                    this.clients.add(client);
                    
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setHeaderText("Añadir");
                    alert.setContentText("El cliente: " + client + " ha sido añadido correctamente");
                    alert.showAndWait();
                    
                    clientsTV.getSelectionModel().selectFirst();
                    
                } else {
                    
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Añadir");
                    alert.setContentText("Al parecer el cliente a añadir ya existe");
                    alert.showAndWait();
                    
                }
                
            }
            
        }
        
        if (action.equalsIgnoreCase("Editar cliente")) {
            
            stage.setTitle("Wolosky - Editar cliente");
            
            Client client = this.clientsTV.getSelectionModel().getSelectedItem();
            
            if (client != null) {
                
                String currentName = client.getNombre();
                String currentEmail = client.getEmail();
                
                int index = this.clientsTV.getSelectionModel().getSelectedIndex();

                inputDialog.setClient(client);

                inputDialog.setTitle("Editar cliente");

                stage.showAndWait();

                Client editedClient = inputDialog.getClient();

                this.clients.get(index).updateData(editedClient);
                
                //serializeClients();
                sqlConnection.update("UPDATE clients "
                        + "SET "
                            + "nombre = '" + editedClient.getNombre() + "', "
                            + "email = '" + editedClient.getEmail() + "', "
                            + "sexo = '" + editedClient.getSexo() + "', "
                            + "telefono = '" + editedClient.getTelefono() + "', "
                            + "edad = " + editedClient.getEdad() + ", "
                            + "tipo = '" + editedClient.getTipo() + "' "
                        
                        + "WHERE "
                            + "nombre = '" + currentName + "' AND "
                            + "email = '" + currentEmail   + "'; "
                );

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setHeaderText("Editar");
                alert.setContentText("El cliente: " + editedClient + " ha sido editado correctamente");
                alert.showAndWait();
                
            }
            
        }
        
        this.fillTypeMenu();
        
        clientsTV.getSelectionModel().selectFirst();
        
        this.clientsTV.refresh();
        
    }
    
    @FXML
    private void showDetailsDialog() throws IOException {
        
        Client client = clientsTV.getSelectionModel().getSelectedItem();
        
        if (client != null) {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/wolosky/admin/dialog/DetailsDialogFXML.fxml"));

            Stage stage = new Stage();
            stage.setScene(new Scene((Pane) loader.load()));

            DetailsDialogController detailsDialog = loader.<DetailsDialogController>getController();

            detailsDialog.initData(client);
            
            stage.showAndWait();
            
        }
        
    }
    
    @FXML
    private void showAdded() throws IOException {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/wolosky/admin/dialog/clients/ShowClientsFXML.fxml"));
        
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        
        ShowClientsController showClients = loader.<ShowClientsController>getController();
        
        showClients.initData(this.sendList);
        
        stage.showAndWait();
        
        this.sendList = showClients.getList();
        
        System.out.println("Ahora su lista tiene " + this.sendList.size() + " clientes");
        
    }
    
    @FXML
    private void fillSendArray(ActionEvent event) {
        
        if (this.rangePane.isVisible()) {
            this.rangePane.setVisible(false);
        }
        
        String type = ((MenuItem)event.getSource()).getText();
        
        this.menuButton.setText("Envío: " + type);
        
        if (type.equals("Todos")) {
            
            this.sendList.setAll(this.clients);
            
            System.out.println("Añadiendo a todos los clientes");
            
        } else {
            
            if (type.equals("Nadie")) {
                
                this.sendList.clear();
                
                System.out.println("Sólo se añadirán los clientes del campo de texto \"Destinatarios\"");
                
            } else {
                
                for (Client client : clients) {
            
                    if (client.getTipo().equals(type)) {

                        this.sendList.add(client);

                    }

                }
                
                removeDuplicates();
                
            }
            
        }
        
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Rellado de destinarios");
        alert.setHeaderText("Se ha llenado la lista de clientes de envío con el tipo " + type);
        alert.setContentText("Se han añadido " + this.sendList.size() + " clientes...");
        alert.showAndWait();
        
    }
    
    @FXML
    private void showRangePane() {
        
        this.rangePane.setVisible(true);
        
    }
    
    @FXML
    private void fillSendArrayByRange() {
        
        this.menuButton.setText("Envío: Edad");
        
        try {
            
            int low = Integer.parseInt(this.lowTF.getText());
            int high = Integer.parseInt(this.highTF.getText());
            
            for (Client client : clients) {

                int edad = client.getEdad();

                if (edad >= low && edad <= high) {

                    this.sendList.add(client);

                }

            }
            
            removeDuplicates();
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Rellado de destinarios");
            alert.setHeaderText("Se ha llenado la lista de clientes de envío con el rango de edad [" + low + " - " + high + "]");
            alert.setContentText("Se han añadido " + this.sendList.size() + " clientes...");
            alert.showAndWait();
            
        } catch (Exception ex) {
            
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Ha olvidado llenar algún campo");
            alert.setContentText("Por favor introduzca los campos de rango de edad faltantes...");
            alert.showAndWait();
            
        }
        
    }
    
    private void fillTypeMenu() {
        
        this.typeMenu.getItems().clear();
        
        for (String type : arrTypes) {
            
            MenuItem menuItem = new MenuItem(type);
            
            menuItem.setOnAction(this::fillSendArray);
            
            this.typeMenu.getItems().add(menuItem);
            
        }
        
    }
    
    private void showAlert(AlertType alertType, String header, String content) {
        
        Alert alert = new Alert(alertType);
        
        if (alertType.name().equalsIgnoreCase("Information")) {
            
            alert.setTitle("Información");
            
        } else {
            
            if (alertType.name().equalsIgnoreCase("Error")) {
                
                alert.setTitle("Información");
                
            }
            
        }
        
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
        
    }
    
    class Updater implements Runnable {
        
        private volatile boolean isRunning = true;

        @Override
        public void run() {
            
            while (isRunning) {
                
                try {Thread.sleep(200);} catch (InterruptedException ex) {}
                
                fill();
                
            }
            
        }
        
        public void kill() {
            isRunning = false;
        }
        
    }
    
}