package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, userName, phoneNumber;
    private Button signupButton;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize UI elements
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        userName = findViewById(R.id.user_name);
        phoneNumber = findViewById(R.id.phone_number);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignUp();
            }
        });

        loginRedirectText.setOnClickListener(view -> {
            startActivity(new Intent(SignUp.this, Login.class));
        });
    }

    private void handleSignUp() {
        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String name = userName.getText().toString().trim();
        String phone = phoneNumber.getText().toString().trim();

        // Validate input fields
        if (!ValidationUtils.isValidEmail(this, email)) {
            signupEmail.setError(getString(R.string.invalid_email));
            return;
        }
        if (!ValidationUtils.isValidPassword(this, password)) {
            signupPassword.setError(getString(R.string.invalid_password));
            return;
        }
        if (name.isEmpty()) {
            userName.setError(getString(R.string.full_name_cannot_be_empty));
            return;
        }
        if (!ValidationUtils.isValidPhone(this, phone)) {
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
}
