package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class Login extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private Button loginButton, googleSignInButton;
    private TextView signupRedirectText;
    private CheckBox rememberMeCheckBox;
    private LoginManager loginManager;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginManager = new LoginManager(this);

        // Check if the user is already logged in
        if (loginManager.isUserLoggedIn()) {
            navigateToMainActivity();
            return;
        }

        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signUpRedirectText);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);

        loginButton.setOnClickListener(v -> loginWithEmailPassword());
        googleSignInButton.setOnClickListener(v -> googleSignIn());
        signupRedirectText.setOnClickListener(v -> navigateToSignUp());

        // Initialize Google Sign-In
        googleSignInClient = loginManager.initializeGoogleSignIn();
    }

    private void loginWithEmailPassword() {
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        if (loginManager.isValidEmail(email) && loginManager.isValidPassword(password)) {
            loginManager.loginWithEmailPassword(email, password, new LoginManager.LoginCallback() {
                @Override
                public void onSuccess() {
                    if (rememberMeCheckBox.isChecked()) {
                        loginManager.saveLoginState(email, password);
                    }
                    navigateToMainActivity();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(Login.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Invalid email or password format", Toast.LENGTH_SHORT).show();
        }
    }

    private void googleSignIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, LoginManager.RC_SIGN_IN);
    }

    private void navigateToSignUp() {
        startActivity(new Intent(Login.this, SignUp.class));
    }

    private void navigateToMainActivity() {
        startActivity(new Intent(Login.this, MainActivity.class));
        finish();
    }
}
