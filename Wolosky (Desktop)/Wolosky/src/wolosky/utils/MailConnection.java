package wolosky.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author VakSF
 */
public class MailConnection {
    
    private final String email;
    private final Session session;
    
    public MailConnection(String email, String password, String host, String port) {
        
        this.email = email;
        
        Properties props = this.getProperties(email, host, port);

        this.session = Session.getInstance(props, new Authenticator() {

            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(email, password);
            }
            
        });
        
        this.session.setDebug(true);
        
        
    }
    
    public void sendMessage(String to, String subject, String text, String type) {
        
        try {
            
            Message message = new MimeMessage(this.session);
           
            message.setFrom(new InternetAddress(this.email));
           
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            message.setSubject(subject);
           
            if (type.equals("html")) {
                
                message.setContent(text, "text/html");

            } else {
                
                message.setText(text);
                
            }

            Transport.send(message);

            System.out.println("El mensaje ha sido enviado correctamente!");

        } catch (MessagingException ex) {
            
            throw new RuntimeException(ex);
            
        }
        
    }
    
    private Properties getProperties(String email, String host, String port) {
        
        Properties props = new Properties();
        
        props.put("mail.smtp.user", email);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        
        return props;
        
    }
    
}