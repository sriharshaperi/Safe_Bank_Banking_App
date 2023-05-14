package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
	public static String hashPassword(String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return hashedPassword;
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        boolean passwordMatch = BCrypt.checkpw(password, hashedPassword);
        return passwordMatch;
    }
}
