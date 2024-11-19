package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.content.Context;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ValidationUtilsTest {

    private Context mockContext;

    @Before
    public void setUp() {
        mockContext = mock(Context.class);

        when(mockContext.getString(R.string.regex_email_validation)).thenReturn("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        when(mockContext.getString(R.string.regex_password_validation)).thenReturn("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        when(mockContext.getString(R.string.d_10)).thenReturn("^[0-9]{10}$");
    }

    // Test valid email with valid format
    @Test
    public void testValidEmail() {
        boolean result = ValidationUtils.isValidEmail(mockContext, "test@example.com");
        assertTrue("Email should be valid", result);
    }

    // Test invalid email format
    @Test
    public void testInvalidEmail() {
        boolean result = ValidationUtils.isValidEmail(mockContext, "test@.com");
        assertFalse("Email should be invalid", result);
    }

    // Test valid password format (at least 8 characters, with special characters, numbers, and mixed case)
    @Test
    public void testValidPassword() {
        boolean result = ValidationUtils.isValidPassword(mockContext, "Test@1234");
        assertTrue("Password should be valid", result);
    }

    // Test invalid password format (missing special character)
    @Test
    public void testInvalidPasswordMissingSpecialChar() {
        boolean result = ValidationUtils.isValidPassword(mockContext, "Test1234");
        assertFalse("Password should be invalid due to missing special character", result);
    }

    // Test valid phone number (exactly 10 digits)
    @Test
    public void testValidPhone() {
        boolean result = ValidationUtils.isValidPhone(mockContext, "1234567890");
        assertTrue("Phone number should be valid", result);
    }

    // Test invalid phone number (less than 10 digits)
    @Test
    public void testInvalidPhoneShort() {
        boolean result = ValidationUtils.isValidPhone(mockContext, "12345");
        assertFalse("Phone number should be invalid (less than 10 digits)", result);
    }

    // Test invalid phone number (more than 10 digits)
    @Test
    public void testInvalidPhoneLong() {
        boolean result = ValidationUtils.isValidPhone(mockContext, "123456789012");
        assertFalse("Phone number should be invalid (more than 10 digits)", result);
    }

    // Test invalid phone number (contains non-numeric characters)
    @Test
    public void testInvalidPhoneNonNumeric() {
        boolean result = ValidationUtils.isValidPhone(mockContext, "1234abcd90");
        assertFalse("Phone number should be invalid (non-numeric characters)", result);
    }

    // Ensure that the context is not null
    @Test
    public void testContextNotNull() {
        assertNotNull("Context should not be null", mockContext);
    }

    // Test valid email format (alternative regex pattern)
    @Test
    public void testValidEmailAlternative() {
        when(mockContext.getString(R.string.regex_email_validation)).thenReturn("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        boolean result = ValidationUtils.isValidEmail(mockContext, "valid.email@example.com");
        assertEquals("Email result should be true", true, result);
    }
}
