package wolosky.utils;

import javafx.scene.control.Button;
import wolosky.admin.Client;

/**
 *
 * @author Arturh
 */
public class ButtonClient extends Button {
    
    private Client client;
    
    public ButtonClient() {}

    public ButtonClient(String text) {
        super(text);
    }
    
    public void updateData(String data) {
        this.setText(data);
    }
    
    public Client getClient() {
        return new Client(super.getText());
    }
    
}
