package wolosky.admin;

/**
 *
 * @author VakSF
 */

public class Client {
    
    private String nombre;
    private String email;
    private String sexo;
    private String telefono;
    private int edad;
    
    private String tipo;
    
    private int hash;

    public Client(String email) {
        this.email = email;
    }
    
    public Client(String nombre, String email, String sexo, String telefono, int edad, String tipo) {
        
        this.nombre = nombre;
        this.email = email;
        this.sexo = sexo;
        this.telefono = telefono;
        this.edad = edad;
        this.tipo = tipo;
        
    }
    
    public void updateData(Client client) {
        
        this.nombre = client.getNombre();
        this.email = client.getEmail();
        this.sexo = client.getSexo();
        this.telefono = client.getTelefono();
        this.edad = client.getEdad();
        this.tipo = client.getTipo();
        
        
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getSexo() {
        return sexo;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getEdad() {
        return edad;
    }
    
    public String getTipo() {
        return tipo;
    }

    @Override
    public int hashCode() {
        
        int h = hash;
        
        char[] value = this.toString().toCharArray();
        
        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return h;
    }
    
    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof Client) {
            Client anotherClient = (Client) anObject;
            
            if (anotherClient.toString().equals(this.toString())) {
                return true;
            }
            
        }
        return false;
    }

    @Override
    public String toString() {
        
        return nombre  + " : " + email + " : " + edad + " : " + tipo;
        
    }
    
}