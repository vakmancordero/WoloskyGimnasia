package wolosky.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Arturh
 */
public class Serializer {
    
    public void serialize(Object data, String path) {
        
        try (FileOutputStream outFile = new FileOutputStream(path)) {
            try (ObjectOutputStream output = new ObjectOutputStream(outFile)) {
                output.writeObject(data);
                output.close();
            }
            System.out.println("Archivo serializado correctamente!\n");
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. FileNotFoundException");
        } catch (IOException ex) {
            System.out.println("File not found. IOException");
            ex.printStackTrace();
        }
        
    }
    
    public Object deserialize(String path) {
        
        Object object = null;
        
        try (FileInputStream inFile = new FileInputStream(path)) {
            try (ObjectInputStream input = new ObjectInputStream(inFile)) {
                try {
                    object = input.readObject();
                    input.close();
                } catch (ClassNotFoundException ex) {
                    System.out.println("Class not found.");
                }
                System.out.println("Archivo deserializado correctamente!\n");
            }
        } catch (IOException ex) {
            System.out.println("El archivo aun no ha sido creado.");
        }
        
        return object;
    }
    
}