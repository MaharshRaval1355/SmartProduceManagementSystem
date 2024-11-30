package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, userName, phoneNumber, confirmPassword;
    private Button signupButton;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            startActivity(new Intent(SignUp.this, MainActivity.class));
            finish();
        }

        // Initialize UI elements
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.confirm_password);
        userName = findViewById(R.id.user_name);
        phoneNumber = findViewById(R.id.phone_number);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signupEmail.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();
                String confirmPass = confirmPassword.getText().toString().trim();
                String name = userName.getText().toString().trim();
                String phone = phoneNumber.getText().toString().trim();

                if (email.isEmpty()) {
                    signupEmail.setError(getString(R.string.email_cannot_be_empty));
                    return;
                }
                if (!ValidationUtils.isValidEmail(SignUp.this, email)) {
                    signupEmail.setError(getString(R.string.invalid_email));
                    return;
                }
                if (password.isEmpty()) {
                    signupPassword.setError(getString(R.string.password_cannot_be_empty));
                    return;
                }
                if (!ValidationUtils.isValidPassword(SignUp.this, password)) {
                    signupPassword.setError(getString(R.string.invalid_password));
                    return;
                }
                if (confirmPass.isEmpty()) {
                    confirmPassword.setError(getString(R.string.confirm_password_cannot_be_empty));
                    return;
                }
                if (!password.equals(confirmPass)) {
                    confirmPassword.setError(getString(R.string.passwords_must_match));
                    return;
                }
                if (name.isEmpty()) {
                    userName.setError(getString(R.string.full_name_cannot_be_empty));
                    return;
                }
                if (phone.isEmpty()) {
                    phoneNumber.setError(getString(R.string.phone_number_cannot_be_empty));
                    return;
                }
                if (!ValidationUtils.isValidPhone(SignUp.this, phone)) {
                    phoneNumber.setError(getString(R.string.invalid_phone_number));
                    return;
                }


                // Register user
                FirebaseUtils.registerUser(auth, email, password, name, phone, new FirebaseUtils.FirebaseCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(SignUp.this, getString(R.string.signup_successful), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this, Login.class));
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(SignUp.this, getString(R.string.signup_failed) + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });
    }
}
