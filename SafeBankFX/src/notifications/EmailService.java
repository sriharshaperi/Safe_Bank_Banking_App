package notifications;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {

	private static final String fromEmail;
    private static final String password;
    private static final Properties properties;

    static {
    	properties = new Properties();
        try {
			properties.load(new FileInputStream("env.properties"));
		} catch (FileNotFoundException fileNotFoundException) {
			// TODO Auto-generated catch block
			Logger.getLogger(EmailService.class.getName())
			.log(Level.SEVERE, null, fileNotFoundException);

		} catch (IOException ioException) {
			// TODO Auto-generated catch block
			Logger.getLogger(EmailService.class.getName())
			.log(Level.SEVERE, null, ioException);
		}
        fromEmail = properties.getProperty("SENDER_EMAIL");
		password = properties.getProperty("SENDER_PASSWORD");
		properties.put("mail.smtp.host", "smtp.mail.yahoo.com");
		properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
    }


    public static boolean sendEmail(String toEmail, String subject, String message) {
    	boolean emailSent = false;
    	try {
    		// Create a Session object with authentication details
            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password); // Replace with your email and password
                }
            });

            // Create the email message
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(fromEmail)); // Replace with your email
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail)); // Replace with the recipient's email
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            // Send the email
            Transport.send(mimeMessage);
            System.out.println("Email sent successfully.");
            emailSent = true;
		} catch (MessagingException messagingException) {
			// TODO: handle exception
			 emailSent = false;
		}
    	return emailSent;
    }
}

