package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.content.Context;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    // Validate Email format using regex
    public static boolean isValidEmail(Context context, String email) {
        String emailRegex = context.getString(R.string.regex_email_validation);
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Validate Password format (at least 8 characters, uppercase, lowercase, number, and special character)
    public static boolean isValidPassword(Context context, String password) {
        String passwordRegex = context.getString(R.string.regex_password_validation);
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    // Validate Phone Number (exactly 10 digits)
    public static boolean isValidPhone(Context context, String phone) {
        String phoneRegex = context.getString(R.string.d_10);
        return phone.length() == 10 && phone.matches(phoneRegex);
    }
}
