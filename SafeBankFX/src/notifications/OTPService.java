package notifications;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class OTPService {
	private static Properties properties = null;
	private static String TEST_SID = null;
	private static String PROD_SID = null;
	private static String TEST_API_KEY = null;
	private static String PROD_API_KEY = null;
	static {
		properties = new Properties();
		try {
			properties.load(new FileInputStream("env.properties"));
			TEST_SID = properties.getProperty("TWILIO_TEST_SID");
			TEST_API_KEY = properties.getProperty("TWILIO_TEST_API_KEY");
			PROD_SID = properties.getProperty("TWILIO_PROD_SID");
			PROD_API_KEY = properties.getProperty("TWILIO_PROD_API_KEY");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static final String TWILIO_SID = PROD_SID;
	private static final String TWILIO_API_KEY = PROD_API_KEY;
			
	
	public static void sendSMS(String phoneNumber) {
		Twilio.init(TWILIO_SID, TWILIO_API_KEY);
		PhoneNumber from = new PhoneNumber(phoneNumber);
		PhoneNumber to = new PhoneNumber("+18572005105");
		String messageBody = "Safe Bank Test";
        Message message = Message.creator(to,from,messageBody).create();
        System.out.println(message.getSid());
	}
	
}
