package validations;

public class UserValidations {

	public static boolean isEmailValid(String email) {
		String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		return email.matches(regex);
	}

	public static boolean isPasswordValid(String password) {
		if (password.length() < 8) {
			return false;
		}
		if (!password.matches(".*[A-Z].*")) {
			return false; // missing uppercase letter
		}
		if (!password.matches(".*[a-z].*")) {
			return false; // missing lowercase letter
		}
		if (!password.matches(".*\\d.*")) {
			return false; // missing number
		}
		if (!password.matches(".*[!@#$%^&*()].*")) {
			return false; // missing special character
		}
		return true; // password meets all requirements
	}

	public static boolean isPhoneValid(String txtPhone) {
		try {
			long phone = Long.parseLong(txtPhone);
			// Check that the input is not null or empty
			if (txtPhone == null || txtPhone.isEmpty()) {
				return false;
			}

			if (phone >= 1111111111L && phone <= 9999999999L)
				return true;// Phone number is valid

			return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	public static boolean isNameValid(String name) {
		if (name == null || name.trim().isEmpty()) {
	        return false; // Name is null, empty, or contains only whitespace
	    }
	    if (!name.matches("^[a-zA-Z]+([\\s]?[a-zA-Z]+)*$")) {
	        return false; // Name contains non-alphabetic characters or invalid whitespace
	    }
	    if (name.length() < 2 || name.length() > 50) {
	        return false; // Name length is out of range
	    }
	    return true; // Name is valid
	}
}
