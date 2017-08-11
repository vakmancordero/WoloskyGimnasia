package wolosky.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author VakSF
 */
public class FTPConnection {
    
    private String server;
    private String user;
    private String password;
    private int port;
    
    private FTPClient ftpClient;

    public FTPConnection(String server, String user, String password, int port) {
        
        this.server = server;
        this.user = user;
        this.password = password;
        this.port = port;
        
        //initConnection();
        
    }
    
    private void initConnection() {
        
        this.ftpClient = new FTPClient();
        
        try {
            
            this.ftpClient.connect(server, port);
            boolean logged = this.ftpClient.login(user, password);
            
            if (logged) {
                
                System.out.println("Conectado!");
                
                this.ftpClient.enterLocalPassiveMode();
                this.ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                
            } else {
                
                System.out.println("Error en la conexión al servidor FTP");
                
            }
            
        } catch (IOException ex) {
            
            System.out.println("Error en la conexión al servidor FTP");
            
        }
        
    }
    
    public boolean storeFile(File file, String path) {
        
        initConnection();
        
        boolean stored = false;
        
        try {
            
            FileInputStream inputStream = new FileInputStream(file);
            
            String fileName = file.getName();
            
            System.out.println("Almacenando archivo... " + fileName);
            
            stored = this.ftpClient.storeFile(path + fileName, inputStream);
            
            if (stored) {
                
                System.out.println("Archivo almacenado correctamente!");
                
            }
            
        } catch (FileNotFoundException ex) {
            
            System.out.println("El archivo no existe");
            
        } catch (IOException ex) {
            
        } finally {
            
            try {
                
                if (ftpClient.isConnected()) {
                    
                    ftpClient.logout();
                    
                    ftpClient.disconnect();
                    
                }
                
            } catch (IOException ex) {
            
            }
            
        }
        
        return stored;
        
    }
    
}